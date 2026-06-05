package net.andrespr.casinorocket;

import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.games.slot.SlotReels;
import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.screen.custom.blackjack.BlackjackTableScreen;
import net.andrespr.casinorocket.screen.custom.chip_table.ChipTableScreen;
import net.andrespr.casinorocket.screen.custom.common.BetScreen;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreen;
import net.andrespr.casinorocket.screen.custom.ledger.CasinoLedgerScreen;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreen;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = CasinoRocket.MOD_ID, value = Dist.CLIENT)
public final class CasinoRocketClient {

    private CasinoRocketClient() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            SlotReels.reloadFromConfig(CasinoRocket.CONFIG.slotMachine);

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


