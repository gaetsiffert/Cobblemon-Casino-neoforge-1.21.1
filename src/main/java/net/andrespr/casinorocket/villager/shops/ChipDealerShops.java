package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;

public final class ChipDealerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        ListTag shops = new ListTag();
        ListTag chipOffers = new ListTag();

        if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {

            for (Item item : ModItems.ALL_CHIP_ITEMS) {
                VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, (ChipItem) item);
            }
            VillagerTradeHelper.addShopCompound(shops, "Chips", chipOffers);

            return new VillagerTradeHelper.ShopData(shops, "cobbledollars:cobble_merchant", 2, "cobblemon:display_case");

        } else {

            for (Item item : ModItems.ALL_CHIP_ITEMS) {
                VillagerTradeHelper.makeItemToChipOffer(chipOffers, (ChipItem) item);
            }
            CompoundTag offers = VillagerTradeHelper.makeVanillaShopCompound(chipOffers);

            return new VillagerTradeHelper.ShopData(shops, "casinorocket:casino_worker", 2, "casinorocket:chip_table").withOffers(offers);

        }

    }

    @Override
    public String getName() {
        return "Chip Dealer";
    }

}

