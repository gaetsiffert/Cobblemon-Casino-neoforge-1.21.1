package net.andrespr.casinorocket.data;

import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import java.util.*;

public class PlayerSlotMachineData extends SavedData {

    private static final String STORAGE_KEY = "casinorocket_slot_machine_data";

    private final Map<UUID, Integer> betIndex = new HashMap<>();
    private final Map<UUID, Integer> linesMode = new HashMap<>();

    private final Map<UUID, Long> totalDeposited = new HashMap<>();
    private final Map<UUID, Long> totalWon = new HashMap<>();
    private final Map<UUID, Long> highestWin = new HashMap<>();
    private final Map<UUID, Long> lastWin = new HashMap<>();
    private final Map<UUID, Long> totalSpent = new HashMap<>();

    public static PlayerSlotMachineData get(MinecraftServer server) {
        DimensionDataStorage manager = Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage();
        SavedData.Factory<PlayerSlotMachineData> type = new SavedData.Factory<>(
                PlayerSlotMachineData::new, PlayerSlotMachineData::readNbt, null);
        return manager.computeIfAbsent(type, STORAGE_KEY);
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        CompoundTag betIndexTag = new CompoundTag();
        betIndex.forEach((uuid, val) -> betIndexTag.putInt(uuid.toString(), val));
        nbt.put("betIndex", betIndexTag);

        CompoundTag linesTag = new CompoundTag();
        linesMode.forEach((uuid, val) -> linesTag.putInt(uuid.toString(), val));
        nbt.put("linesMode", linesTag);

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

    private static PlayerSlotMachineData readNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        PlayerSlotMachineData data = new PlayerSlotMachineData();

        if (nbt.contains("betIndex", Tag.TAG_COMPOUND)) {
            CompoundTag bi = nbt.getCompound("betIndex");
            bi.getAllKeys().forEach(key -> {
                try { data.betIndex.put(UUID.fromString(key), bi.getInt(key)); } catch (Exception ignored) {}
            });
        }

        if (nbt.contains("linesMode", Tag.TAG_COMPOUND)) {
            CompoundTag lm = nbt.getCompound("linesMode");
            lm.getAllKeys().forEach(key -> {
                try { data.linesMode.put(UUID.fromString(key), lm.getInt(key)); } catch (Exception ignored) {}
            });
        }

        if (nbt.contains("totalDeposited", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("totalDeposited");
            t.getAllKeys().forEach(k -> { try { data.totalDeposited.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("totalWon", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("totalWon");
            t.getAllKeys().forEach(k -> { try { data.totalWon.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("highestWin", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("highestWin");
            t.getAllKeys().forEach(k -> { try { data.highestWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("lastWin", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("lastWin");
            t.getAllKeys().forEach(k -> { try { data.lastWin.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        if (nbt.contains("totalSpent", Tag.TAG_COMPOUND)) {
            CompoundTag t = nbt.getCompound("totalSpent");
            t.getAllKeys().forEach(k -> { try { data.totalSpent.put(UUID.fromString(k), t.getLong(k)); } catch (Exception ignored) {}});
        }

        return data;
    }

    // === GETTERS ===
    public int getBetBase(UUID id) {
        List<Integer> bets = SlotMachineConstants.betValues();
        if (bets.isEmpty()) return SlotMachineConstants.defaultBetBase();

        int idx = betIndex.getOrDefault(id, 0);
        idx = Math.max(0, Math.min(idx, bets.size() - 1));

        return bets.get(idx);
    }

    public int getBetIndex(UUID id) {
        int idx = betIndex.getOrDefault(id, 0);
        int max = Math.max(0, SlotMachineConstants.betValues().size() - 1);
        return Math.max(0, Math.min(idx, max));
    }

    public int getLinesMode(UUID id) {
        return linesMode.getOrDefault(id, SlotMachineConstants.defaultLinesMode());
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
        return getTotalWon(id) - getTotalSpent(id);
    }

    public Set<UUID> getAllKnownPlayers() {
        Set<UUID> s = new HashSet<>();
        s.addAll(totalDeposited.keySet());
        s.addAll(totalWon.keySet());
        s.addAll(highestWin.keySet());
        s.addAll(lastWin.keySet());
        s.addAll(totalSpent.keySet());
        s.addAll(betIndex.keySet());
        s.addAll(linesMode.keySet());
        return s;
    }

    // === SETTERS ===
    public void setBetBase(UUID id, int baseValue) {
        List<Integer> bets = SlotMachineConstants.betValues();
        int idx = bets.indexOf(baseValue);
        if (idx < 0) idx = 0;

        betIndex.put(id, idx);
        setDirty();
    }

    public void setBetIndex(UUID id, int index) {
        int max = Math.max(0, SlotMachineConstants.betValues().size() - 1);
        int idx = Math.max(0, Math.min(index, max));
        betIndex.put(id, idx);
        setDirty();
    }

    public void setLinesMode(UUID id, int mode) {
        linesMode.put(id, mode);
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

}

