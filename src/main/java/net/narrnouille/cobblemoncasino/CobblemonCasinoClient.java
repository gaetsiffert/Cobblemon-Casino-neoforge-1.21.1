package net.narrnouille.cobblemoncasino;

import net.narrnouille.cobblemoncasino.block.ModBlocks;
import net.narrnouille.cobblemoncasino.games.slot.SlotReels;
import net.narrnouille.cobblemoncasino.screen.ModMenuTypes;
import net.narrnouille.cobblemoncasino.screen.custom.blackjack.BlackjackTableScreen;
import net.narrnouille.cobblemoncasino.screen.custom.chip_table.ChipTableScreen;
import net.narrnouille.cobblemoncasino.screen.custom.common.BetScreen;
import net.narrnouille.cobblemoncasino.screen.custom.common.WithdrawScreen;
import net.narrnouille.cobblemoncasino.screen.custom.ledger.CasinoLedgerScreen;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineMenuScreen;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = CobblemonCasino.MOD_ID, value = Dist.CLIENT)
public final class CobblemonCasinoClient {

    private CobblemonCasinoClient() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            SlotReels.reloadFromConfig(CobblemonCasino.CONFIG.slotMachine);

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLD_DOOR, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GOLD_TRAPDOOR, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GACHA_MACHINE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.POKEMON_GACHA_MACHINE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EVENT_GACHA_MACHINE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLUSHIES_GACHA_MACHINE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SLOT_MACHINE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BLACKJACK_TABLE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHIP_TABLE, RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CASINO_SCOREBOARD, RenderType.cutout());
        });
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.SLOT_MACHINE_MENU_TYPE, SlotMachineScreen::new);
        event.register(ModMenuTypes.SLOT_MACHINE_CONFIG_MENU_TYPE, SlotMachineMenuScreen::new);
        event.register(ModMenuTypes.BET_MENU_TYPE, BetScreen::new);
        event.register(ModMenuTypes.WITHDRAW_MENU_TYPE, WithdrawScreen::new);
        event.register(ModMenuTypes.BLACKJACK_TABLE_MENU_TYPE, BlackjackTableScreen::new);
        event.register(ModMenuTypes.CHIP_TABLE_MENU_TYPE, ChipTableScreen::new);
        event.register(ModMenuTypes.CASINO_LEDGER_MENU_TYPE, CasinoLedgerScreen::new);
    }
}


