package net.narrnouille.cobblemoncasino;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.narrnouille.cobblemoncasino.block.ModBlocks;
import net.narrnouille.cobblemoncasino.block.entity.ModBlockEntities;
import net.narrnouille.cobblemoncasino.command.CobblemonCasinoCommands;
import net.narrnouille.cobblemoncasino.config.CobblemonCasinoConfig;
import net.narrnouille.cobblemoncasino.condition.ModConditions;
import net.narrnouille.cobblemoncasino.games.gachapon.GachaponUtils;
import net.narrnouille.cobblemoncasino.games.gachapon.PlushiesGachaponUtils;
import net.narrnouille.cobblemoncasino.games.gachapon.PokemonGachaponUtils;
import net.narrnouille.cobblemoncasino.games.slot.SlotReels;
import net.narrnouille.cobblemoncasino.item.ModItems;
import net.narrnouille.cobblemoncasino.item.ModItemsGroup;
import net.narrnouille.cobblemoncasino.network.CobblemonCasinoPackets;
import net.narrnouille.cobblemoncasino.network.SuitSyncPayload;
import net.narrnouille.cobblemoncasino.network.s2c.MoneyValuesSyncS2CPayload;
import net.narrnouille.cobblemoncasino.network.s2c.SlotConfigSyncS2CPayload;
import net.narrnouille.cobblemoncasino.screen.ModMenuTypes;
import net.narrnouille.cobblemoncasino.sound.ModSounds;
import net.narrnouille.cobblemoncasino.sound.SlotJackpotSoundScheduler;
import net.narrnouille.cobblemoncasino.util.CobblemonCasinoLogger;
import net.narrnouille.cobblemoncasino.util.CobbledollarsBankIntegration;
import net.narrnouille.cobblemoncasino.util.SuitData;
import net.narrnouille.cobblemoncasino.villager.CasinoVillagerTrades;
import net.narrnouille.cobblemoncasino.villager.ModVillagers;
import net.narrnouille.cobblemoncasino.villager.ShopsRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CobblemonCasino.MOD_ID)
public class CobblemonCasino {

    public static final String MOD_ID = "cobblemoncasino";
    public static final String NETWORK_VERSION = "1";
    public static final Logger LOGGER = LoggerFactory.getLogger("CobblemonCasino");
    public static CobblemonCasinoConfig CONFIG;

    public CobblemonCasino(IEventBus modEventBus, ModContainer modContainer) {
        AutoConfig.register(CobblemonCasinoConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(CobblemonCasinoConfig.class).getConfig();
        SlotReels.reloadFromConfig(CobblemonCasino.CONFIG.slotMachine);

        ModBlocks.registerModBlocks(modEventBus);
        ModItems.registerModItems(modEventBus);
        ModSounds.registerSounds(modEventBus);
        ModBlockEntities.registerBlockEntities(modEventBus);
        ModMenuTypes.registerMenuTypes(modEventBus);
        ModVillagers.registerVillagers(modEventBus);
        ModItemsGroup.registerItemGroups(modEventBus);
        ModConditions.register(modEventBus);

        modEventBus.addListener(CobblemonCasinoDataGenerator::gatherData);
        modEventBus.addListener(CobblemonCasinoPackets::registerPayloads);

        NeoForge.EVENT_BUS.addListener(this::onCommandsRegister);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(this::onStartTracking);
        NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        NeoForge.EVENT_BUS.addListener(this::onBlockBreak);
        NeoForge.EVENT_BUS.addListener(SlotJackpotSoundScheduler::onServerTick);
        NeoForge.EVENT_BUS.addListener(CasinoVillagerTrades::register);

        ShopsRegistry.bootstrap();
        LOGGER.info("Mod initialized successfully!");
    }

    private void onCommandsRegister(RegisterCommandsEvent event) {
        CobblemonCasinoCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CobblemonCasinoPackets.sendToPlayer(player, MoneyValuesSyncS2CPayload.fromServer());
            CobblemonCasinoPackets.sendToPlayer(player, SlotConfigSyncS2CPayload.fromServer());
        }
    }

    private void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof Villager villager) {
            CobblemonCasinoPackets.sendToPlayer(player, new SuitSyncPayload(villager.getId(), SuitData.getSuit(villager)));
        }
    }

    private void onServerStarted(ServerStartedEvent event) {
        GachaponUtils.buildCache(CobblemonCasino.CONFIG.itemGachapon.pools);
        PokemonGachaponUtils.buildCache(CobblemonCasino.CONFIG.pokemonGachapon.pools);
        PlushiesGachaponUtils.buildCache(CobblemonCasino.CONFIG.plushiesGachapon.plushies);
        CobbledollarsBankIntegration.registerChipsForBuyback();
    }

    private void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!CobblemonCasino.CONFIG.generalConfig.makeMachinesUnbreakable) {
            return;
        }

        Player player = event.getPlayer();
        if (player.isCreative() || player.hasPermissions(2)) {
            return;
        }

        if (isCasinoMachine(event.getState())) {
            event.setCanceled(true);
            CobblemonCasinoLogger.toPlayerTranslated(player, "message.cobblemoncasino.machine_unbreakable", true);
        }
    }

    private static boolean isCasinoMachine(BlockState state) {
        Block block = state.getBlock();
        return block == ModBlocks.GACHA_MACHINE
                || block == ModBlocks.POKEMON_GACHA_MACHINE
                || block == ModBlocks.PLUSHIES_GACHA_MACHINE
                || block == ModBlocks.EVENT_GACHA_MACHINE
                || block == ModBlocks.SLOT_MACHINE
                || block == ModBlocks.BLACKJACK_TABLE
                || block == ModBlocks.CHIP_TABLE
                || block == ModBlocks.CASINO_SCOREBOARD;
    }
}


