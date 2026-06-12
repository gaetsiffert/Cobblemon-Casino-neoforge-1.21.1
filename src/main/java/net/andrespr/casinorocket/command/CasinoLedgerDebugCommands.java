package net.andrespr.casinorocket.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CasinoLedgerDebugCommands {

    private static final int DEBUG_PLAYER_COUNT = 12;

    private CasinoLedgerDebugCommands() {}

    public static LiteralArgumentBuilder<CommandSourceStack> buildSubcommand() {
        return Commands.literal("debug")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("ledger")
                        .then(Commands.literal("fill")
                                .executes(context -> fillLedger(context.getSource())))
                        .then(Commands.literal("clear")
                                .executes(context -> clearLedger(context.getSource())))
                        .then(Commands.literal("clear_player")
                                .then(Commands.argument("player", StringArgumentType.word())
                                        .suggests(CasinoLedgerDebugCommands::suggestPlayers)
                                        .executes(context -> clearPlayer(context.getSource(),
                                                StringArgumentType.getString(context, "player"))))));
    }

    private static int fillLedger(CommandSourceStack source) {
        MinecraftServer server = source.getServer();
        PlayerSlotMachineData slots = PlayerSlotMachineData.get(server);
        PlayerBlackjackData blackjack = PlayerBlackjackData.get(server);

        for (int i = 1; i <= DEBUG_PLAYER_COUNT; i++) {
            UUID id = debugUuid(i);
            cacheDebugProfile(server, id, debugName(i));

            long rankWeight = DEBUG_PLAYER_COUNT + 1L - i;
            slots.setDebugLedgerStats(id,
                    25_000L * rankWeight,
                    180_000L * rankWeight,
                    235_000L * rankWeight);
            blackjack.setDebugLedgerStats(id,
                    30_000L * rankWeight,
                    210_000L * rankWeight,
                    275_000L * rankWeight);
        }

        source.sendSuccess(() -> Component.translatable("command.casinorocket.ledger_debug_filled", DEBUG_PLAYER_COUNT)
                .withStyle(ChatFormatting.GREEN), true);
        return DEBUG_PLAYER_COUNT;
    }

    private static int clearLedger(CommandSourceStack source) {
        MinecraftServer server = source.getServer();
        int slotPlayers = PlayerSlotMachineData.get(server).clearLedgerStats();
        int blackjackPlayers = PlayerBlackjackData.get(server).clearLedgerStats();

        source.sendSuccess(() -> Component.translatable("command.casinorocket.ledger_debug_cleared", slotPlayers, blackjackPlayers)
                .withStyle(ChatFormatting.GREEN), true);
        return slotPlayers + blackjackPlayers;
    }

    private static int clearPlayer(CommandSourceStack source, String playerName) {
        MinecraftServer server = source.getServer();
        UUID playerId = resolvePlayerId(server, playerName);
        if (playerId == null) {
            source.sendFailure(Component.translatable("command.casinorocket.ledger_debug_unknown_player", playerName)
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        boolean removedSlots = PlayerSlotMachineData.get(server).removeLedgerStats(playerId);
        boolean removedBlackjack = PlayerBlackjackData.get(server).removeLedgerStats(playerId);
        if (!removedSlots && !removedBlackjack) {
            source.sendFailure(Component.translatable("command.casinorocket.ledger_debug_no_stats", playerName)
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        source.sendSuccess(() -> Component.translatable("command.casinorocket.ledger_debug_cleared_player", playerName)
                .withStyle(ChatFormatting.GREEN), true);
        return 1;
    }

    private static UUID debugUuid(int index) {
        String seed = "CasinoRocket:DebugLedger:" + index;
        return UUID.nameUUIDFromBytes(seed.getBytes(StandardCharsets.UTF_8));
    }

    private static String debugName(int index) {
        return String.format("HighRoller%02d", index);
    }

    private static void cacheDebugProfile(MinecraftServer server, UUID id, String name) {
        var cache = server.getProfileCache();
        if (cache != null) {
            cache.add(new GameProfile(id, name));
        }
    }

    private static UUID resolvePlayerId(MinecraftServer server, String input) {
        var onlinePlayer = server.getPlayerList().getPlayerByName(input);
        if (onlinePlayer != null) {
            return onlinePlayer.getUUID();
        }

        try {
            return UUID.fromString(input);
        } catch (IllegalArgumentException ignored) {
        }

        var cache = server.getProfileCache();
        if (cache != null) {
            Optional<GameProfile> profile = cache.get(input);
            if (profile.isPresent()) {
                return profile.get().getId();
            }
        }

        return null;
    }

    private static CompletableFuture<Suggestions> suggestPlayers(CommandContext<CommandSourceStack> context,
                                                                 SuggestionsBuilder builder) {
        MinecraftServer server = context.getSource().getServer();

        Set<String> suggestions = registeredLedgerPlayerNames(server);
        if (suggestions.isEmpty()) {
            addOnlinePlayerNames(server, suggestions);
        }

        for (String suggestion : suggestions) {
            builder.suggest(suggestion);
        }

        return builder.buildFuture();
    }

    private static Set<String> registeredLedgerPlayerNames(MinecraftServer server) {
        Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (UUID id : registeredLedgerPlayerIds(server)) {
            resolveKnownName(server, id).ifPresent(names::add);
        }
        return names;
    }

    private static Set<UUID> registeredLedgerPlayerIds(MinecraftServer server) {
        Set<UUID> ids = new HashSet<>();
        ids.addAll(PlayerSlotMachineData.get(server).getAllKnownPlayers());
        ids.addAll(PlayerBlackjackData.get(server).getAllKnownPlayers());
        return ids;
    }

    private static Optional<String> resolveKnownName(MinecraftServer server, UUID id) {
        var onlinePlayer = server.getPlayerList().getPlayer(id);
        if (onlinePlayer != null) {
            return Optional.of(onlinePlayer.getName().getString());
        }

        var cache = server.getProfileCache();
        if (cache != null) {
            return cache.get(id).map(GameProfile::getName).filter(name -> !name.isBlank());
        }

        return Optional.empty();
    }

    private static void addOnlinePlayerNames(MinecraftServer server, Set<String> names) {
        for (var player : server.getPlayerList().getPlayers()) {
            names.add(player.getName().getString());
        }
    }
}
