package net.narrnouille.cobblemoncasino.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.npc.Villager;

public class IdleYawData {

    private static final String KEY = "cobblemoncasino.IdleYaw";

    public static float get(Villager v) {
        CompoundTag nbt = v.getPersistentData();
        return nbt.contains(KEY) ? nbt.getFloat(KEY) : v.getYRot();
    }

    public static void set(Villager v, float yaw) {
        v.getPersistentData().putFloat(KEY, yaw);
    }

    public static boolean has(Villager v) {
        return v.getPersistentData().contains(KEY);
    }

}

