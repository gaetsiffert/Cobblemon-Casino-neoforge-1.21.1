package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.CasinoRocketConfig;
import net.andrespr.casinorocket.config.GeneralConfig;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public final class ChipDealerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        ListTag shops = new ListTag();
        ListTag chipOffers = new ListTag();

        if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {

            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.BASIC_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.RED_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.BLUE_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.PURPLE_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.COPPER_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.IRON_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.EMERALD_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.GOLD_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.DIAMOND_CHIP);
            VillagerTradeHelper.makeMoneyToChipOffer(chipOffers, ModItems.NETHERITE_CHIP);
            shops.add(VillagerTradeHelper.makeShopCompound("Chips", chipOffers));

            return new VillagerTradeHelper.ShopData(shops, "cobbledollars:cobble_merchant", 2, "cobblemon:display_case");

        } else {

            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.BASIC_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.RED_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.BLUE_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.PURPLE_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.COPPER_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.IRON_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.EMERALD_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.GOLD_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.DIAMOND_CHIP);
            VillagerTradeHelper.makeItemToChipOffer(chipOffers, ModItems.NETHERITE_CHIP);
            CompoundTag offers = VillagerTradeHelper.makeVanillaShopCompound(chipOffers);

            return new VillagerTradeHelper.ShopData(shops, "casinorocket:casino_worker", 2, "casinorocket:chip_table").withOffers(offers);

        }

    }

    @Override
    public String getName() {
        return "Chip Dealer";
    }

}

