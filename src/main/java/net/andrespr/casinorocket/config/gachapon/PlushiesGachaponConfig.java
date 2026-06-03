package net.andrespr.casinorocket.config.gachapon;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = "gachapon/plushies_gachapon")
public class PlushiesGachaponConfig implements ConfigData {

    public List<GachaEntry> plushies = defaultPlushies();

    public static class GachaEntry {
        public String itemId;
        public int weight;

        public GachaEntry() {}

        public GachaEntry(String itemId, int weight) {
            this.itemId = itemId;
            this.weight = weight;
        }
    }

    private static List<GachaEntry> defaultPlushies() {
        List<GachaEntry> list = new ArrayList<>();

        list.add(new GachaEntry("casinorocket:litwick_pin", 10));
        list.add(new GachaEntry("casinorocket:staryu_pin", 10));
        list.add(new GachaEntry("casinorocket:bellsprout_pin", 10));
        list.add(new GachaEntry("casinorocket:tyrogue_pin", 8));
        list.add(new GachaEntry("casinorocket:scyther_pin", 7));
        list.add(new GachaEntry("casinorocket:eevee_pin", 6));
        list.add(new GachaEntry("casinorocket:dratini_pin", 4));
        list.add(new GachaEntry("casinorocket:rotom_pin", 4));
        list.add(new GachaEntry("casinorocket:porygon_pin", 3));
        list.add(new GachaEntry("casinorocket:ditto_pin", 1));

        return list;
    }

}

