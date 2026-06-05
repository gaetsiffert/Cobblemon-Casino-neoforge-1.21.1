package net.andrespr.casinorocket.games.records;

import com.mojang.authlib.GameProfile;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.data.PlayerCasinoBalanceData;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.screen.opening.CasinoLedgerOpenData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.ToLongFunction;

public final class CasinoLedgerService {

    private CasinoLedgerService() {}

    public static CasinoLedgerOpenData createSnapshot(MinecraftServer server, ServerPlayer player) {
        UUID playerId = player.getUUID();
        PlayerSlotMachineData slots = PlayerSlotMachineData.get(server);
        PlayerBlackjackData blackjack = PlayerBlackjackData.get(server);
        long balance = PlayerCasinoBalanceData.get(server).getBalance(playerId);

        return new CasinoLedgerOpenData(balance,
                createSlotStats(server, slots, playerId),
                createBlackjackStats(server, blackjack, playerId));
    }

    private static CasinoLedgerOpenData.GameStats createSlotStats(MinecraftServer server, PlayerSlotMachineData data, UUID playerId) {
        return new CasinoLedgerOpenData.GameStats(
                new CasinoLedgerOpenData.PlayerStats(data.getHighestWin(playerId), data.getTotalWon(playerId), data.getTotalLost(playerId)),
                topRows(server, data.getAllKnownPlayers(), data::getHighestWin, false),
                topRows(server, data.getAllKnownPlayers(), data::getTotalWon, false),
                topRows(server, data.getAllKnownPlayers(), data::getTotalLost, true)
        );
    }

    private static CasinoLedgerOpenData.GameStats createBlackjackStats(MinecraftServer server, PlayerBlackjackData data, UUID playerId) {
        return new CasinoLedgerOpenData.GameStats(
                new CasinoLedgerOpenData.PlayerStats(data.getHighestWin(playerId), data.getTotalWon(playerId), data.getTotalLost(playerId)),
                topRows(server, data.getAllKnownPlayers(), data::getHighestWin, false),
                topRows(server, data.getAllKnownPlayers(), data::getTotalWon, false),
                topRows(server, data.getAllKnownPlayers(), data::getTotalLost, true)
        );
    }

    private static List<CasinoLedgerOpenData.LeaderboardRow> topRows(MinecraftServer server, Set<UUID> players,
                                                                     ToLongFunction<UUID> valueFn, boolean lossBoard) {
        List<Map.Entry<UUID, Long>> rows = new ArrayList<>();
        for (UUID id : players) {
            long value = valueFn.applyAsLong(id);
            if (lossBoard ? value < 0 : value > 0) {
                rows.add(new AbstractMap.SimpleEntry<>(id, value));
            }
        }

        Comparator<Map.Entry<UUID, Long>> comparator = Comparator.comparingLong(Map.Entry::getValue);
        if (!lossBoard) comparator = comparator.reversed();
        rows.sort(comparator);

        return rows.stream()
                .limit(10)
                .map(entry -> new CasinoLedgerOpenData.LeaderboardRow(resolveName(server, entry.getKey()), entry.getValue()))
                .toList();
    }

    private static String resolveName(MinecraftServer server, UUID id) {
        ServerPlayer player = server.getPlayerList().getPlayer(id);
        if (player != null) return player.getName().getString();

        Optional<GameProfile> profile = server.getProfileCache() == null ? Optional.empty() : server.getProfileCache().get(id);
        return profile.map(GameProfile::getName).filter(name -> !name.isBlank()).orElse(shortUuid(id));
    }

    private static String shortUuid(UUID id) {
        return id.toString().replace("-", "").substring(0, 8).toLowerCase(Locale.ROOT);
    }
}
