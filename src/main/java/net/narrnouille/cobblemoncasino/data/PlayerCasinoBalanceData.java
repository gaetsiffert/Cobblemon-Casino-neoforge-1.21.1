package net.narrnouille.cobblemoncasino.data;

import net.narrnouille.cobblemoncasino.util.MoneyCalculator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PlayerCasinoBalanceData extends SavedData {

    private static final String STORAGE_KEY = "cobblemoncasino_player_casino_balance_data";

    private final Map<UUID, Long> balances = new HashMap<>();

    public static PlayerCasinoBalanceData get(MinecraftServer server) {
        DimensionDataStorage manager = Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage();
        SavedData.Factory<PlayerCasinoBalanceData> type = new SavedData.Factory<>(
                PlayerCasinoBalanceData::new, PlayerCasinoBalanceData::readNbt, null);
        return manager.computeIfAbsent(type, STORAGE_KEY);
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        CompoundTag balTag = new CompoundTag();
        balances.forEach((uuid, val) -> balTag.putLong(uuid.toString(), val));
        nbt.put("balances", balTag);

        return nbt;
    }

    private static PlayerCasinoBalanceData readNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        PlayerCasinoBalanceData data = new PlayerCasinoBalanceData();

        if (nbt.contains("balances", Tag.TAG_COMPOUND)) {
            CompoundTag bal = nbt.getCompound("balances");
            bal.getAllKeys().forEach(key -> {
                try { data.balances.put(UUID.fromString(key), bal.getLong(key)); } catch (Exception ignored) {}
            });
        }

        return data;
    }

    public long getBalance(UUID id) {
        return balances.getOrDefault(id, 0L);
    }

    public void setBalance(UUID id, long value) {
        balances.put(id, Math.max(0L, value));
        setDirty();
    }

    public void addBalance(UUID id, long amount) {
        long current = balances.getOrDefault(id, 0L);
        long next = amount >= 0
                ? MoneyCalculator.safeAdd(current, amount, Long.MAX_VALUE)
                : subtractOrZero(current, amount);
        balances.put(id, next);
        setDirty();
    }

    private static long subtractOrZero(long current, long negativeAmount) {
        if (negativeAmount == Long.MIN_VALUE) return 0L;
        long decrement = -negativeAmount;
        return current <= decrement ? 0L : current - decrement;
    }
}
