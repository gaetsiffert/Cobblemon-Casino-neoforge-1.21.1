package net.andrespr.casinorocket.config.npc;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = "npc/cobbledollars_dealer")
public class CobbledollarsDealerNpcConfig implements ConfigData {
    public List<Category> categories = new ArrayList<>();

    public CobbledollarsDealerNpcConfig() {
        this.categories = List.of(
                new Category("Pokemon Pins", List.of(
                        new Offer("casinorocket:litwick_pin", 500, -1),
                        new Offer("casinorocket:staryu_pin", 500, -1),
                        new Offer("casinorocket:bellsprout_pin", 500, -1),
                        new Offer("casinorocket:tyrogue_pin", 1_250, -1),
                        new Offer("casinorocket:scyther_pin", 2_500, -1),
                        new Offer("casinorocket:eevee_pin", 3_750, -1),
                        new Offer("casinorocket:dratini_pin", 5_000, -1),
                        new Offer("casinorocket:rotom_pin", 12_500, -1),
                        new Offer("casinorocket:ditto_pin", 25_000, -1),
                        new Offer("casinorocket:porygon_pin", 50_000, -1)
                )),
                new Category("Gacha Coins", List.of(
                        new Offer("casinorocket:copper_coin", 100, 50),
                        new Offer("casinorocket:iron_coin", 250, 125),
                        new Offer("casinorocket:gold_coin", 750, 375),
                        new Offer("casinorocket:diamond_coin", 2_000, 1_000),
                        new Offer("casinorocket:primogem", 2_500, 1_250),
                        new Offer("casinorocket:event_coin", 5_000, 2_500)
                ))
        );
    }

    public static class Category implements ConfigData {
        public String name;
        public List<Offer> offers = new ArrayList<>();

        public Category() {}

        public Category(String name, List<Offer> offers) {
            this.name = name;
            this.offers = offers;
        }
    }

    public static class Offer implements ConfigData {
        public String item;
        public long price;
        public long buyback_price;

        public Offer() {}

        public Offer(String item, long price, long buybackPrice) {
            this.item = item;
            this.price = price;
            this.buyback_price = buybackPrice;
        }
    }
}
