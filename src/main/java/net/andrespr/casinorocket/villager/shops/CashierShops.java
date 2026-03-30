package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public final class CashierShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        NbtList shops = new NbtList();
        NbtList chipOffers = new NbtList();

        if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {

            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.BASIC_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.RED_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.BLUE_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.PURPLE_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.COPPER_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.IRON_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.EMERALD_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.GOLD_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.DIAMOND_CHIP);
            VillagerTradeHelper.makeChipToMoneyOffer(chipOffers, ModItems.NETHERITE_CHIP);

        } else {

            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.BASIC_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.RED_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.BLUE_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.PURPLE_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.COPPER_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.IRON_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.EMERALD_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.GOLD_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.DIAMOND_CHIP);
            VillagerTradeHelper.makeChipToItemOffer(chipOffers, ModItems.NETHERITE_CHIP);

        }

        NbtCompound offers = VillagerTradeHelper.makeVanillaShopCompound(chipOffers);
        return new VillagerTradeHelper.ShopData(shops, "casinorocket:casino_worker", 2, "casinorocket:chip_table").withOffers(offers);

    }

    @Override
    public String getName() {
        return "Cashier";
    }

}