package net.andrespr.casinorocket.screen.widget;

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
        return ModItems.ALL_CHIP_ITEMS.contains(stack.getItem());
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

}

