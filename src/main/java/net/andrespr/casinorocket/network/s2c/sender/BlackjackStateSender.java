package net.andrespr.casinorocket.network.s2c.sender;

import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.data.PlayerCasinoBalanceData;
import net.andrespr.casinorocket.games.blackjack.*;
import net.andrespr.casinorocket.network.s2c.SendBlackjackStateS2CPayload;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;

public final class BlackjackStateSender {

    private BlackjackStateSender() {}

    public static void send(ServerPlayer player, BlockPos pos, PlayerCasinoBalanceData balanceStorage,
                            PlayerBlackjackData storage, BlackjackGameController controller) {

        BlackjackRound round = controller.getRound();
        BlackjackHand playerHand = round.getPlayerHand();
        BlackjackHand dealerHand = round.getDealerHand();

        long balance = balanceStorage.getBalance(player.getUUID());
        int betIndex = storage.getBetIndex(player.getUUID());

        // === CARDS ===
        int[] playerCards = toCardIds(playerHand.cards());

        int[] dealerCards;
        if (round.isDealerHoleHidden()) {
            dealerCards = toDealerCardsWithHiddenHole(dealerHand.cards());
        } else {
            dealerCards = toCardIds(dealerHand.cards());
        }

        // === TEXTS ===
        String playerValueText = playerHand.getValueText();
        String dealerValueText = round.isDealerHoleHidden() ? "?" : dealerHand.getValueText();

        // === BUTTON STATES ===
        boolean canPlay = round.getPhase() == BlackjackPhase.IDLE;
        boolean canHit = round.canHit();
        boolean canStand = round.canStand();
        boolean canDoubleDown = round.canDoubleDown(balance);
        boolean canFinish = round.canFinish();
        boolean canDoubleOrNothing = round.canDoubleOrNothing();

        SendBlackjackStateS2CPayload payload =
                new SendBlackjackStateS2CPayload(
                        pos, "blackjack",
                        balance, betIndex, BlackjackRules.betValuesArray(), round.getCurrentBet(),
                        round.getPhase(), round.getWinPayout(), round.getResultSeq(),
                        controller.getResultId(), controller.getLastResolvedBet(), controller.getLastResolvedPayout(),
                        !round.isDealerHoleHidden(), playerCards, dealerCards,
                        playerValueText, dealerValueText,
                        canPlay, canHit, canStand, canDoubleDown, canFinish, canDoubleOrNothing
                );

        CasinoRocketPackets.sendToPlayer(player, payload);
    }

    // ===== HELPERS =====

    private static int[] toCardIds(List<BlackjackCard> cards) {
        int[] out = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            BlackjackCard c = cards.get(i);
            out[i] = c.j() * 13 + c.i();
        }
        return out;
    }

    private static int[] toDealerCardsWithHiddenHole(List<BlackjackCard> cards) {
        int[] out = new int[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            if (i == 1) {
                out[i] = -1; // hole card
            } else {
                BlackjackCard c = cards.get(i);
                out[i] = c.j() * 13 + c.i();
            }
        }
        return out;
    }

}

