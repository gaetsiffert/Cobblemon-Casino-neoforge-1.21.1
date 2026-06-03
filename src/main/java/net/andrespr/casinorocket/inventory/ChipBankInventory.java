package net.andrespr.casinorocket.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class ChipBankInventory extends SimpleContainer {

    public ChipBankInventory() {
        super(27);
    }

    // ===== NBT =====

    public void fromTag(ListTag list, HolderLookup.Provider registries) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            this.setItem(i, ItemStack.EMPTY);
        }

        for (int i = 0; i < list.size(); i++) {
            CompoundTag c = list.getCompound(i);
            int slot = c.getByte("Slot") & 255;
            if (slot >= 0 && slot < this.getContainerSize()) {
                this.setItem(slot, ItemStack.parse(registries, c).orElse(ItemStack.EMPTY));
            }
        }
    }

    public ListTag createTag(HolderLookup.Provider registries) {
        ListTag list = new ListTag();

        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag c = new CompoundTag();
                c.putByte("Slot", (byte) i);
                list.add(stack.save(registries, c));
            }
        }

        return list;
    }

}

