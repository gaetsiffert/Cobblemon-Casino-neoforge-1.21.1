package net.andrespr.casinorocket.data;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CasinoDataOverflowTest {

    @Test
    void casinoBalanceSaturatesPositiveOverflowAndClampsNegative() {
        PlayerCasinoBalanceData data = new PlayerCasinoBalanceData();
        UUID player = UUID.randomUUID();

        data.setBalance(player, Long.MAX_VALUE);
        data.addBalance(player, 1L);

        assertEquals(Long.MAX_VALUE, data.getBalance(player));

        data.addBalance(player, Long.MIN_VALUE);

        assertEquals(0L, data.getBalance(player));
    }

    @Test
    void slotLedgerStatsSaturatePositiveOverflow() {
        PlayerSlotMachineData data = new PlayerSlotMachineData();
        UUID player = UUID.randomUUID();

        data.addTotalWon(player, Long.MAX_VALUE);
        data.addTotalWon(player, 1L);
        data.addTotalSpent(player, Long.MAX_VALUE);
        data.addTotalSpent(player, 1L);

        assertEquals(Long.MAX_VALUE, data.getTotalWon(player));
        assertEquals(Long.MAX_VALUE, data.getTotalSpent(player));
    }

    @Test
    void slotTotalLostIsPositiveLossOnly() {
        PlayerSlotMachineData data = new PlayerSlotMachineData();
        UUID losingPlayer = UUID.randomUUID();
        UUID winningPlayer = UUID.randomUUID();

        data.addTotalSpent(losingPlayer, 100L);
        data.addTotalWon(losingPlayer, 25L);
        data.addTotalSpent(winningPlayer, 25L);
        data.addTotalWon(winningPlayer, 100L);

        assertEquals(75L, data.getTotalLost(losingPlayer));
        assertEquals(0L, data.getTotalLost(winningPlayer));
    }

    @Test
    void blackjackLedgerStatsSaturatePositiveOverflow() {
        PlayerBlackjackData data = new PlayerBlackjackData();
        UUID player = UUID.randomUUID();

        data.addTotalDeposited(player, Long.MAX_VALUE);
        data.addTotalDeposited(player, 1L);
        data.addTotalWon(player, Long.MAX_VALUE);
        data.addTotalWon(player, 1L);

        assertEquals(Long.MAX_VALUE, data.getTotalDeposited(player));
        assertEquals(Long.MAX_VALUE, data.getTotalWon(player));
    }

    @Test
    void blackjackTotalLostIsPositiveLossOnly() {
        PlayerBlackjackData data = new PlayerBlackjackData();
        UUID losingPlayer = UUID.randomUUID();
        UUID winningPlayer = UUID.randomUUID();

        data.addTotalSpent(losingPlayer, 100L);
        data.addTotalWon(losingPlayer, 25L);
        data.addTotalSpent(winningPlayer, 25L);
        data.addTotalWon(winningPlayer, 100L);

        assertEquals(75L, data.getTotalLost(losingPlayer));
        assertEquals(0L, data.getTotalLost(winningPlayer));
    }
}
