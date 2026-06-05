package net.andrespr.casinorocket.config.npc;

import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = "npc/prize_dealer")
public class PrizeDealerNpcConfig extends NpcTradeConfig {
    public PrizeDealerNpcConfig() {
        this.trades = List.of(
                new Trade("casinorocket:diamond_nugget", 1, "casinorocket:copper_coin", 4),
                new Trade("casinorocket:diamond_nugget", 2, "casinorocket:iron_coin", 3),
                new Trade("casinorocket:diamond_nugget", 4, "casinorocket:gold_coin", 2),
                new Trade("casinorocket:diamond_nugget", 8, "casinorocket:diamond_coin", 1),
                new Trade("casinorocket:diamond_nugget", 10, "casinorocket:primogem", 1),
                new Trade("casinorocket:diamond_nugget", 12, "casinorocket:event_coin", 1),
                new Trade("casinorocket:diamond_nugget", 5, "casinorocket:litwick_pin", 1),
                new Trade("casinorocket:diamond_nugget", 5, "casinorocket:staryu_pin", 1),
                new Trade("casinorocket:diamond_nugget", 5, "casinorocket:bellsprout_pin", 1),
                new Trade("casinorocket:diamond_nugget", 13, "casinorocket:tyrogue_pin", 1),
                new Trade("casinorocket:diamond_nugget", 25, "casinorocket:scyther_pin", 1),
                new Trade("casinorocket:diamond_nugget", 38, "casinorocket:eevee_pin", 1),
                new Trade("casinorocket:diamond_nugget", 50, "casinorocket:dratini_pin", 1),
                new Trade("minecraft:diamond", 14, "casinorocket:rotom_pin", 1),
                new Trade("minecraft:diamond", 28, "casinorocket:ditto_pin", 1),
                new Trade("minecraft:diamond", 56, "casinorocket:porygon_pin", 1)
        );
    }
}
