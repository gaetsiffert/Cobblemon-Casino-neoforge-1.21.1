package net.andrespr.casinorocket.games.blackjack;

import net.andrespr.casinorocket.CasinoRocket;

import java.util.List;

public final class BlackjackRules {

    private BlackjackRules() {}

    public static final boolean BLACKJACK_ONLY_ACE_PLUS_TEN = true;
    public static final boolean DEALER_STANDS_ON_SOFT_17 = true;
    public static final boolean DEALER_BLACKJACK_IS_STRONGEST = true;
    public static final int NORMAL_WIN_MULTIPLIER = 2;
    public static final int BLACKJACK_WIN_MULTIPLIER = 3;
    public static final int PUSH_MULTIPLIER = 1;
    public static final int LOSE_MULTIPLIER = 0;
    public static final boolean DOUBLE_DOWN_ALWAYS_ALLOWED = true;

    public static final List<Long> FALLBACK_BET_VALUES =
            List.of(1L, 5L, 10L, 50L, 100L, 500L, 1_000L, 5_000L, 10_000L, 50_000L, 100_000L, 500_000L, 1_000_000L);

    public static final int DEFAULT_BET_INDEX = 0;

    public static List<Long> betValues() {
        try {
            List<Long> values = CasinoRocket.CONFIG.blackjackTable.bet_amounts;
            return values == null || values.isEmpty() ? FALLBACK_BET_VALUES : values;
        } catch (RuntimeException exception) {
            return FALLBACK_BET_VALUES;
        }
    }

    public static long betAmount(int index) {
        List<Long> values = betValues();
        return values.get(clampBetIndex(index));
    }

    public static int maxBetIndex() {
        return Math.max(0, betValues().size() - 1);
    }

    public static int clampBetIndex(int index) {
        return Math.max(0, Math.min(index, maxBetIndex()));
    }

    public static long[] betValuesArray() {
        List<Long> values = betValues();
        long[] array = new long[values.size()];
        for (int i = 0; i < values.size(); i++) {
            array[i] = values.get(i);
        }
        return array;
    }

}

