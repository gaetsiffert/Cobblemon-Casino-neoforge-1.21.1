package net.andrespr.casinorocket.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.npc.Villager;

public class LookPlayerData {

    private static final String KEY = "casinorocket.LookPlayer";

    public static int getLookPlayer(Villager villager) {
        CompoundTag nbt = villager.getPersistentData();
        return nbt.contains(KEY) ? nbt.getInt(KEY) : 0;
    }

    public static void setLookPlayer(Villager villager, int value) {
        CompoundTag nbt = villager.getPersistentData();
        nbt.putInt(KEY, value);
    }

}

