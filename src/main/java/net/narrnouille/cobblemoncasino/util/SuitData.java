package net.narrnouille.cobblemoncasino.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.npc.Villager;

public class SuitData {

    private static final String KEY = "cobblemoncasino.CustomSuit";

    public static void setSuitServer(Villager villager, int suit) {
        CompoundTag nbt = villager.getPersistentData();
        nbt.putInt(KEY, suit);
    }

    public static int getSuit(Villager villager) {
        CompoundTag nbt = villager.getPersistentData();
        return nbt.contains(KEY) ? nbt.getInt(KEY) : 0;
    }

    public static void setSuitClient(Villager villager, int suit) {
        CompoundTag nbt = villager.getPersistentData();
        nbt.putInt(KEY, suit);
    }

}


