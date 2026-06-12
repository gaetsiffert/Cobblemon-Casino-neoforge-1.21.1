package net.andrespr.casinorocket.games.gachapon;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.gachapon.ItemGachaponConfig;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GachaponUtils {

    public record CachedEntry(Item item, int count, int weight) {}
    public record CachedPool(List<CachedEntry> entries, int totalWeight, int[] cumulativeWeights) {}

    private static final Map<String, CachedPool> CACHE = new ConcurrentHashMap<>();
    private static final Set<String> WARNED_ITEMS = ConcurrentHashMap.newKeySet();

    public static void buildCache(Map<String, List<ItemGachaponConfig.GachaEntry>> pools) {
        CACHE.clear();
        WARNED_ITEMS.clear();

        for (Map.Entry<String, List<ItemGachaponConfig.GachaEntry>> poolEntry : pools.entrySet()) {
            String poolKey = poolEntry.getKey();
            List<ItemGachaponConfig.GachaEntry> entries = poolEntry.getValue();
            List<CachedEntry> valid = new ArrayList<>();
            int totalWeight = 0;

            for (ItemGachaponConfig.GachaEntry entry : entries) {
                if (entry == null || entry.itemId == null || entry.itemId.isBlank()) {
                    CasinoRocket.LOGGER.warn("[Gachapon] Skipping blank item id in pool '{}'", poolKey);
                    continue;
                }

                ResourceLocation id = ResourceLocation.tryParse(entry.itemId);
                if (id == null) {
                    if (WARNED_ITEMS.add(entry.itemId)) {
                        CasinoRocket.LOGGER.warn("[Gachapon] Invalid item id format in pool '{}': '{}'", poolKey, entry.itemId);
                    }
                    continue;
                }

                if (!BuiltInRegistries.ITEM.containsKey(id)) {
                    if (WARNED_ITEMS.add(entry.itemId)) {
                        CasinoRocket.LOGGER.warn("[Gachapon] Invalid item in pool '{}': '{}'", poolKey, entry.itemId);
                    }
                    continue;
                }

                Item item = BuiltInRegistries.ITEM.get(id);
                int count = Math.max(1, entry.count);
                int weight = Math.max(0, entry.weight);

                valid.add(new CachedEntry(item, count, weight));
                totalWeight += weight;
            }

            if (valid.isEmpty()) {
                CasinoRocket.LOGGER.warn("[Gachapon] Pool '{}' is empty or contains no valid items", poolKey);
            }

            int[] cumulative = new int[valid.size()];
            int cumulativeSum = 0;
            for (int i = 0; i < valid.size(); i++) {
                cumulativeSum += valid.get(i).weight();
                cumulative[i] = cumulativeSum;
            }

            CACHE.put(poolKey, new CachedPool(Collections.unmodifiableList(valid), totalWeight, cumulative));
        }

        CasinoRocket.LOGGER.info("[Gachapon] Cache built with '{}' pools.", CACHE.size());
    }

    public static ItemStack pickItemReward(RandomSource random, String poolKey) {
        CachedPool pool = CACHE.get(poolKey);
        if (pool == null || pool.entries().isEmpty()) return ItemStack.EMPTY;

        int totalWeight = pool.totalWeight();
        if (totalWeight <= 0) return ItemStack.EMPTY;

        int roll = random.nextInt(totalWeight);

        int index = Arrays.binarySearch(pool.cumulativeWeights(), roll + 1);
        if (index < 0) { index = -index - 1; }
        if (index >= pool.entries().size()) return ItemStack.EMPTY;

        CachedEntry entry = pool.entries().get(index);
        return new ItemStack(entry.item(), entry.count());
    }

    public static Component getPoolPercentages(String poolKey) {
        CachedPool pool = CACHE.get(poolKey);
        if (pool == null || pool.entries().isEmpty()) {
            return Component.translatable("command.casinorocket.pool_no_valid_items", poolKey).withStyle(ChatFormatting.RED);
        }

        int totalWeight = pool.totalWeight();
        if (totalWeight <= 0) {
            return Component.translatable("command.casinorocket.pool_total_weight_zero", poolKey).withStyle(ChatFormatting.RED);
        }

        MutableComponent result = Component.literal("")
                .append(Component.translatable("command.casinorocket.rates").withStyle(ChatFormatting.UNDERLINE)).append("\n");

        List<CachedEntry> sorted = new ArrayList<>(pool.entries());
        sorted.sort((a, b) -> Integer.compare(b.weight(), a.weight()));

        boolean first = true;

        for (CachedEntry entry : sorted) {
            if (!first) result.append(Component.literal(", "));
            first = false;

            ItemStack stack = new ItemStack(entry.item());
            double percentage = (entry.weight() * 100.0) / totalWeight;
            double rounded = Math.round(percentage * 100.0) / 100.0;

            ChatFormatting color = TextUtils.percentagesColor(rounded);

            result.append(stack.getHoverName())
                    .append(Component.literal(": ")
                    .append(Component.literal(String.format("%.2f%%", rounded)).withStyle(color)));
        }

        return result;
    }

    public static Set<String> getPools() {
        return CACHE.keySet();
    }

    public static boolean hasValidPool(String poolKey) {
        CachedPool pool = CACHE.get(poolKey);
        return pool != null && !pool.entries().isEmpty() && pool.totalWeight() > 0;
    }

}

