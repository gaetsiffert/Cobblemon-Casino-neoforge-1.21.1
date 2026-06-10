package net.andrespr.casinorocket.villager;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.npc.NpcTradeConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

public final class CasinoVillagerTrades {

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 5;
    private static final int MAX_USES = 64;
    private static final float PRICE_MULTIPLIER = 0.0f;
    private static final int[] XP_BY_LEVEL = {2, 5, 10, 15, 30};

    private CasinoVillagerTrades() {}

    public static void register(VillagerTradesEvent event) {
        if (event.getType() != ModVillagers.CASINO_WORKER) {
            return;
        }

        Int2ObjectMap<List<VillagerTrades.ItemListing>> tradesByLevel = event.getTrades();
        if (CasinoRocket.CONFIG.prizeDealerNpc != null) {
            addConfiguredTrades(tradesByLevel, CasinoRocket.CONFIG.prizeDealerNpc.trades);
        }
        if (CasinoRocket.CONFIG.exchangerNpc != null) {
            addConfiguredTrades(tradesByLevel, CasinoRocket.CONFIG.exchangerNpc.trades);
        }
    }

    private static void addConfiguredTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> tradesByLevel, List<NpcTradeConfig.Trade> configuredTrades) {
        if (configuredTrades == null || configuredTrades.isEmpty()) {
            return;
        }

        for (int index = 0; index < configuredTrades.size(); index++) {
            NpcTradeConfig.Trade trade = configuredTrades.get(index);
            VillagerTrades.ItemListing listing = createListing(trade, levelForIndex(index, configuredTrades.size()));
            if (listing != null) {
                tradesByLevel.get(levelForIndex(index, configuredTrades.size())).add(listing);
            }
        }
    }

    private static VillagerTrades.ItemListing createListing(NpcTradeConfig.Trade trade, int level) {
        if (trade == null || trade.buy_item == null || trade.buy_item.isBlank()
                || trade.sell_item == null || trade.sell_item.isBlank()
                || trade.buy_count <= 0 || trade.sell_count <= 0) {
            return null;
        }

        Item buyItem = resolveItem(trade.buy_item);
        Item sellItem = resolveItem(trade.sell_item);
        if (buyItem == Items.AIR || sellItem == Items.AIR) {
            return null;
        }

        int xp = XP_BY_LEVEL[Math.clamp(level, MIN_LEVEL, MAX_LEVEL) - 1];
        return new BasicItemListing(
                new ItemStack(buyItem, trade.buy_count),
                new ItemStack(sellItem, trade.sell_count),
                MAX_USES,
                xp,
                PRICE_MULTIPLIER
        );
    }

    private static int levelForIndex(int index, int totalTrades) {
        if (totalTrades <= 1) {
            return MIN_LEVEL;
        }
        return Math.clamp(((index * MAX_LEVEL) / totalTrades) + 1, MIN_LEVEL, MAX_LEVEL);
    }

    private static Item resolveItem(String itemId) {
        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) {
            return Items.AIR;
        }
        return BuiltInRegistries.ITEM.get(id);
    }
}
