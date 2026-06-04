package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;

public final class CobbledollarsDealerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {
        ListTag shops = new ListTag();

        ListTag chipOffers = new ListTag();
        for (Item item : ModItems.ALL_CHIP_ITEMS) {
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, (ChipItem) item);
        }
        VillagerTradeHelper.addShopCompound(shops, "Chips", chipOffers);

        ListTag pinOffers = new ListTag();
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:litwick_pin", "10000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:staryu_pin", "10000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:bellsprout_pin", "10000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:tyrogue_pin", "25000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:scyther_pin", "50000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:eevee_pin", "75000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:dratini_pin", "100000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:rotom_pin", "250000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:ditto_pin", "500000"));
        pinOffers.add(VillagerTradeHelper.makeOffer("casinorocket:porygon_pin", "999999"));
        VillagerTradeHelper.addShopCompound(shops, "Pokemon Pins", pinOffers);

        return new VillagerTradeHelper.ShopData(shops, "cobbledollars:cobble_merchant", 1, "cobblemon:display_case");
    }

    @Override
    public String getName() {
        return "Cobbledollars Dealer";
    }
}
