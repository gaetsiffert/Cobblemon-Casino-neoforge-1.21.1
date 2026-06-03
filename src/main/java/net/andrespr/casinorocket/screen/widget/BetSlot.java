package net.andrespr.casinorocket.screen.widget;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BetSlot extends Slot {

    public BetSlot(Container inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
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
    public int getMaxStackSize() {
        return 64;
    }

}

