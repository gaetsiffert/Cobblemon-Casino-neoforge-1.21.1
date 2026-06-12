package net.narrnouille.cobblemoncasino.villager;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public final class VillagerNbtFactory {

    private VillagerNbtFactory() {}

    public static CompoundTag createBaseVillagerNbt(String displayName, BlockPos jobPos, String profession) {
        return createBaseVillagerNbt(displayName, jobPos, profession, 0);
    }

    public static CompoundTag createBaseVillagerNbt(String displayName, BlockPos jobPos, String profession, int suitId) {

        CompoundTag root = new CompoundTag();

        // Villager ID
        root.putString("id", "minecraft:villager");

        // Custom Name
        String nameJson = "{\"text\":\"" + escapeJson(displayName) + "\"}";
        root.putString("CustomName", nameJson);

        // VillagerData
        CompoundTag villagerData = new CompoundTag();
        villagerData.putString("profession", profession);
        villagerData.putInt("level", 5);
        villagerData.putString("type", "minecraft:plains");
        root.put("VillagerData", villagerData);

        // Flags
        root.putBoolean("NoAI", true);
        root.putBoolean("PersistenceRequired", true);

        // CobblemonCasino
        root.putInt("cobblemoncasino.LookPlayer", 1);
        root.putFloat("cobblemoncasino.IdleYaw", 0f);

        // JobPos Memories
        if (jobPos != null) {
            CompoundTag brain = new CompoundTag();
            CompoundTag memories = new CompoundTag();
            CompoundTag jobSiteEntry = new CompoundTag();
            CompoundTag jobSiteValue = new CompoundTag();

            int[] posArray = new int[]{ jobPos.getX(), jobPos.getY(), jobPos.getZ() };
            jobSiteValue.putIntArray("pos", posArray);
            jobSiteValue.putString("dimension", "minecraft:overworld");

            jobSiteEntry.put("value", jobSiteValue);
            memories.put("minecraft:job_site", jobSiteEntry);
            brain.put("memories", memories);
            root.put("Brain", brain);
        }

        if (suitId > 0) {
            root.putInt("cobblemoncasino.CustomSuit", suitId);
        }

        return root;

    }

    private static @NotNull String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}

