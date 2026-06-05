package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.npc.CobbledollarsDealerNpcConfig;
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

        CobbledollarsDealerNpcConfig config = CasinoRocket.CONFIG.cobbledollarsDealerNpc;
        if (config != null && config.categories != null) {
            for (CobbledollarsDealerNpcConfig.Category category : config.categories) {
                VillagerTradeHelper.addCobbledollarCategory(shops, category);
            }
        }

        return new VillagerTradeHelper.ShopData(shops, "cobbledollars:cobble_merchant", 1, "cobblemon:display_case");
    }

    @Override
    public String getName() {
        return "Cobbledollars Dealer";
    }
}
