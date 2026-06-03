package net.andrespr.casinorocket.screen.custom.chip_table;

import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.screen.widget.BetSlot;
import net.andrespr.casinorocket.util.IChipBankHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChipTableScreenHandler extends AbstractContainerMenu {

    private final Container bankInventory;

    public ChipTableScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
        super(ModMenuTypes.CHIP_TABLE_MENU_TYPE, syncId);

        if (playerInventory.player.level().isClientSide) {
            this.bankInventory = new SimpleContainer(27);
        } else {
            this.bankInventory = ((IChipBankHolder) playerInventory.player).casinorocket$getChipBankInventory();
        }
        checkContainerSize(this.bankInventory, 27);
        this.bankInventory.startOpen(playerInventory.player);

        addChestInventory(this.bankInventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (invSlot < this.bankInventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.bankInventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.bankInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.bankInventory.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.bankInventory.stopOpen(player);
    }

    // ==== HELPER -> INVENTORIES =====

    private void addChestInventory(Container inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new BetSlot(inventory, l + i * 9, 7 + l * 18, 15 + i * 18));
            }
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 7 + l * 18, 90 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 7 + i * 18, 148));
        }
    }

}

