package net.andrespr.casinorocket.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.andrespr.casinorocket.games.slot.SlotUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public final class SlotMachineCommands {

    public static LiteralArgumentBuilder<CommandSourceStack> buildSubcommand() {
        return Commands.literal("slots")
                .then(Commands.literal("leaderboard")
                        .then(Commands.literal("highest_win")
                                .executes(ctx -> executeLeaderboard(ctx, "highest_win")))
                        .then(Commands.literal("total_win")
                                .executes(ctx -> executeLeaderboard(ctx, "total_win")))
                        .then(Commands.literal("total_lost")
                                .executes(ctx -> executeLeaderboard(ctx, "total_lost")))
                );
    }

    private static int executeLeaderboard(CommandContext<CommandSourceStack> context, String key) {
        ServerPlayer sender = getPlayer(context);
        if (sender == null) return 0;

        Component msg = SlotUtils.getLeaderboardText(context.getSource().getServer(), key);
        sender.displayClientMessage(msg, false);
        return 1;
    }

    private static @Nullable ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) {
        try {
            return context.getSource().getPlayer();
        } catch (Exception e) {
            return null;
        }
    }

}

