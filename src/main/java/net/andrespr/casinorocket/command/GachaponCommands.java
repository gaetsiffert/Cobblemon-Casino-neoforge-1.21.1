package net.andrespr.casinorocket.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.andrespr.casinorocket.games.gachapon.GachaMachinesUtils;
import net.andrespr.casinorocket.games.gachapon.GachaponUtils;
import net.andrespr.casinorocket.games.gachapon.PlushiesGachaponUtils;
import net.andrespr.casinorocket.games.gachapon.PokemonGachaponUtils;
import net.andrespr.casinorocket.util.CommandUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class GachaponCommands {

    public static LiteralArgumentBuilder<CommandSourceStack> buildSubcommand() {
        return Commands.literal("gachapon")
                .then(Commands.literal("rates")
                        .then(Commands.literal("item")
                                .then(Commands.argument("pool", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            GachaponUtils.getPools().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(GachaponCommands::executeRatesForItem)
                                )
                        )
                        .then(Commands.literal("pokemon")
                                .then(Commands.argument("pool", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            PokemonGachaponUtils.getPools().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(GachaponCommands::executeRatesForPokemon)
                                )
                        )
                        .then(Commands.literal("plushies")
                                .executes(GachaponCommands::executeRatesForPlushies)
                        )
                        .then(Commands.literal("machine")
                                .then(Commands.literal("base")
                                        .then(Commands.argument("coin", StringArgumentType.string())
                                                .suggests((context, builder) -> {
                                                    GachaMachinesUtils.getCoinKeys().forEach(builder::suggest);
                                                    return builder.buildFuture();
                                                })
                                                .executes(ctx -> executeRatesForMachine(ctx, false))
                                        )
                                )
                                .then(Commands.literal("pity")
                                        .then(Commands.argument("player", StringArgumentType.string())
                                                .suggests(CommandUtils::suggestPlayerNames)
                                                .then(Commands.argument("coin", StringArgumentType.string())
                                                        .suggests((context, builder) -> {
                                                            GachaMachinesUtils.getCoinKeys().forEach(builder::suggest);
                                                            return builder.buildFuture();
                                                        })
                                                        .executes(ctx -> executeRatesForMachine(ctx, true))
                                                )
                                        )
                                )
                        )
                ).then(Commands.literal("stats")
                        .then(Commands.argument("player", StringArgumentType.string())
                                .suggests(CommandUtils::suggestPlayerNames)
                                .executes(GachaponCommands::executeForMachineStats)
                        )
                ).then(Commands.literal("leaderboard")
                        .then(Commands.literal("rarity")
                                .then(Commands.argument("rarity", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            GachaMachinesUtils.getRarityKeys().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> executeForLeaderboard(ctx, "rarity"))
                                )
                        )
                        .then(Commands.literal("coins")
                                .then(Commands.argument("coin", StringArgumentType.string())
                                        .suggests((context, builder) -> {
                                            GachaMachinesUtils.getCoinKeys().forEach(builder::suggest);
                                            return builder.buildFuture();
                                        })
                                        .executes(ctx -> executeForLeaderboard(ctx, "coins"))
                                )
                        )
                ).then(Commands.literal("cleandata")
                        .requires(source -> source.hasPermission(2))
                        .executes(GachaponCommands::executeCleanData)
                        .then(Commands.literal("confirm")
                                .executes(GachaponCommands::confirmCleanData)
                        )
                );
    }

    private static int executeRatesForItem(CommandContext<CommandSourceStack> context) {
        String poolKey = StringArgumentType.getString(context, "pool");
        ServerPlayer player = getPlayer(context);
        if (player == null) return 0;
        player.displayClientMessage(GachaponUtils.getPoolPercentages(poolKey), false);
        return 1;
    }

    private static int executeRatesForPokemon(CommandContext<CommandSourceStack> context) {
        String poolKey = StringArgumentType.getString(context, "pool");
        ServerPlayer player = getPlayer(context);
        if (player == null) return 0;
        player.displayClientMessage(PokemonGachaponUtils.getPoolPercentages(poolKey), false);
        return 1;
    }

    private static int executeRatesForMachine(CommandContext<CommandSourceStack> context, boolean includePity) {
        MinecraftServer server = context.getSource().getServer();
        ServerPlayer sender = getPlayer(context);
        if (sender == null) return 0;

        String coinKey;
        ServerPlayer target = null;
        if (includePity) {
            String name = StringArgumentType.getString(context, "player");
            coinKey = StringArgumentType.getString(context, "coin");
            target = server.getPlayerList().getPlayerByName(name);

            if (target == null) {
                sender.displayClientMessage(Component.literal("Player '" + name + "' is not online.").withStyle(ChatFormatting.RED), false);
                return 0;
            }

        } else {
            coinKey = StringArgumentType.getString(context, "coin");
        }

        Component msg = GachaMachinesUtils.getMachineRatesText(target, coinKey, includePity);
        sender.displayClientMessage(msg, false);
        return 1;
    }

    private static int executeForMachineStats(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "player");
        MinecraftServer server = context.getSource().getServer();
        ServerPlayer sender = getPlayer(context);
        if (sender == null) return 0;

        ServerPlayer target = server.getPlayerList().getPlayerByName(name);

        Component statsText;
        if (target != null) {
            statsText = GachaMachinesUtils.getPlayerStatsText(target, server);
        } else {
            statsText = GachaMachinesUtils.getPlayerStatsText(name, server);
        }

        sender.displayClientMessage(statsText, false);
        return 1;
    }

    private static int executeForLeaderboard(CommandContext<CommandSourceStack> context, String category) {
        String key = StringArgumentType.getString(context, category.equals("rarity") ? "rarity" : "coin");
        ServerPlayer sender = getPlayer(context);
        if (sender == null) return 0;
        sender.displayClientMessage(GachaMachinesUtils.getLeaderboardText(context.getSource().getServer(), category, key), false);
        return 1;
    }

    private static int executeCleanData(CommandContext<CommandSourceStack> context) {
        ServerPlayer sender = getPlayer(context);
        if (sender == null) return 0;
        Component response = GachaMachinesUtils.clearAllGachaData(Objects.requireNonNull(sender.getServer()), false, sender);
        sender.displayClientMessage(response, false);
        return 1;
    }

    private static int confirmCleanData(CommandContext<CommandSourceStack> context) {
        ServerPlayer sender = getPlayer(context);
        if (sender == null) return 0;
        Component response = GachaMachinesUtils.clearAllGachaData(Objects.requireNonNull(sender.getServer()), true, sender);
        sender.displayClientMessage(response, false);
        return 1;
    }

    private static int executeRatesForPlushies(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = getPlayer(context);
        if (player == null) return 0;
        player.displayClientMessage(PlushiesGachaponUtils.getRates(), false);
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

