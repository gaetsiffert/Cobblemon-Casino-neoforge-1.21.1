package net.narrnouille.cobblemoncasino.villager.shops;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.villager.VillagerTradeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public final class DiamondRelicExchangeShops implements IShop {
    @Override
    public VillagerTradeHelper.ShopData build() {
        ListTag shops = new ListTag();
        ListTag trades = new ListTag();

        if (CobblemonCasino.CONFIG.exchangerNpc != null) {
            VillagerTradeHelper.addConfiguredVanillaOffers(trades, CobblemonCasino.CONFIG.exchangerNpc.trades);
        }

        CompoundTag offers = VillagerTradeHelper.makeVanillaShopCompound(trades);
        return new VillagerTradeHelper.ShopData(shops, "cobblemoncasino:casino_worker", 3, "cobblemoncasino:chip_table")
                .withOffers(offers);
    }

    @Override
    public String getName() {
        return "Exchanger";
    }
}
