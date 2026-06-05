package net.andrespr.casinorocket;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.command.CasinoRocketCommands;
import net.andrespr.casinorocket.config.CasinoRocketConfig;
import net.andrespr.casinorocket.condition.ModConditions;
import net.andrespr.casinorocket.games.gachapon.GachaponUtils;
import net.andrespr.casinorocket.games.gachapon.PlushiesGachaponUtils;
import net.andrespr.casinorocket.games.gachapon.PokemonGachaponUtils;
import net.andrespr.casinorocket.games.slot.SlotReels;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.ModItemsGroup;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.andrespr.casinorocket.network.SuitSyncPayload;
import net.andrespr.casinorocket.network.s2c.MoneyValuesSyncS2CPayload;
import net.andrespr.casinorocket.network.s2c.SlotConfigSyncS2CPayload;
import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.CobbledollarsBankIntegration;
import net.andrespr.casinorocket.util.SuitData;
import net.andrespr.casinorocket.villager.ModVillagers;
import net.andrespr.casinorocket.villager.ShopsRegistry;
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

@Mod(CasinoRocket.MOD_ID)
public class CasinoRocket {

    public static final String MOD_ID = "casinorocket";
    public static final String NETWORK_VERSION = "1";
    public static final Logger LOGGER = LoggerFactory.getLogger("CasinoRocket");
    public static CasinoRocketConfig CONFIG;

    public CasinoRocket(IEventBus modEventBus, ModContainer modContainer) {
        AutoConfig.register(CasinoRocketConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(CasinoRocketConfig.class).getConfig();
        SlotReels.reloadFromConfig(CasinoRocket.CONFIG.slotMachine);

        ModBlocks.registerModBlocks(modEventBus);
        ModItems.registerModItems(modEventBus);
        ModSounds.registerSounds(modEventBus);
        ModBlockEntities.registerBlockEntities(modEventBus);
        ModMenuTypes.registerMenuTypes(modEventBus);
        ModVillagers.registerVillagers(modEventBus);
        ModItemsGroup.registerItemGroups(modEventBus);
        ModConditions.register(modEventBus);

        modEventBus.addListener(CasinoRocketDataGenerator::gatherData);
        modEventBus.addListener(CasinoRocketPackets::registerPayloads);

        NeoForge.EVENT_BUS.addListener(this::onCommandsRegister);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(this::onStartTracking);
        NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        NeoForge.EVENT_BUS.addListener(this::onBlockBreak);

        ShopsRegistry.bootstrap();
        LOGGER.info("Mod initialized successfully!");
    }

    private void onCommandsRegister(RegisterCommandsEvent event) {
        CasinoRocketCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CasinoRocketPackets.sendToPlayer(player, MoneyValuesSyncS2CPayload.fromServer());
            CasinoRocketPackets.sendToPlayer(player, SlotConfigSyncS2CPayload.fromServer());
        }
    }

    private void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof Villager villager) {
            CasinoRocketPackets.sendToPlayer(player, new SuitSyncPayload(villager.getId(), SuitData.getSuit(villager)));
        }
    }

    private void onServerStarted(ServerStartedEvent event) {
        GachaponUtils.buildCache(CasinoRocket.CONFIG.itemGachapon.pools);
        PokemonGachaponUtils.buildCache(CasinoRocket.CONFIG.pokemonGachapon.pools);
        PlushiesGachaponUtils.buildCache(CasinoRocket.CONFIG.plushiesGachapon.plushies);
        CobbledollarsBankIntegration.registerChipsForBuyback();
    }

    private void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!CasinoRocket.CONFIG.generalConfig.makeMachinesUnbreakable) {
            return;
        }

        Player player = event.getPlayer();
        if (player.isCreative() || player.hasPermissions(2)) {
            return;
        }

        if (isCasinoMachine(event.getState())) {
            event.setCanceled(true);
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.machine_unbreakable", true);
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


