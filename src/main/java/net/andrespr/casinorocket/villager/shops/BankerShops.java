package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.BillItem;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;

public final class BankerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        ListTag shops = new ListTag();

        // ===== IN CASH MONEY =====
        ListTag billOffers = new ListTag();
        for (Item bill : ModItems.ALL_BILL_ITEMS) VillagerTradeHelper.makeInCashOffer(billOffers, (BillItem) bill);
        VillagerTradeHelper.addShopCompound(shops, "Bills", billOffers);

        return new VillagerTradeHelper.ShopData(shops, "cobbledollars:cobble_merchant", 2, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Banker";
    }

}

