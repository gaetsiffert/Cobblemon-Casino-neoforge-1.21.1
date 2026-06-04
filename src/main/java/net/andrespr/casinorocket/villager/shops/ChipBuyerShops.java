package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Item;

public final class ChipBuyerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {
        ListTag shops = new ListTag();
        ListTag chipOffers = new ListTag();

        for (Item item : ModItems.ALL_CHIP_ITEMS) {
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, (ChipItem) item);
        }

        CompoundTag offers = VillagerTradeHelper.makeVanillaShopCompound(chipOffers);
        return new VillagerTradeHelper.ShopData(shops, "casinorocket:casino_worker", 2, "casinorocket:chip_table").withOffers(offers);
    }

    @Override
    public String getName() {
        return "Chip Buyer";
    }
}
