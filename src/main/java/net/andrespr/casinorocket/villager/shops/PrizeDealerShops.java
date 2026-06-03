package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.ListTag;

public final class PrizeDealerShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {

        ListTag shops = new ListTag();

        // ===== POKEMON PIN =====
        ListTag pokemonOffers = new ListTag();
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:litwick_pin", "10000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:staryu_pin", "10000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:bellsprout_pin", "10000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:tyrogue_pin", "25000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:scyther_pin", "50000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:eevee_pin", "75000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:dratini_pin", "100000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:rotom_pin", "250000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:ditto_pin", "500000"));
        pokemonOffers.add(VillagerTradeHelper.makeOffer("casinorocket:porygon_pin", "999999"));
        VillagerTradeHelper.addShopCompound(shops, "Pokemon", pokemonOffers);

        // ===== FOSSILS =====
        ListTag fossilOffers = new ListTag();
        fossilOffers.add(VillagerTradeHelper.makeOffer("cobblemon:helix_fossil","50000"));
        fossilOffers.add(VillagerTradeHelper.makeOffer("cobblemon:dome_fossil","50000"));
        fossilOffers.add(VillagerTradeHelper.makeOffer("cobblemon:root_fossil","75000"));
        fossilOffers.add(VillagerTradeHelper.makeOffer("cobblemon:claw_fossil","75000"));
        fossilOffers.add(VillagerTradeHelper.makeOffer("cobblemon:old_amber_fossil","100000"));
        VillagerTradeHelper.addShopCompound(shops, "Fossils", fossilOffers);

        return new VillagerTradeHelper.ShopData(shops,"cobbledollars:cobble_merchant",1, "cobblemon:display_case");

    }

    @Override
    public String getName() {
        return "Prize Dealer";
    }

}

