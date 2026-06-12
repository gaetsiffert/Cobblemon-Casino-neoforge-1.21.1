package net.narrnouille.cobblemoncasino.villager.shops;

import net.narrnouille.cobblemoncasino.villager.VillagerTradeHelper;

public interface IShop {
    VillagerTradeHelper.ShopData build();
    String getName();
}

