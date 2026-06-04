package net.andrespr.casinorocket.config.machines;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "machines/chip_table")
public class ChipTableConfig implements ConfigData {

    public boolean enable_currency_to_chips = true;
    public boolean enable_chips_to_relic_coins = true;
    public boolean enable_chips_to_cobbledollars = true;
    public boolean enable_cobbledollars_to_chips = true;

    public long relic_coin_value = 10;
    public long handful_of_relic_coins_value = 40;
    public long relic_coin_pouch_value = 90;
    public long relic_coin_sack_value = 810;
    public long stack_of_relic_coins_value = 160;

}
