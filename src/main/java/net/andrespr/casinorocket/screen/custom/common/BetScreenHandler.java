package net.andrespr.casinorocket.screen.custom.common;

import net.andrespr.casinorocket.item.custom.BillItem;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.network.s2c.sender.MachineBalanceSender;
import net.andrespr.casinorocket.screen.ModScreenHandlers;
import net.andrespr.casinorocket.screen.opening.CommonMachineOpenData;
import net.andrespr.casinorocket.screen.widget.BetSlot;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BetScreenHandler extends AbstractContainerMenu implements IMachineBoundHandler {

    private final BlockPos pos;
    private final String machineKey;

    private final SimpleContainer inventory = new SimpleContainer(27);
    private final Player player;
    private long totalMoney = 0L;

    public BetScreenHandler(int syncId, Inventory playerInventory, CommonMachineOpenData data) {
        this(syncId, playerInventory, data.pos(), data.machineKey());
    }

    public BetScreenHandler(int syncId, Inventory playerInventory, BlockPos pos, String machineKey) {
        super(ModScreenHandlers.BET_SCREEN_HANDLER, syncId);

        this.pos = pos;
        this.machineKey = machineKey;

        this.inventory.addListener(this::slotsChanged);
        this.player = playerInventory.player;

        addChestInventory(inventory);
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
            if (invSlot < this.inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, this.inventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, this.inventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return newStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);

        if (!player.level().isClientSide) {
            this.clearContainer(player, this.inventory);
        } else {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void slotsChanged(Container inventory) {
        super.slotsChanged(inventory);

        long total = 0;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                Item item = stack.getItem();

                if (item instanceof ChipItem chip) {
                    total += chip.getValue() * stack.getCount();
                } else if (item instanceof BillItem bill) {
                    total += bill.getValue() * stack.getCount();
                }
            }
        }
        this.totalMoney = total;

        if (player instanceof ServerPlayer serverPlayer) {
            MachineBalanceSender.send(serverPlayer, machineKey, total);
        }
    }

    // ==== HELPER -> INVENTORIES =====

    private void addChestInventory(Container inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new BetSlot(inventory, l + i * 9, 7 + l * 18, 19 + i * 18));
            }
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 7 + l * 18, 85 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 7 + i * 18, 143));
        }
    }

    // === GETTERS ===

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    @Override
    public BlockPos getMachinePos() {
        return pos;
    }

    @Override public String getMachineKey() {
        return machineKey;
    }

}

