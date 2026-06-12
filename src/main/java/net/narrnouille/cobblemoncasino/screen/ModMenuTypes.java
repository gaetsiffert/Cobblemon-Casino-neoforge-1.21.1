package net.narrnouille.cobblemoncasino.screen;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.screen.custom.blackjack.BlackjackTableScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.chip_table.ChipTableScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.common.BetScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.common.WithdrawScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.ledger.CasinoLedgerScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineMenuScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineScreenHandler;
import net.narrnouille.cobblemoncasino.screen.opening.BlackjackTableOpenData;
import net.narrnouille.cobblemoncasino.screen.opening.CasinoLedgerOpenData;
import net.narrnouille.cobblemoncasino.screen.opening.ChipTableOpenData;
import net.narrnouille.cobblemoncasino.screen.opening.CommonMachineOpenData;
import net.narrnouille.cobblemoncasino.screen.opening.SlotMachineOpenData;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, CobblemonCasino.MOD_ID);

    public static MenuType<SlotMachineScreenHandler> SLOT_MACHINE_MENU_TYPE;
    public static MenuType<SlotMachineMenuScreenHandler> SLOT_MACHINE_CONFIG_MENU_TYPE;
    public static MenuType<BetScreenHandler> BET_MENU_TYPE;
    public static MenuType<WithdrawScreenHandler> WITHDRAW_MENU_TYPE;
    public static MenuType<BlackjackTableScreenHandler> BLACKJACK_TABLE_MENU_TYPE;
    public static MenuType<ChipTableScreenHandler> CHIP_TABLE_MENU_TYPE;
    public static MenuType<CasinoLedgerScreenHandler> CASINO_LEDGER_MENU_TYPE;

    static {
        register("slot_machine_menu", () -> SLOT_MACHINE_MENU_TYPE =
                IMenuTypeExtension.create((syncId, inv, buf) -> new SlotMachineScreenHandler(syncId, inv, SlotMachineOpenData.CODEC.decode(buf))));
        register("slot_machine_config_menu", () -> SLOT_MACHINE_CONFIG_MENU_TYPE =
                IMenuTypeExtension.create((syncId, inv, buf) -> new SlotMachineMenuScreenHandler(syncId, inv, SlotMachineOpenData.CODEC.decode(buf))));
        register("bet_menu", () -> BET_MENU_TYPE =
                IMenuTypeExtension.create((syncId, inv, buf) -> new BetScreenHandler(syncId, inv, CommonMachineOpenData.CODEC.decode(buf))));
        register("withdraw_menu", () -> WITHDRAW_MENU_TYPE =
                IMenuTypeExtension.create((syncId, inv, buf) -> new WithdrawScreenHandler(syncId, inv, CommonMachineOpenData.CODEC.decode(buf))));
        register("blackjack_table_menu", () -> BLACKJACK_TABLE_MENU_TYPE =
                IMenuTypeExtension.create((syncId, inv, buf) -> new BlackjackTableScreenHandler(syncId, inv, BlackjackTableOpenData.CODEC.decode(buf))));
        register("chip_table_menu", () -> CHIP_TABLE_MENU_TYPE =
                IMenuTypeExtension.create((syncId, inv, buf) -> new ChipTableScreenHandler(syncId, inv, ChipTableOpenData.CODEC.decode(buf))));
        register("casino_ledger_menu", () -> CASINO_LEDGER_MENU_TYPE =
                IMenuTypeExtension.create((syncId, inv, buf) -> new CasinoLedgerScreenHandler(syncId, inv, CasinoLedgerOpenData.CODEC.decode(buf))));
    }

    private static <T extends MenuType<?>> void register(String name, Supplier<T> supplier) {
        MENU_TYPES.register(name, supplier);
    }

    public static void registerMenuTypes(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
        CobblemonCasino.LOGGER.info("Registering menu types for " + CobblemonCasino.MOD_ID);
    }
}
