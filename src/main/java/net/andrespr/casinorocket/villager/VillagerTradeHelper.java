package net.andrespr.casinorocket.villager;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class VillagerTradeHelper {

    private static final String RELIC_CURRENCY_ID = "cobblemon:relic_coin";

    // ===== BASE STRUCTURE - SHOP DATA =====

    public static class ShopData {
        public final ListTag shops;
        public final String profession;
        public final int suitId;
        public CompoundTag offersNbt;
        public final String jobBlockId;

        public ShopData(ListTag shops, String profession, int suitId) {
            this(shops, profession, suitId, null);
        }

        public ShopData(ListTag shops, String profession, int suitId, String jobBlockId) {
            this.shops = shops;
            this.profession = profession;
            this.suitId = suitId;
            this.jobBlockId = jobBlockId;
        }

        public ShopData withOffers(CompoundTag offers) {
            this.offersNbt = offers;
            return this;
        }
    }

    // ===== COBBLEDOLLARS TRADES =====

    public static CompoundTag makeShopCompound(String category, ListTag offers) {
        return createShopCompound(category, filterShopOffers(offers));
    }

    public static boolean addShopCompound(ListTag shops, String category, ListTag offers) {
        ListTag filtered = filterShopOffers(offers);
        if (filtered.isEmpty()) {
            return false;
        }

        shops.add(createShopCompound(category, filtered));
        return true;
    }

    private static CompoundTag createShopCompound(String category, ListTag filteredOffers) {
        CompoundTag shop = new CompoundTag();
        shop.putString("Category", category);
        shop.put("Offers", filteredOffers);
        return shop;
    }

    public static CompoundTag makeOffer(String itemId, String price) {
        CompoundTag offer = new CompoundTag();
        CompoundTag item = new CompoundTag();
        item.putString("id", itemId);
        item.putByte("Count", (byte) 1);
        item.putInt("count", 1);
        offer.put("Item", item);
        offer.putString("Price", price);
        return offer;
    }

    // ===== VANILLA TRADES =====

    public static CompoundTag makeVanillaShopCompound(ListTag trades) {
        CompoundTag offers = new CompoundTag();
        offers.put("Recipes", filterVanillaOffers(trades));
        return offers;
    }

    public static boolean hasTrades(ShopData data) {
        return data != null
                && ((data.shops != null && !data.shops.isEmpty()) || hasVanillaOffers(data.offersNbt));
    }

    public static boolean hasVanillaOffers(CompoundTag offers) {
        return offers != null
                && offers.contains("Recipes")
                && !offers.getList("Recipes", Tag.TAG_COMPOUND).isEmpty();
    }

    public static CompoundTag makeVanillaOffer(String buyItem, int buyCount, String sellItem, int sellCount) {
        CompoundTag trade = new CompoundTag();

        CompoundTag buy = new CompoundTag();
        buy.putString("id", buyItem);
        buy.putByte("Count", (byte) buyCount);
        buy.putInt("count", buyCount);
        trade.put("buy", buy);

        CompoundTag sell = new CompoundTag();
        sell.putString("id", sellItem);
        sell.putByte("Count", (byte) sellCount);
        sell.putInt("count", sellCount);
        trade.put("sell", sell);

        trade.putBoolean("rewardExp", false);
        trade.putInt("maxUses", 999999);
        trade.putInt("uses", 0);
        trade.putFloat("priceMultiplier", 0.0f);
        trade.putInt("demand", 0);
        trade.putInt("specialPrice", 0);
        trade.putInt("xp", 0);
        trade.putBoolean("ignorePriceMultiplier", true);

        return trade;
    }

    // ===== SPECIFIC TRADES =====

    public static void makeMoneyToChipOffer(ListTag trades, ChipItem chip) {
        String chipId = BuiltInRegistries.ITEM.getKey(chip).toString();
        String value = String.valueOf(getMoneyChipValue(chip));
        trades.add(makeOffer(chipId, value));
    }

    public static void makeItemToChipOffer(ListTag trades, ChipItem chip) {
        String chipId = BuiltInRegistries.ITEM.getKey(chip).toString();
        MoneyCalculator.ItemResult itemResult = MoneyCalculator.calculateItemAmount(RELIC_CURRENCY_ID, getRelicCoinChipValue(chip));
        trades.add(makeVanillaOffer(itemResult.item(), Math.toIntExact(itemResult.amount()), chipId, 1));
    }

    public static void makeChipToItemOffer(ListTag trades, ChipItem chip) {
        String chipId = BuiltInRegistries.ITEM.getKey(chip).toString();
        MoneyCalculator.ItemResult itemResult = MoneyCalculator.calculateItemAmount(RELIC_CURRENCY_ID, getRelicCoinChipValue(chip));
        trades.add(makeVanillaOffer(chipId, 1, itemResult.item(), Math.toIntExact(itemResult.amount())));
    }

    private static long getMoneyChipValue(ChipItem chip) {
        return CasinoRocket.CONFIG.generalConfig.getMoneyChipValue(BuiltInRegistries.ITEM.getKey(chip).getPath());
    }

    private static long getRelicCoinChipValue(ChipItem chip) {
        String chipPath = BuiltInRegistries.ITEM.getKey(chip).getPath();
        return CasinoRocket.CONFIG.generalConfig.getRelicCoinChipValue(chipPath);
    }

    private static ListTag filterShopOffers(ListTag offers) {
        ListTag filtered = new ListTag();
        for (int i = 0; i < offers.size(); i++) {
            CompoundTag offer = offers.getCompound(i);
            CompoundTag item = offer.getCompound("Item");
            if (isKnownItem(item.getString("id"))) {
                filtered.add(offer);
            }
        }
        return filtered;
    }

    private static ListTag filterVanillaOffers(ListTag trades) {
        ListTag filtered = new ListTag();
        for (int i = 0; i < trades.size(); i++) {
            CompoundTag trade = trades.getCompound(i);
            if (isKnownItem(trade.getCompound("buy").getString("id"))
                    && isKnownItem(trade.getCompound("sell").getString("id"))) {
                filtered.add(trade);
            }
        }
        return filtered;
    }

    private static boolean isKnownItem(String itemId) {
        ResourceLocation id = ResourceLocation.tryParse(itemId);
        return id != null && BuiltInRegistries.ITEM.containsKey(id) && BuiltInRegistries.ITEM.get(id) != Items.AIR;
    }

}

