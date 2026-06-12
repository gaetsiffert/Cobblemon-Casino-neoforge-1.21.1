package net.andrespr.casinorocket.games.slot;

import com.mojang.authlib.GameProfile;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;
import java.util.function.ToLongFunction;

public class SlotUtils {

    public static Component getRatesText() {
        if (SlotReels.STRIPS == null || SlotReels.STRIPS.length < 3) {
            return Component.translatable("command.casinorocket.slot_rates_not_loaded").withStyle(ChatFormatting.RED);
        }

        SlotSymbol[] reel1 = SlotReels.STRIPS[0];
        SlotSymbol[] reel2 = SlotReels.STRIPS[1];
        SlotSymbol[] reel3 = SlotReels.STRIPS[2];

        MutableComponent out = Component.literal("")
                .append(Component.translatable("command.casinorocket.slots_rates").withStyle(ChatFormatting.UNDERLINE))
                .append("\n")
                .append(Component.translatable("command.casinorocket.per_played_line").withStyle(ChatFormatting.GRAY));

        double totalWin = 0.0;
        List<RateLine> lines = new ArrayList<>();

        double cherry1 = probability(reel1, SlotSymbol.CHERRY) * (1.0 - probability(reel2, SlotSymbol.CHERRY));
        double cherry2 = probability(reel1, SlotSymbol.CHERRY) * probability(reel2, SlotSymbol.CHERRY)
                * (1.0 - probability(reel3, SlotSymbol.CHERRY));
        double cherry3 = probability(reel1, SlotSymbol.CHERRY) * probability(reel2, SlotSymbol.CHERRY)
                * probability(reel3, SlotSymbol.CHERRY);

        lines.add(new RateLine(Component.translatable("command.casinorocket.slot_symbol.cherry_1"), cherry1, "x2"));
        lines.add(new RateLine(Component.translatable("command.casinorocket.slot_symbol.cherry_2"), cherry2, "x3"));
        lines.add(new RateLine(Component.translatable("command.casinorocket.slot_symbol.cherry_3"), cherry3, "x5"));
        totalWin += cherry1 + cherry2 + cherry3;

        for (SlotSymbol symbol : SlotSymbol.values()) {
            if (symbol == SlotSymbol.HAUNTER || symbol == SlotSymbol.CHERRY) continue;

            double chance = probability(reel1, symbol) * probability(reel2, symbol) * probability(reel3, symbol);
            lines.add(new RateLine(Component.translatable("command.casinorocket.slot_symbol.triple",
                    symbol.name()), chance, "x" + symbol.getTripleMultiplier()));
            totalWin += chance;
        }

        out.append(formatPercent(totalWin)).append("\n");

        boolean first = true;
        for (RateLine line : lines) {
            if (!first) out.append(Component.literal(", "));
            first = false;

            out.append(line.label())
                    .append(Component.literal(" -> " + line.multiplier() + ": "))
                    .append(formatPercent(line.chance()));
        }

        out.append("\n")
                .append(Component.translatable("command.casinorocket.modes").withStyle(ChatFormatting.GRAY))
                .append(Component.translatable("command.casinorocket.slot_modes").withStyle(ChatFormatting.WHITE));

        return out;
    }

    public static Component getLeaderboardText(MinecraftServer server, String key) {
        PlayerSlotMachineData data = PlayerSlotMachineData.get(server);

        String k = key.toLowerCase(Locale.ROOT);

        ToLongFunction<UUID> valueFn = switch (k) {
            case "highest_win" -> data::getHighestWin;
            case "total_win" -> data::getTotalWon;
            case "total_lost" -> data::getTotalLost;
            default -> null;
        };

        if (valueFn == null) {
            return Component.translatable("command.casinorocket.invalid_key").withStyle(ChatFormatting.RED);
        }

        List<Map.Entry<UUID, Long>> rows = new ArrayList<>();
        for (UUID id : data.getAllKnownPlayers()) {
            long v = valueFn.applyAsLong(id);

            if (v <= 0) continue;

            rows.add(new AbstractMap.SimpleEntry<>(id, v));
        }

        if (rows.isEmpty()) {
            return Component.translatable("command.casinorocket.no_leaderboard_entries").withStyle(ChatFormatting.GRAY);
        }

        rows.sort(Comparator.<Map.Entry<UUID, Long>>comparingLong(Map.Entry::getValue).reversed());

        int limit = Math.min(10, rows.size());
        List<Map.Entry<UUID, Long>> top = rows.subList(0, limit);

        Component titleLabel = switch (k) {
            case "highest_win" -> Component.translatable("command.casinorocket.slots_highest_win");
            case "total_win" -> Component.translatable("command.casinorocket.slots_total_won");
            case "total_lost" -> Component.translatable("command.casinorocket.slots_total_lost");
            default -> Component.translatable("command.casinorocket.slots_leaderboard");
        };

        ChatFormatting titleColor = switch (k) {
            case "highest_win" -> ChatFormatting.GOLD;
            case "total_win" -> ChatFormatting.GREEN;
            case "total_lost" -> ChatFormatting.RED;
            default -> ChatFormatting.YELLOW;
        };

        MutableComponent out = Component.literal("\n")
                .append(Component.translatable("command.casinorocket.leaderboard_top", titleLabel)
                        .withStyle(titleColor, ChatFormatting.BOLD))
                .append(Component.literal("\n"));

        for (int i = 0; i < top.size(); i++) {
            int rank = i + 1;
            UUID id = top.get(i).getKey();
            long value = top.get(i).getValue();

            String name = resolveName(server, id);

            out.append(Component.literal(rank + ". ").withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(name).withStyle(TextUtils.rankColors(rank)))
                    .append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(TextUtils.formatCompactNoDecimal(value)).withStyle(ChatFormatting.YELLOW));

            if (rank < top.size()) out.append(Component.literal("\n"));
        }

        return out;
    }

    private static String resolveName(MinecraftServer server, UUID id) {
        ServerPlayer p = server.getPlayerList().getPlayer(id);
        if (p != null) return p.getName().getString();

        var cache = server.getProfileCache();
        if (cache != null) {
            Optional<GameProfile> profile = cache.get(id);
            if (profile.isPresent() && profile.get().getName() != null) {
                return profile.get().getName();
            }
        }

        return shortUuid(id);
    }

    private static String shortUuid(UUID id) {
        String s = id.toString().replace("-", "");
        return s.substring(0, 8);
    }

    private static double probability(SlotSymbol[] reel, SlotSymbol symbol) {
        if (reel == null || reel.length == 0) return 0.0;

        int count = 0;
        for (SlotSymbol entry : reel) {
            if (entry == symbol) count++;
        }

        return (double) count / reel.length;
    }

    private static MutableComponent formatPercent(double rate) {
        double percentage = rate * 100.0;
        double rounded = Math.round(percentage * 100.0) / 100.0;
        return Component.literal(String.format(Locale.ROOT, "%.2f%%", rounded))
                .withStyle(TextUtils.percentagesColor(rounded));
    }

    private record RateLine(Component label, double chance, String multiplier) {}

}

