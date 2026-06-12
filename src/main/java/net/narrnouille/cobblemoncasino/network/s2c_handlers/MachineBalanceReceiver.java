package net.narrnouille.cobblemoncasino.network.s2c_handlers;

import net.narrnouille.cobblemoncasino.network.s2c.SendMachineBalanceS2CPayload;
import net.narrnouille.cobblemoncasino.screen.custom.common.BetScreen;
import net.narrnouille.cobblemoncasino.screen.custom.common.WithdrawScreen;
import net.narrnouille.cobblemoncasino.screen.custom.common.WithdrawScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineScreen;
import net.narrnouille.cobblemoncasino.util.MoneyCalculator;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.List;

public final class MachineBalanceReceiver {

    private MachineBalanceReceiver() {}

    public static void handle(SendMachineBalanceS2CPayload payload, IPayloadContext context) {
        String machineKey = payload.machineKey();
        long amount = payload.amount();

        Minecraft.getInstance().execute(() -> {
            Minecraft client = Minecraft.getInstance();
            if (client == null) return;

            if (client.screen instanceof BetScreen betScreen) {
                betScreen.updateTotalAmount(amount);
                return;
            }

            if (client.screen instanceof WithdrawScreen withdrawScreen) {
                withdrawScreen.updateBalance(amount);
                WithdrawScreenHandler handler = withdrawScreen.getMenu();

                if (amount == 0) {
                    handler.clearWithdrawInventory();
                    return;
                }

                List<ItemStack> stacks = MoneyCalculator.calculateChipWithdraw(amount);
                handler.loadStacksIntoSlots(stacks);
                return;
            }

            if ("slots".equals(machineKey) && client.screen instanceof SlotMachineScreen slotScreen && !slotScreen.isSpinning()) {
                slotScreen.updateBalance(amount);
            }
        });
    }
}
