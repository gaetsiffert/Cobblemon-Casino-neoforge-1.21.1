package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public final class DiamondRelicExchangeShops implements IShop {
    @Override
    public VillagerTradeHelper.ShopData build() {
        ListTag shops = new ListTag();
        ListTag trades = new ListTag();

        if (CasinoRocket.CONFIG.exchangerNpc != null) {
            VillagerTradeHelper.addConfiguredVanillaOffers(trades, CasinoRocket.CONFIG.exchangerNpc.trades);
        }

        CompoundTag offers = VillagerTradeHelper.makeVanillaShopCompound(trades);
        return new VillagerTradeHelper.ShopData(shops, "casinorocket:casino_worker", 3, "casinorocket:chip_table")
                .withOffers(offers);
    }

    @Override
    public String getName() {
        return "Exchanger";
    }
}
