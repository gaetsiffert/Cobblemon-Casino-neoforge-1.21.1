package net.andrespr.casinorocket.screen.widget;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BetSlot extends Slot {

    public BetSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        boolean isChipItem = ModItems.ALL_CHIP_ITEMS.contains(stack.getItem());
        boolean isBillItem = ModItems.ALL_BILL_ITEMS.contains(stack.getItem());

        if (CasinoRocket.CONFIG.generalConfig.enableDirectBets) {
            if (CasinoRocket.CONFIG.generalConfig.isRelicCoinActive()) {
                boolean isDiamondItem = ModItems.DIAMOND_VALUES.containsKey(stack.getItem());
            }
            if (CasinoRocket.CONFIG.generalConfig.isDiamondActive()) {
                boolean isDiamondItem = ModItems.DIAMOND_VALUES.containsKey(stack.getItem());
            }
            if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {
                boolean isDiamondItem = ModItems.DIAMOND_VALUES.containsKey(stack.getItem());
            }
        }

        return isChipItem || isBillItem;
    }

    @Override
    public int getMaxItemCount() {
        return 64;
    }

}