package net.andrespr.casinorocket.data;

import net.andrespr.casinorocket.games.blackjack.BlackjackRules;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class PlayerBlackjackData extends SavedData {

    private static final String STORAGE_KEY = "casinorocket_blackjack_data";

    // === GAME DATA ===
    private final Map<UUID, Integer> betIndex = new HashMap<>();

    // === LEADERBOARD STATS ===
    private final Map<UUID, Long> totalDeposited = new HashMap<>();
    private final Map<UUID, Long> totalWon = new HashMap<>();
    private final Map<UUID, Long> highestWin = new HashMap<>();
    private final Map<UUID, Long> lastWin = new HashMap<>();
    private final Map<UUID, Long> totalSpent = new HashMap<>();

    public static PlayerBlackjackData get(MinecraftServer server) {
        DimensionDataStorage manager = Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage();
        SavedData.Factory<PlayerBlackjackData> type = new SavedData.Factory<>(
                PlayerBlackjackData::new, PlayerBlackjackData::readNbt, null
        );
        return manager.computeIfAbsent(type, STORAGE_KEY);
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        CompoundTag betTag = new CompoundTag();
        betIndex.forEach((uuid, val) -> betTag.putInt(uuid.toString(), val));
        nbt.put("betIndex", betTag);

        CompoundTag depTag = new CompoundTag();
        totalDeposited.forEach((uuid, val) -> depTag.putLong(uuid.toString(), val));
        nbt.put("totalDeposited", depTag);

        CompoundTag wonTag = new CompoundTag();
        totalWon.forEach((uuid, val) -> wonTag.putLong(uuid.toString(), val));
        nbt.put("totalWon", wonTag);

        CompoundTag highTag = new CompoundTag();
        highestWin.forEach((uuid, val) -> highTag.putLong(uuid.toString(), val));
        nbt.put("highestWin", highTag);

        CompoundTag lastTag = new CompoundTag();
        lastWin.forEach((uuid, val) -> lastTag.putLong(uuid.toString(), val));
        nbt.put("lastWin", lastTag);

        CompoundTag spentTag = new CompoundTag();
        totalSpent.forEach((uuid, val) -> spentTag.putLong(uuid.toString(), val));
        nbt.put("totalSpent", spentTag);

        return nbt;
    }

    private static PlayerBlackjackData readNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        PlayerBlackjackData data = new PlayerBlackjackData();

        if (nbt.contains("betIndex", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("betIndex");
            t.getAllKeys().forEach(k -> {
                try { data.betIndex.put(UUID.fromString(k), t.getInt(k)); } catch (Exception ignored) {}
            });
        }

        if (nbt.contains("totalDeposited", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("totalDeposited");
            t.getAllKeys().forEach(k -> { try { data.totalDeposited.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("totalWon", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("totalWon");
            t.getAllKeys().forEach(k -> { try { data.totalWon.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("highestWin", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("highestWin");
            t.getAllKeys().forEach(k -> { try { data.highestWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("lastWin", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("lastWin");
            t.getAllKeys().forEach(k -> { try { data.lastWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        if (nbt.contains("totalSpent", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("totalSpent");
            t.getAllKeys().forEach(k -> { try { data.totalSpent.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {} });
        }

        return data;
    }

    // === GETTERS ===
    public int getBetIndex(UUID id) {
        return BlackjackRules.clampBetIndex(betIndex.getOrDefault(id, BlackjackRules.DEFAULT_BET_INDEX));
    }

    public long getBetAmount(UUID id) {
        return BlackjackRules.betAmount(getBetIndex(id));
    }

    public long getTotalDeposited(UUID id) {
        return totalDeposited.getOrDefault(id, 0L);
    }

    public long getTotalWon(UUID id) {
        return totalWon.getOrDefault(id, 0L);
    }

    public long getHighestWin(UUID id) {
        return highestWin.getOrDefault(id, 0L);
    }

    public long getLastWin(UUID id) {
        return lastWin.getOrDefault(id, 0L);
    }

    public long getTotalSpent(UUID id) {
        return totalSpent.getOrDefault(id, 0L);
    }

    public long getTotalLost(UUID id) {
        long spent = getTotalSpent(id);
        long won = getTotalWon(id);
        return spent > won ? spent - won : 0L;
    }

    public Set<UUID> getAllKnownPlayers() {
        Set<UUID> s = new HashSet<>();
        s.addAll(betIndex.keySet());
        s.addAll(totalDeposited.keySet());
        s.addAll(totalWon.keySet());
        s.addAll(highestWin.keySet());
        s.addAll(lastWin.keySet());
        s.addAll(totalSpent.keySet());
        return s;
    }

    // === SETTERS ===
    public void setBetIndex(UUID id, int index) {
        int max = BlackjackRules.maxBetIndex();
        if (index < 0 || index > max) return;

        betIndex.put(id, index);
        setDirty();
    }

    public void setLastWin(UUID id, long amount) {
        lastWin.put(id, Math.max(0L, amount));
        setDirty();
    }

    public void setDebugLedgerStats(UUID id, long highestWinAmount, long totalWonAmount, long totalSpentAmount) {
        highestWin.put(id, Math.max(0L, highestWinAmount));
        lastWin.put(id, Math.max(0L, highestWinAmount));
        totalWon.put(id, Math.max(0L, totalWonAmount));
        totalSpent.put(id, Math.max(0L, totalSpentAmount));
        totalDeposited.put(id, Math.max(0L, totalSpentAmount));
        setDirty();
    }

    public boolean removeLedgerStats(UUID id) {
        boolean removed = totalDeposited.remove(id) != null;
        removed |= totalWon.remove(id) != null;
        removed |= highestWin.remove(id) != null;
        removed |= lastWin.remove(id) != null;
        removed |= totalSpent.remove(id) != null;

        if (removed) {
            setDirty();
        }

        return removed;
    }

    public int clearLedgerStats() {
        Set<UUID> players = new HashSet<>();
        players.addAll(totalDeposited.keySet());
        players.addAll(totalWon.keySet());
        players.addAll(highestWin.keySet());
        players.addAll(lastWin.keySet());
        players.addAll(totalSpent.keySet());

        totalDeposited.clear();
        totalWon.clear();
        highestWin.clear();
        lastWin.clear();
        totalSpent.clear();

        if (!players.isEmpty()) {
            setDirty();
        }

        return players.size();
    }

    // === MUTATORS ===
    public void addTotalDeposited(UUID id, long amount) {
        if (amount <= 0) return;
        totalDeposited.put(id, MoneyCalculator.safeAdd(totalDeposited.getOrDefault(id, 0L), amount, Long.MAX_VALUE));
        setDirty();
    }

    public void addTotalWon(UUID id, long amount) {
        if (amount <= 0) return;
        totalWon.put(id, MoneyCalculator.safeAdd(totalWon.getOrDefault(id, 0L), amount, Long.MAX_VALUE));
        setDirty();
    }

    public void updateHighestWin(UUID id, long win) {
        if (win <= 0) return;
        long prev = highestWin.getOrDefault(id, 0L);
        if (win > prev) {
            highestWin.put(id, win);
            setDirty();
        }
    }

    public void addTotalSpent(UUID id, long amount) {
        if (amount <= 0) return;
        totalSpent.put(id, MoneyCalculator.safeAdd(totalSpent.getOrDefault(id, 0L), amount, Long.MAX_VALUE));
        setDirty();
    }

    public void incrementBetIndex(UUID id) {
        int idx = getBetIndex(id);
        if (idx < BlackjackRules.maxBetIndex()) {
            betIndex.put(id, idx + 1);
            setDirty();
        }
    }

    public void decrementBetIndex(UUID id) {
        int idx = getBetIndex(id);
        if (idx > 0) {
            betIndex.put(id, idx - 1);
            setDirty();
        }
    }

}

