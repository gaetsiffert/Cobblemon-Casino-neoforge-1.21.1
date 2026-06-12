package net.narrnouille.cobblemoncasino.screen.opening;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record CasinoLedgerOpenData(BlockPos pos, long balance, GameStats slots, GameStats blackjack) {

    public record GameStats(PlayerStats playerStats, List<LeaderboardRow> highestWin,
                            List<LeaderboardRow> totalWon, List<LeaderboardRow> totalLost) {
        public GameStats {
            highestWin = List.copyOf(highestWin);
            totalWon = List.copyOf(totalWon);
            totalLost = List.copyOf(totalLost);
        }
    }

    public record PlayerStats(long highestWin, long totalWon, long totalLost) {}

    public record LeaderboardRow(String playerName, long value) {}

    public static final StreamCodec<RegistryFriendlyByteBuf, CasinoLedgerOpenData> CODEC =
            StreamCodec.ofMember(CasinoLedgerOpenData::write, CasinoLedgerOpenData::read);

    private static void write(CasinoLedgerOpenData data, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeLong(data.balance());
        writeGameStats(data.slots(), buf);
        writeGameStats(data.blackjack(), buf);
    }

    private static CasinoLedgerOpenData read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        long balance = buf.readLong();
        GameStats slots = readGameStats(buf);
        GameStats blackjack = readGameStats(buf);
        return new CasinoLedgerOpenData(pos, balance, slots, blackjack);
    }

    private static void writeGameStats(GameStats stats, RegistryFriendlyByteBuf buf) {
        buf.writeLong(stats.playerStats().highestWin());
        buf.writeLong(stats.playerStats().totalWon());
        buf.writeLong(stats.playerStats().totalLost());
        writeRows(stats.highestWin(), buf);
        writeRows(stats.totalWon(), buf);
        writeRows(stats.totalLost(), buf);
    }

    private static GameStats readGameStats(RegistryFriendlyByteBuf buf) {
        PlayerStats playerStats = new PlayerStats(buf.readLong(), buf.readLong(), buf.readLong());
        List<LeaderboardRow> highestWin = readRows(buf);
        List<LeaderboardRow> totalWon = readRows(buf);
        List<LeaderboardRow> totalLost = readRows(buf);
        return new GameStats(playerStats, highestWin, totalWon, totalLost);
    }

    private static void writeRows(List<LeaderboardRow> rows, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(rows.size());
        for (LeaderboardRow row : rows) {
            buf.writeUtf(row.playerName(), 64);
            buf.writeLong(row.value());
        }
    }

    private static List<LeaderboardRow> readRows(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<LeaderboardRow> rows = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            rows.add(new LeaderboardRow(buf.readUtf(64), buf.readLong()));
        }
        return rows;
    }
}
