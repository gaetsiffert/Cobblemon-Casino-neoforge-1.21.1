package net.andrespr.casinorocket.screen.custom.chip_table;

import net.andrespr.casinorocket.games.chip_table.ChipTableConversionMode;
import net.andrespr.casinorocket.games.chip_table.ChipTableExchange;
import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.screen.opening.ChipTableOpenData;
import net.andrespr.casinorocket.screen.widget.BetSlot;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.IChipBankHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ChipTableScreenHandler extends AbstractContainerMenu {

    private static final int WALLET_SLOT_COUNT = 27;
    private static final int EXCHANGE_INPUT_SLOTS = 9;
    private static final int EXCHANGE_OUTPUT_SLOTS = 18;
    private static final int EXCHANGE_TOTAL_SLOTS = EXCHANGE_INPUT_SLOTS + EXCHANGE_OUTPUT_SLOTS;

    private final BlockPos tablePos;
    private final boolean walletMode;
    private final Container bankInventory;
    private final Container inputInventory;
    private final Container outputInventory;

    public ChipTableScreenHandler(int syncId, Inventory playerInventory, ChipTableOpenData openData) {
        super(ModMenuTypes.CHIP_TABLE_MENU_TYPE, syncId);
        this.tablePos = openData.pos();
        this.walletMode = openData.walletMode();

        if (this.walletMode) {
            this.inputInventory = null;
            this.outputInventory = null;
            if (playerInventory.player.level().isClientSide) {
                this.bankInventory = new SimpleContainer(WALLET_SLOT_COUNT);
            } else {
                this.bankInventory = ((IChipBankHolder) playerInventory.player).casinorocket$getChipBankInventory();
            }
            checkContainerSize(this.bankInventory, WALLET_SLOT_COUNT);
            this.bankInventory.startOpen(playerInventory.player);
            addWalletInventory(this.bankInventory);
        } else {
            this.bankInventory = null;
            this.inputInventory = new SimpleContainer(EXCHANGE_INPUT_SLOTS);
            this.outputInventory = new SimpleContainer(EXCHANGE_OUTPUT_SLOTS);
            this.inputInventory.startOpen(playerInventory.player);
            this.outputInventory.startOpen(playerInventory.player);
            addExchangeInventory();
        }

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public boolean isWalletMode() {
        return walletMode;
    }

    public BlockPos getTablePos() {
        return tablePos;
    }

    public void convert(Player player, ChipTableConversionMode mode) {
        if (this.walletMode || this.inputInventory == null || this.outputInventory == null) return;

        if (!isContainerEmpty(this.outputInventory)) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.chip_table_output_not_empty", false);
            return;
        }

        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 0; i < this.inputInventory.getContainerSize(); i++) {
            inputs.add(this.inputInventory.getItem(i).copy());
        }

        ChipTableExchange.ConversionResult result = ChipTableExchange.convert(inputs, mode);
        if (!result.success()) {
            player.displayClientMessage(result.message(), false);
            return;
        }

        if (!canFitOutput(result.outputs())) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.chip_table_output_full", false);
            return;
        }

        this.inputInventory.clearContent();
        for (int i = 0; i < result.outputs().size(); i++) {
            this.outputInventory.setItem(i, result.outputs().get(i).copy());
        }
        player.displayClientMessage(result.message(), false);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int invSlot) {
        return this.walletMode ? quickMoveWallet(invSlot) : quickMoveExchange(invSlot);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.walletMode) {
            return this.bankInventory.stillValid(player);
        }
        return this.inputInventory.stillValid(player) && this.outputInventory.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (this.walletMode) {
            this.bankInventory.stopOpen(player);
            return;
        }

        this.inputInventory.stopOpen(player);
        this.outputInventory.stopOpen(player);
        if (!player.level().isClientSide) {
            this.clearContainer(player, this.inputInventory);
            this.clearContainer(player, this.outputInventory);
        }
    }

    private ItemStack quickMoveWallet(int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (invSlot < WALLET_SLOT_COUNT) {
                if (!this.moveItemStackTo(originalStack, WALLET_SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(originalStack, 0, WALLET_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return newStack;
    }

    private ItemStack quickMoveExchange(int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            newStack = originalStack.copy();

            if (invSlot < EXCHANGE_TOTAL_SLOTS) {
                if (!this.moveItemStackTo(originalStack, EXCHANGE_TOTAL_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (ChipTableExchange.isAcceptedInput(originalStack)) {
                if (!this.moveItemStackTo(originalStack, 0, EXCHANGE_INPUT_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return newStack;
    }

    private boolean canFitOutput(List<ItemStack> outputs) {
        if (outputs.size() > EXCHANGE_OUTPUT_SLOTS) return false;
        for (ItemStack output : outputs) {
            if (output.getCount() > output.getMaxStackSize()) return false;
        }
        return true;
    }

    private static boolean isContainerEmpty(Container container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) return false;
        }
        return true;
    }

    private void addWalletInventory(Container inventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new BetSlot(inventory, l + i * 9, 7 + l * 18, 15 + i * 18));
            }
        }
    }

    private void addExchangeInventory() {
        for (int i = 0; i < EXCHANGE_INPUT_SLOTS; i++) {
            this.addSlot(new ExchangeInputSlot(this.inputInventory, i, 7 + i * 18, 17));
        }
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 9; col++) {
                int index = col + row * 9;
                this.addSlot(new ExchangeOutputSlot(this.outputInventory, index, 7 + col * 18, 47 + row * 18));
            }
        }
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 7 + l * 18, (this.walletMode ? 90 : 113) + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 7 + i * 18, this.walletMode ? 148 : 171));
        }
    }

    private static class ExchangeInputSlot extends Slot {
        public ExchangeInputSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return ChipTableExchange.isAcceptedInput(stack);
        }
    }

    private static class ExchangeOutputSlot extends Slot {
        public ExchangeOutputSlot(Container inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

}
