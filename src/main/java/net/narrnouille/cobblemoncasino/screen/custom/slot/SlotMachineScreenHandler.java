package net.narrnouille.cobblemoncasino.screen.custom.slot;

import net.narrnouille.cobblemoncasino.block.entity.custom.SlotMachineEntity;
import net.narrnouille.cobblemoncasino.screen.ModMenuTypes;
import net.narrnouille.cobblemoncasino.screen.opening.SlotMachineOpenData;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
import net.narrnouille.cobblemoncasino.util.MenuValidation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SlotMachineScreenHandler extends AbstractContainerMenu implements IMachineBoundHandler {

    private final BlockPos pos;
    private final String machineKey;
    private final long initialBalance;
    private final int initialBetBase;
    private final int initialLinesMode;

    public SlotMachineScreenHandler(int syncId, Inventory inv, SlotMachineOpenData data) {
        super(ModMenuTypes.SLOT_MACHINE_MENU_TYPE, syncId);
        this.pos = data.pos();
        this.machineKey = data.machineKey();
        this.initialBalance = data.balance();
        this.initialBetBase = data.betBase();
        this.initialLinesMode = data.linesMode();
    }

    public SlotMachineScreenHandler(int syncId, Inventory inv, BlockPos pos, String machineKey,
                                    long balance, int betBase, int linesMode) {
        super(ModMenuTypes.SLOT_MACHINE_MENU_TYPE, syncId);
        this.pos = pos;
        this.machineKey = machineKey;
        this.initialBalance = balance;
        this.initialBetBase = betBase;
        this.initialLinesMode = linesMode;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (player.level().isClientSide) return;
        if (!(player instanceof ServerPlayer sp)) return;

        BlockPos machinePos = this.pos;
        MinecraftServer server = sp.getServer();
        if (server == null) return;

        server.execute(() -> {
            if (sp.containerMenu instanceof IMachineBoundHandler bound
                    && bound.getMachinePos().equals(machinePos)) {
                return;
            }

            BlockEntity be = sp.level().getBlockEntity(machinePos);
            if (be instanceof SlotMachineEntity slotBe) {
                slotBe.unlock(sp);
            }
        });
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return MenuValidation.isValidMachine(player, this.pos, this.machineKey);
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

