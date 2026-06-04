package net.andrespr.casinorocket.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;

import java.util.Locale;

@Config(name = "general_config")
public class GeneralConfig implements ConfigData {

    @CollapsibleObject
    public ChipValues money_chip_values = new ChipValues(1, 5, 10, 50, 100, 500, 1_000, 5_000, 10_000, 50_000, 100_000, 500_000, 1_000_000);

    @CollapsibleObject
    public ChipValues relic_coin_chip_values = new ChipValues(1, 1, 1, 1, 1, 1, 1, 5, 10, 50, 100, 500, 1_000);

    public static class ChipValues implements ConfigData {

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

        public ChipValues() {}
        public ChipValues(long redChip, long blueChip, long yellowChip, long purpleChip, long copperChip,
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

    // ===== HELPERS =====

    public long getChipValue(String chipId) {
        return getMoneyChipValue(chipId);
    }

    public long getMoneyChipValue(String chipId) {
        return getValue(money_chip_values, chipId);
    }

    public long getRelicCoinChipValue(String chipId) {
        return getValue(relic_coin_chip_values, chipId);
    }

    private long getValue(ChipValues values, String chipId) {
        if (values == null) return 1;

        chipId = chipId.toLowerCase(Locale.ROOT);
        return switch (chipId) {
            case "red_chip" -> values.red_chip;
            case "blue_chip" -> values.blue_chip;
            case "yellow_chip" -> values.yellow_chip;
            case "purple_chip" -> values.purple_chip;
            case "copper_chip" -> values.copper_chip;
            case "iron_chip" -> values.iron_chip;
            case "emerald_chip" -> values.emerald_chip;
            case "gold_chip" -> values.gold_chip;
            case "diamond_chip" -> values.diamond_chip;
            case "netherite_chip" -> values.netherite_chip;
            case "black_chip" -> values.black_chip;
            case "white_chip" -> values.white_chip;
            case "rainbow_chip" -> values.rainbow_chip;
            default -> 1;
        };
    }

}

