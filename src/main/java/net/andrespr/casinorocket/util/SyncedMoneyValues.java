package net.andrespr.casinorocket.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SyncedMoneyValues {

    private static final Map<String, Long> CHIP_VALUES = new ConcurrentHashMap<>();

    private SyncedMoneyValues() {}

    public static void applyChipValues(Map<String, Long> values) {
        CHIP_VALUES.clear();
        CHIP_VALUES.putAll(values);
    }

    public static long getChipValue(Item item, long fallback) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null) {
            return fallback;
        }
        return CHIP_VALUES.getOrDefault(id.toString(), fallback);
    }
}
