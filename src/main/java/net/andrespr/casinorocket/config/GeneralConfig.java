package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;

import java.util.Locale;

@Config(name = "general_config")
public class GeneralConfig implements ConfigData {

    public String economy_type = "relic_coins";

    @CollapsibleObject
    public ChipPrices chip_prices_in_money = new ChipPrices(1, 5, 10, 50, 100, 500, 1_000, 5_000, 10_000, 50_000, 100_000, 500_000, 1_000_000);
    @CollapsibleObject
    public ChipPrices chip_prices_in_items = new ChipPrices(1, 5, 10, 50, 100, 500, 1_000, 5_000, 10_000, 50_000, 100_000, 500_000, 1_000_000);

    public static class ChipPrices implements ConfigData {

        public long red_chip;
        public long blue_chip;
        public long yellow_chip;
        public long purple_chip;
        public long copper_chip;
        public long iron_chip;
        public long emerald_chip;
        public long gold_chip;
        public long diamond_chip;
        public long netherite_chip;
        public long black_chip;
        public long white_chip;
        public long rainbow_chip;

        public ChipPrices() {}
        public ChipPrices(long redChip, long blueChip, long yellowChip, long purpleChip, long copperChip,
                          long ironChip, long emeraldChip, long goldChip, long diamondChip, long netheriteChip,
                          long blackChip, long whiteChip, long rainbowChip) {
            this.red_chip = redChip;
            this.blue_chip = blueChip;
            this.yellow_chip = yellowChip;
            this.purple_chip = purpleChip;
            this.copper_chip = copperChip;
            this.iron_chip = ironChip;
            this.emerald_chip = emeraldChip;
            this.gold_chip = goldChip;
            this.diamond_chip = diamondChip;
            this.netherite_chip = netheriteChip;
            this.black_chip = blackChip;
            this.white_chip = whiteChip;
            this.rainbow_chip = rainbowChip;
        }

    }

    public boolean enableMachinesCrafting = true;
    public boolean makeMachinesUnbreakable = false;
    public boolean enableDirectBets = true;
    public boolean enableDirectPayout = true;

    // ===== HELPERS =====

    public long getChipPriceInMoney(String chipId) {
        chipId = chipId.toLowerCase(Locale.ROOT);
        return switch (chipId) {
            case "red_chip" -> chip_prices_in_money.red_chip;
            case "blue_chip" -> chip_prices_in_money.blue_chip;
            case "yellow_chip" -> chip_prices_in_money.yellow_chip;
            case "purple_chip" -> chip_prices_in_money.purple_chip;
            case "copper_chip" -> chip_prices_in_money.copper_chip;
            case "iron_chip" -> chip_prices_in_money.iron_chip;
            case "emerald_chip" -> chip_prices_in_money.emerald_chip;
            case "gold_chip" -> chip_prices_in_money.gold_chip;
            case "diamond_chip" -> chip_prices_in_money.diamond_chip;
            case "netherite_chip" -> chip_prices_in_money.netherite_chip;
            case "black_chip" -> chip_prices_in_money.black_chip;
            case "white_chip" -> chip_prices_in_money.white_chip;
            case "rainbow_chip" -> chip_prices_in_money.rainbow_chip;
            default -> 100;
        };
    }

    public long getChipPriceInItems(String chipId) {
        chipId = chipId.toLowerCase(Locale.ROOT);
        return switch (chipId) {
            case "red_chip" -> chip_prices_in_items.red_chip;
            case "blue_chip" -> chip_prices_in_items.blue_chip;
            case "yellow_chip" -> chip_prices_in_items.yellow_chip;
            case "purple_chip" -> chip_prices_in_items.purple_chip;
            case "copper_chip" -> chip_prices_in_items.copper_chip;
            case "iron_chip" -> chip_prices_in_items.iron_chip;
            case "emerald_chip" -> chip_prices_in_items.emerald_chip;
            case "gold_chip" -> chip_prices_in_items.gold_chip;
            case "diamond_chip" -> chip_prices_in_items.diamond_chip;
            case "netherite_chip" -> chip_prices_in_items.netherite_chip;
            case "black_chip" -> chip_prices_in_items.black_chip;
            case "white_chip" -> chip_prices_in_items.white_chip;
            case "rainbow_chip" -> chip_prices_in_items.rainbow_chip;
            default -> 1;
        };
    }

    public boolean isCobbledollarsActive() {
        return economy_type.equalsIgnoreCase("cobbledollars")
                || economy_type.equalsIgnoreCase("cobbledollar");
    }

    public boolean isRelicCoinActive() {
        return economy_type.equalsIgnoreCase("relic_coins")
                || economy_type.equalsIgnoreCase("relic_coin");
    }

    public boolean isDiamondActive() {
        return economy_type.equalsIgnoreCase("diamonds")
                || economy_type.equalsIgnoreCase("diamond");
    }

}

