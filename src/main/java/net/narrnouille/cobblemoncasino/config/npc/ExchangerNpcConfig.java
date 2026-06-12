package net.narrnouille.cobblemoncasino.config.npc;

import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = "npc/exchanger")
public class ExchangerNpcConfig extends NpcTradeConfig {
    public ExchangerNpcConfig() {
        this.trades = List.of(
                new Trade("cobblemoncasino:diamond_nugget", 1, "cobblemon:relic_coin", 10),
                new Trade("cobblemon:relic_coin", 10, "cobblemoncasino:diamond_nugget", 1),
                new Trade("cobblemoncasino:diamond_nugget", 2, "cobblemoncasino:handful_of_relic_coins", 5),
                new Trade("cobblemoncasino:handful_of_relic_coins", 5, "cobblemoncasino:diamond_nugget", 2),
                new Trade("cobblemoncasino:diamond_nugget", 8, "cobblemoncasino:stack_of_relic_coins", 5),
                new Trade("cobblemoncasino:stack_of_relic_coins", 5, "cobblemoncasino:diamond_nugget", 8),
                new Trade("cobblemoncasino:diamond_nugget", 9, "cobblemon:relic_coin_pouch", 10),
                new Trade("cobblemon:relic_coin_pouch", 10, "cobblemoncasino:diamond_nugget", 9)
        );
    }
}
