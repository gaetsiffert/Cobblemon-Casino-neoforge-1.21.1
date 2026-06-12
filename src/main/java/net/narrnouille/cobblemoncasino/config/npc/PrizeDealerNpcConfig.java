package net.narrnouille.cobblemoncasino.config.npc;

import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = "npc/prize_dealer")
public class PrizeDealerNpcConfig extends NpcTradeConfig {
    public PrizeDealerNpcConfig() {
        this.trades = List.of(
                new Trade("cobblemoncasino:diamond_nugget", 1, "cobblemoncasino:copper_coin", 4),
                new Trade("cobblemoncasino:diamond_nugget", 2, "cobblemoncasino:iron_coin", 3),
                new Trade("cobblemoncasino:diamond_nugget", 4, "cobblemoncasino:gold_coin", 2),
                new Trade("cobblemoncasino:diamond_nugget", 9, "cobblemoncasino:diamond_coin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 12, "cobblemoncasino:event_coin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 13, "cobblemoncasino:primogem", 1),
                new Trade("cobblemoncasino:diamond_nugget", 5, "cobblemoncasino:litwick_pin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 5, "cobblemoncasino:staryu_pin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 5, "cobblemoncasino:bellsprout_pin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 13, "cobblemoncasino:tyrogue_pin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 25, "cobblemoncasino:scyther_pin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 38, "cobblemoncasino:eevee_pin", 1),
                new Trade("cobblemoncasino:diamond_nugget", 50, "cobblemoncasino:dratini_pin", 1),
                new Trade("minecraft:diamond", 14, "cobblemoncasino:rotom_pin", 1),
                new Trade("minecraft:diamond", 28, "cobblemoncasino:ditto_pin", 1),
                new Trade("minecraft:diamond", 56, "cobblemoncasino:porygon_pin", 1)
        );
    }
}
