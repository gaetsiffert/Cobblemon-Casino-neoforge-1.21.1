package net.andrespr.casinorocket.config.npc;

import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = "npc/exchanger")
public class ExchangerNpcConfig extends NpcTradeConfig {
    public ExchangerNpcConfig() {
        this.trades = List.of(
                new Trade("casinorocket:diamond_nugget", 1, "cobblemon:relic_coin", 10),
                new Trade("cobblemon:relic_coin", 10, "casinorocket:diamond_nugget", 1),
                new Trade("casinorocket:diamond_nugget", 2, "casinorocket:handful_of_relic_coins", 5),
                new Trade("casinorocket:handful_of_relic_coins", 5, "casinorocket:diamond_nugget", 2),
                new Trade("casinorocket:diamond_nugget", 8, "casinorocket:stack_of_relic_coins", 5),
                new Trade("casinorocket:stack_of_relic_coins", 5, "casinorocket:diamond_nugget", 8),
                new Trade("casinorocket:diamond_nugget", 9, "cobblemon:relic_coin_pouch", 10),
                new Trade("cobblemon:relic_coin_pouch", 10, "casinorocket:diamond_nugget", 9)
        );
    }
}
