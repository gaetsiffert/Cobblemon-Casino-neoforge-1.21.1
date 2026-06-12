package net.narrnouille.cobblemoncasino.villager;

import net.narrnouille.cobblemoncasino.villager.shops.*;

import java.util.Map;
import java.util.TreeMap;

public final class ShopsRegistry {

    private static final Map<String, IShop> SHOPS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private ShopsRegistry() {}

    public static void register(IShop shop) {
        SHOPS.put(shop.getName(), shop);
    }

    public static IShop get(String name) {
        return SHOPS.get(name);
    }

    public static Map<String, IShop> all() {
        return SHOPS;
    }

    public static void bootstrap() {
        register(new CobbledollarsDealerShops());
        register(new DiamondRelicExchangeShops());
        register(new PrizeDealerShops());
    }

}

