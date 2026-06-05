package net.andrespr.casinorocket.util;

import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.block.entity.custom.ChipTableEntity;
import net.andrespr.casinorocket.block.entity.custom.SlotMachineEntity;
import net.andrespr.casinorocket.block.custom.CasinoScoreboardBlock;
import net.andrespr.casinorocket.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class MenuValidation {

    private static final double MAX_INTERACTION_DISTANCE_SQR = 64.0D;

    private MenuValidation() {}

    public static boolean isValidMachine(Player player, BlockPos pos, String machineKey) {
        if (player == null || pos == null || machineKey == null) return false;
        if (!isNear(player, pos)) return false;
        if (!player.level().hasChunkAt(pos)) return false;

        BlockEntity blockEntity = player.level().getBlockEntity(pos);
        return switch (machineKey) {
            case "slots" -> blockEntity instanceof SlotMachineEntity;
            case "blackjack" -> blockEntity instanceof BlackjackTableEntity;
            default -> false;
        };
    }

    public static boolean isValidChipTable(Player player, BlockPos pos) {
        if (player == null || pos == null) return false;
        if (!isNear(player, pos)) return false;
        if (!player.level().hasChunkAt(pos)) return false;
        return player.level().getBlockEntity(pos) instanceof ChipTableEntity;
    }

    public static boolean isValidWallet(Player player, InteractionHand hand) {
        if (player == null || hand == null) return false;
        ItemStack stack = player.getItemInHand(hand);
        return stack.is(ModItems.WALLET);
    }

    public static boolean isValidScoreboard(Player player, BlockPos pos) {
        if (player == null || pos == null) return false;
        if (!isNear(player, pos)) return false;
        if (!player.level().hasChunkAt(pos)) return false;
        return player.level().getBlockState(pos).getBlock() instanceof CasinoScoreboardBlock;
    }

    public static boolean isNear(Player player, BlockPos pos) {
        return player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D)
                <= MAX_INTERACTION_DISTANCE_SQR;
    }
}
