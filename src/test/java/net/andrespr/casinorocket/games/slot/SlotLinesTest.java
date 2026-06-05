package net.andrespr.casinorocket.games.slot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SlotLinesTest {

    @Test
    void modeOnePaysMiddleLineTripleOnly() {
        SlotSymbol[][] matrix = {
                {SlotSymbol.SEVEN, SlotSymbol.ROCKET, SlotSymbol.MEW},
                {SlotSymbol.PIKACHU, SlotSymbol.PIKACHU, SlotSymbol.PIKACHU},
                {SlotSymbol.BULBASAUR, SlotSymbol.SQUIRTLE, SlotSymbol.CHARMANDER}
        };

        SlotSpinResult result = SlotLines.evaluateSpin(matrix, 10, 1);

        assertEquals(200L, result.totalWin());
        assertEquals(1, result.lines().size());
        assertTrue(result.lines().getFirst().win());
    }

    @Test
    void cherriesPayLeftToRightPartialMatches() {
        SlotSymbol[][] matrix = {
                {SlotSymbol.HAUNTER, SlotSymbol.HAUNTER, SlotSymbol.HAUNTER},
                {SlotSymbol.CHERRY, SlotSymbol.CHERRY, SlotSymbol.SEVEN},
                {SlotSymbol.HAUNTER, SlotSymbol.HAUNTER, SlotSymbol.HAUNTER}
        };

        SlotSpinResult result = SlotLines.evaluateSpin(matrix, 10, 1);

        assertEquals(30L, result.totalWin());
        assertEquals(2, result.lines().getFirst().count());
    }

    @Test
    void modeThreeEvaluatesFiveLines() {
        SlotSymbol[][] matrix = {
                {SlotSymbol.SEVEN, SlotSymbol.SEVEN, SlotSymbol.SEVEN},
                {SlotSymbol.ROCKET, SlotSymbol.ROCKET, SlotSymbol.ROCKET},
                {SlotSymbol.MEW, SlotSymbol.MEW, SlotSymbol.MEW}
        };

        SlotSpinResult result = SlotLines.evaluateSpin(matrix, 1, 3);

        assertEquals(650L, result.totalWin());
        assertEquals(5, result.lines().size());
    }
}
