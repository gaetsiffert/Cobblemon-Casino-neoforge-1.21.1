package net.narrnouille.cobblemoncasino.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyCalculatorTest {

    @Test
    void safeAddSaturatesAtConfiguredMaximum() {
        assertEquals(100L, MoneyCalculator.safeAdd(95L, 10L, 100L));
    }

    @Test
    void safeAddKeepsNormalAdditions() {
        assertEquals(42L, MoneyCalculator.safeAdd(40L, 2L, Long.MAX_VALUE));
    }

    @Test
    void safeAddSaturatesNegativeUnderflow() {
        assertEquals(Long.MIN_VALUE, MoneyCalculator.safeAdd(Long.MIN_VALUE, -1L, Long.MAX_VALUE));
    }

    @Test
    void relicItemAmountUsesSmallestCoveringDenomination() {
        MoneyCalculator.ItemResult result = MoneyCalculator.calculateItemAmount("cobblemon:relic_coin", 82L);

        assertEquals("cobblemoncasino:handful_of_relic_coins", result.item());
        assertEquals(21L, result.amount());
    }

    @Test
    void relicItemAmountFallsBackToLargestStackWhenValueIsTooLarge() {
        MoneyCalculator.ItemResult result = MoneyCalculator.calculateItemAmount("cobblemon:relic_coin", 10_000L);

        assertEquals("cobblemon:relic_coin_sack", result.item());
        assertEquals(64L, result.amount());
    }
}
