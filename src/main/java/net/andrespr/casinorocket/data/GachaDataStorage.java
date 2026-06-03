package net.andrespr.casinorocket.data;

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

public class GachaDataStorage extends SavedData {

    public Map<UUID, Map<String, Integer>> pityTracker = new HashMap<>();
    public Map<UUID, GachaStats> playerStats = new HashMap<>();

    private static final String STORAGE_KEY = "casinorocket_gacha_data";

    // === MAIN LOADER ===
    public static GachaDataStorage get(MinecraftServer server) {
        DimensionDataStorage manager = Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage();

        SavedData.Factory<GachaDataStorage> type = new SavedData.Factory<>(
                GachaDataStorage::new,
                GachaDataStorage::readNbt,
                null
        );

        return manager.computeIfAbsent(type, STORAGE_KEY);
    }

    // === SAVE ===
    @Override
    public CompoundTag save(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registryLookup) {
        CompoundTag pityTag = new CompoundTag();
        for (var entry : pityTracker.entrySet()) {
            UUID uuid = entry.getKey();
            CompoundTag coinMap = new CompoundTag();
            for (var coinEntry : entry.getValue().entrySet()) {
                coinMap.putInt(coinEntry.getKey(), coinEntry.getValue());
            }
            pityTag.put(uuid.toString(), coinMap);
        }
        nbt.put("PityTracker", pityTag);

        CompoundTag statsTag = new CompoundTag();
        for (var entry : playerStats.entrySet()) {
            statsTag.put(entry.getKey().toString(), entry.getValue().toNbt());
        }
        nbt.put("PlayerStats", statsTag);

        return nbt;
    }

    // === LOAD ===
    private static GachaDataStorage readNbt(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider registryLookup) {
        GachaDataStorage data = new GachaDataStorage();

        if (nbt.contains("PityTracker", Tag.TAG_COMPOUND)) {
            CompoundTag pityTag = nbt.getCompound("PityTracker");
            for (String uuidStr : pityTag.getAllKeys()) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    CompoundTag coinTag = pityTag.getCompound(uuidStr);
                    Map<String, Integer> coinMap = new HashMap<>();
                    for (String coin : coinTag.getAllKeys()) {
                        coinMap.put(coin, coinTag.getInt(coin));
                    }
                    data.pityTracker.put(uuid, coinMap);
                } catch (Exception ignored) {}
            }
        }

        if (nbt.contains("PlayerStats", Tag.TAG_COMPOUND)) {
            CompoundTag statsTag = nbt.getCompound("PlayerStats");
            for (String uuidStr : statsTag.getAllKeys()) {
                try {
                    UUID uuid = UUID.fromString(uuidStr);
                    data.playerStats.put(uuid, GachaStats.fromNbt(statsTag.getCompound(uuidStr)));
                } catch (Exception ignored) {}
            }
        }

        return data;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

}

