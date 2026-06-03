package net.andrespr.casinorocket.screen.custom.slot;

import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class SlotMachineMenuScreenHandler extends AbstractContainerMenu implements IMachineBoundHandler {

    private final BlockPos pos;
    private final String machineKey;
    private final long initialBalance;
    private final int initialBetBase;
    private final int initialLinesMode;

    public SlotMachineMenuScreenHandler(int syncId, Inventory inv, SlotMachineOpenData data) {
        super(ModMenuTypes.SLOT_MACHINE_CONFIG_MENU_TYPE, syncId);
        this.pos = data.pos();
        this.machineKey = data.machineKey();
        this.initialBalance = data.balance();
        this.initialBetBase = data.betBase();
        this.initialLinesMode = data.linesMode();
    }

    public SlotMachineMenuScreenHandler(int syncId, Inventory inv, BlockPos pos,
                                        String machineKey, long balance, int betBase, int linesMode) {
        super(ModMenuTypes.SLOT_MACHINE_CONFIG_MENU_TYPE, syncId);
        this.pos = pos;
        this.machineKey = machineKey;
        this.initialBalance = balance;
        this.initialBetBase = betBase;
        this.initialLinesMode = linesMode;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // === GETTERS ===
    public BlockPos getMachinePos() { return pos; }
    public long getInitialBalance() { return initialBalance; }
    public int getInitialBetBase() { return initialBetBase; }
    public int getInitialLinesMode() { return initialLinesMode; }

    @Override
    public String getMachineKey() {
        return machineKey;
    }

}

