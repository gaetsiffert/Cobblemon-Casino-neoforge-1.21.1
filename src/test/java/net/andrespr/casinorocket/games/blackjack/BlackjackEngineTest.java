package net.andrespr.casinorocket.games.blackjack;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlackjackEngineTest {

    @Test
    void startRoundImmediatelyResolvesPlayerNaturalBlackjack() {
        BlackjackRound round = new BlackjackRound();
        BlackjackDeck deck = new BlackjackDeck(List.of(
                card(BlackjackRank.ACE),
                card(BlackjackRank.NINE),
                card(BlackjackRank.KING),
                card(BlackjackRank.SEVEN)
        ));

        BlackjackEngine.EngineResult result = BlackjackEngine.startRound(round, deck, 10L);

        assertTrue(result.roundEnded());
        assertEquals(BlackjackPhase.RESULT_PENDING, round.getPhase());
        assertEquals(30L, result.payout());
        assertEquals(30L, round.getWinPayout());
        assertFalse(round.isDealerHoleHidden());
    }

    @Test
    void startRoundImmediatelyResolvesDealerNaturalBlackjackAsLoss() {
        BlackjackRound round = new BlackjackRound();
        BlackjackDeck deck = new BlackjackDeck(List.of(
                card(BlackjackRank.NINE),
                card(BlackjackRank.ACE),
                card(BlackjackRank.SEVEN),
                card(BlackjackRank.QUEEN)
        ));

        BlackjackEngine.EngineResult result = BlackjackEngine.startRound(round, deck, 10L);

        assertTrue(result.roundEnded());
        assertEquals(BlackjackPhase.IDLE, round.getPhase());
        assertEquals(0L, result.payout());
        assertEquals(0L, round.getWinPayout());
        assertFalse(round.isDealerHoleHidden());
    }

    private static BlackjackCard card(BlackjackRank rank) {
        return new BlackjackCard(rank, BlackjackSuit.SPADES);
    }
}
