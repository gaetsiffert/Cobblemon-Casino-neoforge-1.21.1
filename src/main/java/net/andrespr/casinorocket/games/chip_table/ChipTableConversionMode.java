package net.andrespr.casinorocket.games.chip_table;

public enum ChipTableConversionMode {
    CURRENCY_TO_CHIPS,
    CHIPS_TO_RELIC_COINS;

    public static ChipTableConversionMode byOrdinal(int ordinal) {
        ChipTableConversionMode[] values = values();
        if (ordinal < 0 || ordinal >= values.length) {
            return CURRENCY_TO_CHIPS;
        }
        return values[ordinal];
    }
}
