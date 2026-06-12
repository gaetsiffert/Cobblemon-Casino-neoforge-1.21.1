package net.narrnouille.cobblemoncasino.games.gachapon;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.config.gachapon.PlushiesGachaponConfig;
import net.narrnouille.cobblemoncasino.util.TextUtils;
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

public final class PlushiesGachaponUtils {

    public record CachedEntry(Item item, int weight) {}

    private static final List<CachedEntry> ENTRIES = new ArrayList<>();
    private static int TOTAL_WEIGHT = 0;
    private static int[] CUMULATIVE = new int[0];

    private static final Set<String> WARNED_ITEMS = ConcurrentHashMap.newKeySet();

    private PlushiesGachaponUtils() {}

    public static void buildCache(List<PlushiesGachaponConfig.GachaEntry> plushies) {

        ENTRIES.clear();
        WARNED_ITEMS.clear();
        TOTAL_WEIGHT = 0;

        if (plushies == null || plushies.isEmpty()) {
            CobblemonCasino.LOGGER.warn("[PlushiesGachapon] Plushies list is empty.");
            CUMULATIVE = new int[0];
            return;
        }

        for (PlushiesGachaponConfig.GachaEntry entry : plushies) {
            if (entry == null || entry.itemId == null || entry.itemId.isBlank()) continue;

            ResourceLocation id = ResourceLocation.tryParse(entry.itemId);
            if (id == null) {
                if (WARNED_ITEMS.add(entry.itemId)) {
                    CobblemonCasino.LOGGER.warn("[PlushiesGachapon] Invalid item id format '{}'", entry.itemId);
                }
                continue;
            }

            if (!BuiltInRegistries.ITEM.containsKey(id)) {
                if (WARNED_ITEMS.add(entry.itemId)) {
                    CobblemonCasino.LOGGER.warn("[PlushiesGachapon] Invalid item '{}'", entry.itemId);
                }
                continue;
            }

            int weight = Math.max(0, entry.weight);
            if (weight <= 0) continue;

            Item item = BuiltInRegistries.ITEM.get(id);
            ENTRIES.add(new CachedEntry(item, weight));
            TOTAL_WEIGHT += weight;
        }

        if (ENTRIES.isEmpty()) {
            CobblemonCasino.LOGGER.warn("[PlushiesGachapon] No valid items found (all invalid or weight=0).");
            CUMULATIVE = new int[0];
            return;
        }

        CUMULATIVE = new int[ENTRIES.size()];
        int sum = 0;
        for (int i = 0; i < ENTRIES.size(); i++) {
            sum += ENTRIES.get(i).weight();
            CUMULATIVE[i] = sum;
        }

        CobblemonCasino.LOGGER.info("[PlushiesGachapon] Cache built with {} entries. TotalWeight={}", ENTRIES.size(), TOTAL_WEIGHT);

    }

    public static ItemStack pickPlushie(RandomSource random) {

        if (ENTRIES.isEmpty() || TOTAL_WEIGHT <= 0) return ItemStack.EMPTY;

        int roll = random.nextInt(TOTAL_WEIGHT);
        int idx = Arrays.binarySearch(CUMULATIVE, roll + 1);
        if (idx < 0) idx = -idx - 1;
        if (idx < 0 || idx >= ENTRIES.size()) return ItemStack.EMPTY;

        CachedEntry entry = ENTRIES.get(idx);
        return new ItemStack(entry.item());

    }

    public static Component getRates() {

        if (ENTRIES.isEmpty()) {
            return Component.translatable("message.cobblemoncasino.no_plushies_available").withStyle(ChatFormatting.RED);
        }
        if (TOTAL_WEIGHT <= 0) {
            return Component.translatable("command.cobblemoncasino.plushies_total_weight_zero").withStyle(ChatFormatting.RED);
        }

        MutableComponent result = Component.literal("")
                .append(Component.translatable("command.cobblemoncasino.rates").withStyle(ChatFormatting.UNDERLINE))
                .append("\n");

        List<CachedEntry> sorted = new ArrayList<>(ENTRIES);
        sorted.sort((a, b) -> Integer.compare(b.weight(), a.weight()));

        boolean first = true;
        for (CachedEntry e : sorted) {
            if (!first) result.append(Component.literal(", "));
            first = false;

            ItemStack stack = new ItemStack(e.item());
            double pct = (e.weight() * 100.0) / TOTAL_WEIGHT;
            double rounded = Math.round(pct * 100.0) / 100.0;

            ChatFormatting color = TextUtils.percentagesColor(rounded);

            result.append(stack.getHoverName())
                    .append(Component.literal(": ")
                            .append(Component.literal(String.format("%.2f%%", rounded)).withStyle(color)));
        }

        return result;
    }

    public static int getEntryCount() {
        return ENTRIES.size();
    }

    public static boolean hasRewards() {
        return getEntryCount() > 0;
    }

}

