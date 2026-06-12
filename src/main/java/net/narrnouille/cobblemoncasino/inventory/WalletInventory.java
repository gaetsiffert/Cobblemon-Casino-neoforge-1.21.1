package net.narrnouille.cobblemoncasino.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class WalletInventory extends SimpleContainer {

    private static final String ITEMS_KEY = "Items";

    private final ItemStack walletStack;
    private final HolderLookup.Provider registries;
    private boolean loading;

    public WalletInventory(ItemStack walletStack, HolderLookup.Provider registries) {
        super(27);
        this.walletStack = walletStack;
        this.registries = registries;
        loadFromWallet();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (!loading) {
            saveToWallet();
        }
    }

    public void saveToWallet() {
        CompoundTag tag = currentTag();
        ListTag items = new ListTag();

        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                items.add(stack.save(registries, itemTag));
            }
        }

        tag.put(ITEMS_KEY, items);
        walletStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private void loadFromWallet() {
        loading = true;
        try {
            clearContent();
            CompoundTag tag = currentTag();
            if (!tag.contains(ITEMS_KEY, Tag.TAG_LIST)) {
                return;
            }

            ListTag items = tag.getList(ITEMS_KEY, Tag.TAG_COMPOUND);
            for (int i = 0; i < items.size(); i++) {
                CompoundTag itemTag = items.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot >= 0 && slot < this.getContainerSize()) {
                    this.setItem(slot, ItemStack.parse(registries, itemTag).orElse(ItemStack.EMPTY));
                }
            }
        } finally {
            loading = false;
        }
    }

    private CompoundTag currentTag() {
        CustomData data = walletStack.get(DataComponents.CUSTOM_DATA);
        return data != null ? data.copyTag() : new CompoundTag();
    }
}
