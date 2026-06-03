package net.andrespr.casinorocket.screen.custom.common;

import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.s2c.sender.MachineBalanceSender;
import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.screen.opening.CommonMachineOpenData;
import net.andrespr.casinorocket.screen.widget.WithdrawSlot;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public class WithdrawScreenHandler extends AbstractContainerMenu implements IMachineBoundHandler {

    private final BlockPos pos;
    private final String machineKey;
    private final SimpleContainer inventory = new SimpleContainer(27);

    public WithdrawScreenHandler(int syncId, Inventory playerInventory, CommonMachineOpenData data) {
        this(syncId, playerInventory, data.pos(), data.machineKey());
    }

    public WithdrawScreenHandler(int syncId, Inventory playerInventory, BlockPos pos, String machineKey) {
        super(ModMenuTypes.WITHDRAW_MENU_TYPE, syncId);
        this.machineKey = machineKey;
        this.pos = pos;

        this.inventory.addListener(this::slotsChanged);

        addChestInventory(inventory);
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        Player player = playerInventory.player;

        if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            long balance = resolveBalance(player);
            loadStacksIntoSlots(MoneyCalculator.calculateChipWithdraw(balance));

            this.inventory.setChanged();
            this.broadcastChanges();

            MachineBalanceSender.send(serverPlayer, machineKey, balance);
        }

    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // ==== HELPER -> INVENTORIES =====

    private void addChestInventory(Container inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new WithdrawSlot(inventory, l + i * 9, 7 + l * 18, 19 + i * 18));
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

    // === HELPER -> BALANCE PER MACHINE ===

    private long resolveBalance(Player player) {
        if (player.getServer() == null) return 0L;
        return switch (machineKey) {
            case "slots" -> PlayerSlotMachineData.get(player.getServer()).getBalance(player.getUUID());
            case "blackjack" -> PlayerBlackjackData.get(player.getServer()).getBalance(player.getUUID());
            default -> 0L;
        };
    }

    // === WITHDRAW INVENTORY ===

    public void loadStacksIntoSlots(List<ItemStack> stacks) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, ItemStack.EMPTY);
        }

        int i = 0;
        for (ItemStack stack : stacks) {
            if (i >= inventory.getContainerSize()) break;
            inventory.setItem(i, stack.copy());
            i++;
        }

        inventory.setChanged();
        broadcastChanges();
    }

    public void clearWithdrawInventory() {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, ItemStack.EMPTY);
        }
        inventory.setChanged();
        broadcastChanges();
    }

    // === GETTERS ===

    @Override
    public BlockPos getMachinePos() {
        return pos;
    }

    @Override
    public String getMachineKey() {
        return machineKey;
    }

}

