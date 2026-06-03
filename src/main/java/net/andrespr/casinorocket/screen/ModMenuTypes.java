package net.andrespr.casinorocket.screen;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.screen.custom.blackjack.BlackjackTableScreenHandler;
import net.andrespr.casinorocket.screen.custom.chip_table.ChipTableScreenHandler;
import net.andrespr.casinorocket.screen.custom.common.BetScreenHandler;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreenHandler;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreenHandler;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreenHandler;
import net.andrespr.casinorocket.screen.opening.BlackjackTableOpenData;
import net.andrespr.casinorocket.screen.opening.CommonMachineOpenData;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, CasinoRocket.MOD_ID);

    public static MenuType<SlotMachineScreenHandler> SLOT_MACHINE_MENU_TYPE;
    public static MenuType<SlotMachineMenuScreenHandler> SLOT_MACHINE_CONFIG_MENU_TYPE;
    public static MenuType<BetScreenHandler> BET_MENU_TYPE;
    public static MenuType<WithdrawScreenHandler> WITHDRAW_MENU_TYPE;
    public static MenuType<BlackjackTableScreenHandler> BLACKJACK_TABLE_MENU_TYPE;
    public static MenuType<ChipTableScreenHandler> CHIP_TABLE_MENU_TYPE;

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
                IMenuTypeExtension.create((syncId, inv, buf) -> new ChipTableScreenHandler(syncId, inv, BlockPos.STREAM_CODEC.decode(buf))));
    }

    private static <T extends MenuType<?>> void register(String name, Supplier<T> supplier) {
        MENU_TYPES.register(name, supplier);
    }

    public static void registerMenuTypes(IEventBus eventBus) {
        MENU_TYPES.register(eventBus);
        CasinoRocket.LOGGER.info("Registering menu types for " + CasinoRocket.MOD_ID);
    }
}
