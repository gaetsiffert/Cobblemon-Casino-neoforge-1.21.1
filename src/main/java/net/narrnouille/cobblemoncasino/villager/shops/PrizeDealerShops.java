package net.narrnouille.cobblemoncasino.villager.shops;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.villager.VillagerTradeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public final class PrizeDealerShops implements IShop {
    @Override
    public VillagerTradeHelper.ShopData build() {
        ListTag shops = new ListTag();
        ListTag trades = new ListTag();

        if (CobblemonCasino.CONFIG.prizeDealerNpc != null) {
            VillagerTradeHelper.addConfiguredVanillaOffers(trades, CobblemonCasino.CONFIG.prizeDealerNpc.trades);
        }

        CompoundTag offers = VillagerTradeHelper.makeVanillaShopCompound(trades);
        return new VillagerTradeHelper.ShopData(shops, "cobblemoncasino:casino_worker", 1, "cobblemoncasino:chip_table")
                .withOffers(offers);
    }

    @Override
    public String getName() {
        return "Prize Dealer";
    }
}
