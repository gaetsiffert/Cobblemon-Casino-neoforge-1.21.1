package net.narrnouille.cobblemoncasino.config.npc;

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
                        new Offer("cobblemoncasino:litwick_pin", 500, -1),
                        new Offer("cobblemoncasino:staryu_pin", 500, -1),
                        new Offer("cobblemoncasino:bellsprout_pin", 500, -1),
                        new Offer("cobblemoncasino:tyrogue_pin", 1_250, -1),
                        new Offer("cobblemoncasino:scyther_pin", 2_500, -1),
                        new Offer("cobblemoncasino:eevee_pin", 3_750, -1),
                        new Offer("cobblemoncasino:dratini_pin", 5_000, -1),
                        new Offer("cobblemoncasino:rotom_pin", 12_500, -1),
                        new Offer("cobblemoncasino:ditto_pin", 25_000, -1),
                        new Offer("cobblemoncasino:porygon_pin", 50_000, -1)
                )),
                new Category("Gacha Coins", List.of(
                        new Offer("cobblemoncasino:copper_coin", 100, 50),
                        new Offer("cobblemoncasino:iron_coin", 250, 125),
                        new Offer("cobblemoncasino:gold_coin", 750, 375),
                        new Offer("cobblemoncasino:diamond_coin", 2_000, 1_000),
                        new Offer("cobblemoncasino:primogem", 2_500, 1_250),
                        new Offer("cobblemoncasino:event_coin", 5_000, 2_500)
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
