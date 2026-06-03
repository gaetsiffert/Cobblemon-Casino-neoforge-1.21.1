package net.andrespr.casinorocket.games.blackjack;

import com.mojang.authlib.GameProfile;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.*;
import java.util.function.ToLongFunction;

public final class BlackjackUtils {

    private BlackjackUtils() {}

    public static Component getLeaderboardText(MinecraftServer server, String key) {
        PlayerBlackjackData data = PlayerBlackjackData.get(server);

        String k = key.toLowerCase(Locale.ROOT);

        ToLongFunction<UUID> valueFn = switch (k) {
            case "highest_win" -> data::getHighestWin;
            case "total_win" -> data::getTotalWon;
            case "total_lost" -> data::getTotalLost;
            default -> null;
        };

        if (valueFn == null) {
            return Component.literal("Invalid key.").withStyle(ChatFormatting.RED);
        }

        List<Map.Entry<UUID, Long>> rows = new ArrayList<>();
        for (UUID id : data.getAllKnownPlayers()) {
            long v = valueFn.applyAsLong(id);

            if (k.equals("total_lost")) {
                if (v >= 0) continue;
            } else {
                if (v <= 0) continue;
            }

            rows.add(new AbstractMap.SimpleEntry<>(id, v));
        }

        if (rows.isEmpty()) {
            return Component.literal("No leaderboard entries yet.").withStyle(ChatFormatting.GRAY);
        }

        Comparator<Map.Entry<UUID, Long>> cmp = Comparator.comparingLong(Map.Entry::getValue);
        if (!k.equals("total_lost")) cmp = cmp.reversed();
        rows.sort(cmp);

        int limit = Math.min(10, rows.size());
        List<Map.Entry<UUID, Long>> top = rows.subList(0, limit);

        String titleLabel = switch (k) {
            case "highest_win" -> "Blackjack - Highest Win";
            case "total_win" -> "Blackjack - Total Won";
            case "total_lost" -> "Blackjack - Total Lost";
            default -> "Blackjack Leaderboard";
        };

        ChatFormatting titleColor = switch (k) {
            case "highest_win" -> ChatFormatting.GOLD;
            case "total_win" -> ChatFormatting.GREEN;
            case "total_lost" -> ChatFormatting.RED;
            default -> ChatFormatting.YELLOW;
        };

        MutableComponent out = Component.literal("\n")
                .append(Component.literal("Top 10 - " + titleLabel).withStyle(titleColor, ChatFormatting.BOLD))
                .append(Component.literal("\n"));

        for (int i = 0; i < top.size(); i++) {
            int rank = i + 1;
            UUID id = top.get(i).getKey();
            long value = top.get(i).getValue();

            String name = resolveName(server, id);

            out.append(Component.literal(rank + ". ").withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(name).withStyle(TextUtils.rankColors(rank)))
                    .append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(formatSignedMoney(value)).withStyle(ChatFormatting.YELLOW));

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

    private static String formatSignedMoney(long value) {
        if (value < 0) return "-" + TextUtils.formatCompactNoDecimal(-value);
        return TextUtils.formatCompactNoDecimal(value);
    }

}

