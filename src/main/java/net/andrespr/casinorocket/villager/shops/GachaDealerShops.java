package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.ListTag;

public final class GachaDealerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        ListTag shops = new ListTag();

        // ===== COINS =====
        ListTag coinOffers = new ListTag();
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.COPPER_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.IRON_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.GOLD_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.DIAMOND_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.EVENT_COIN);
        VillagerTradeHelper.makeCoinOffer(coinOffers, ModItems.PRIMOGEM);
        VillagerTradeHelper.addShopCompound(shops, "Gacha Coins", coinOffers);

        if (CasinoRocket.CONFIG.gachaMachines.gacha_store.gachapon_store.enableItemGachaponStore) {
            ListTag ItemGachaponOffers = new ListTag();
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.POKE_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.GREAT_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.ULTRA_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.MASTER_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.CHERISH_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(ItemGachaponOffers, ModItems.PREMIER_GACHAPON);
            VillagerTradeHelper.addShopCompound(shops, "Item Gacha", ItemGachaponOffers);
        }
        if (CasinoRocket.CONFIG.gachaMachines.gacha_store.gachapon_store.enablePokemonGachaponStore) {
            ListTag PokemonGachaponOffers = new ListTag();
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_POKE_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_GREAT_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_ULTRA_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_MASTER_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_CHERISH_GACHAPON);
            VillagerTradeHelper.makeGachaponOffer(PokemonGachaponOffers, ModItems.POKEMON_PREMIER_GACHAPON);
            VillagerTradeHelper.addShopCompound(shops, "Pokémon Gacha", PokemonGachaponOffers);
        }

        return new VillagerTradeHelper.ShopData(shops,"cobbledollars:cobble_merchant", 3, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Mr. Lucky";
    }

}

