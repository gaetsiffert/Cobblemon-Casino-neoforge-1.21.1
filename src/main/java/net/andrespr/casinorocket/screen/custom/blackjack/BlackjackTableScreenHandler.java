package net.andrespr.casinorocket.screen.custom.blackjack;

import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.screen.opening.BlackjackTableOpenData;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.andrespr.casinorocket.util.MenuValidation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlackjackTableScreenHandler extends AbstractContainerMenu implements IMachineBoundHandler {

    private final BlockPos pos;
    private final String machineKey;
    private final long initialBalance;
    private final int initialBetIndex;
    private final long[] initialBetValues;

    public BlackjackTableScreenHandler(int syncId, Inventory inv, BlackjackTableOpenData data) {
        super(ModMenuTypes.BLACKJACK_TABLE_MENU_TYPE, syncId);
        this.pos = data.pos();
        this.machineKey = data.machineKey();
        this.initialBalance = data.balance();
        this.initialBetIndex = data.betIndex();
        this.initialBetValues = data.betValues();
    }

    public BlackjackTableScreenHandler(int syncId, Inventory inv, BlockPos pos, String machineKey, long balance, int betIndex, long[] betValues) {
        super(ModMenuTypes.BLACKJACK_TABLE_MENU_TYPE, syncId);
        this.pos = pos;
        this.machineKey = machineKey;
        this.initialBalance = balance;
        this.initialBetIndex = betIndex;
        this.initialBetValues = betValues;
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
            if (be instanceof BlackjackTableEntity table) {
                table.unlock(sp);
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
    public int getInitialBetIndex() { return initialBetIndex; }
    public long[] getInitialBetValues() { return initialBetValues; }

    @Override
    public String getMachineKey() {
        return machineKey;
    }

}

