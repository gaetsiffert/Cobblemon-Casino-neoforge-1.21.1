package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public final class PrizeDealerShops implements IShop {
    @Override
    public VillagerTradeHelper.ShopData build() {
        ListTag shops = new ListTag();
        ListTag trades = new ListTag();

        if (CasinoRocket.CONFIG.prizeDealerNpc != null) {
            VillagerTradeHelper.addConfiguredVanillaOffers(trades, CasinoRocket.CONFIG.prizeDealerNpc.trades);
        }

        CompoundTag offers = VillagerTradeHelper.makeVanillaShopCompound(trades);
        return new VillagerTradeHelper.ShopData(shops, "casinorocket:casino_worker", 1, "casinorocket:chip_table")
                .withOffers(offers);
    }

    @Override
    public String getName() {
        return "Prize Dealer";
    }
}
