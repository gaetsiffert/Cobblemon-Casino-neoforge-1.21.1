package net.andrespr.casinorocket.config.npc;

import me.shedaniel.autoconfig.ConfigData;

import java.util.ArrayList;
import java.util.List;

public abstract class NpcTradeConfig implements ConfigData {
    public List<Trade> trades = new ArrayList<>();

    public static class Trade implements ConfigData {
        public String buy_item;
        public int buy_count;
        public String sell_item;
        public int sell_count;

        public Trade() {}

        public Trade(String buyItem, int buyCount, String sellItem, int sellCount) {
            this.buy_item = buyItem;
            this.buy_count = buyCount;
            this.sell_item = sellItem;
            this.sell_count = sellCount;
        }
    }
}
