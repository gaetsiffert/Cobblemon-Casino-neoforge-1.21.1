package net.narrnouille.cobblemoncasino.games.blackjack;

import net.narrnouille.cobblemoncasino.data.PlayerBlackjackData;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

public final class BlackjackGameController {

    private final UUID playerId;
    private final PlayerCasinoBalanceData balanceStorage;
    private final PlayerBlackjackData blackjackStorage;

    private final BlackjackDeck deck = new BlackjackDeck();
    private final BlackjackRound round = new BlackjackRound();

    private int resultId = 0;
    private long lastResolvedBet = 0L;
    private long lastResolvedPayout = 0L;

    public BlackjackGameController(UUID playerId, PlayerCasinoBalanceData balanceStorage, PlayerBlackjackData blackjackStorage) {
        this.playerId = playerId;
        this.balanceStorage = balanceStorage;
        this.blackjackStorage = blackjackStorage;
    }

    // === PLAY ===
    public boolean play() {
        long bet = blackjackStorage.getBetAmount(playerId);
        long balance = balanceStorage.getBalance(playerId);

        if (balance < bet) return false;

        balanceStorage.setBalance(playerId, balance - bet);
        blackjackStorage.addTotalSpent(playerId, bet);

        BlackjackEngine.EngineResult res = BlackjackEngine.startRound(round, deck, bet);
        if (res.roundEnded()) {
            resolveRound(bet, res.payout());
        }
        return true;
    }

    // === GAME ACTIONS ===
    public void hit() {
        long betBefore = round.getCurrentBet();
        BlackjackEngine.EngineResult res = BlackjackEngine.playerHit(round, deck);
        if (res.roundEnded()) {
            resolveRound(betBefore, res.payout());
        }
    }

    public void stand() {
        long betBefore = round.getCurrentBet();

        BlackjackEngine.EngineResult res = BlackjackEngine.playerStand(round, deck);
        if (res.roundEnded()) {
            resolveRound(betBefore, res.payout());
        }
    }

    public boolean doubleDown() {
        long balance = balanceStorage.getBalance(playerId);
        long betBefore = round.getCurrentBet();

        BlackjackEngine.EngineResult res =
                BlackjackEngine.playerDoubleDown(round, deck, balance);

        if (res.extraCostToCharge() > 0) {
            balanceStorage.setBalance(playerId, balance - res.extraCostToCharge());
            blackjackStorage.addTotalSpent(playerId, res.extraCostToCharge());
        }

        if (res.roundEnded()) {
            resolveRound(betBefore + Math.max(0L, res.extraCostToCharge()), res.payout());
        }

        return res.extraCostToCharge() > 0;
    }

    // === FINISH ===
    public void finish() {
        if (!round.canFinish()) return;

        round.setPhase(BlackjackPhase.IDLE);
    }

    // === DOUBLE OR NOTHING ===
    public boolean doubleOrNothing() {
        if (!round.canDoubleOrNothing()) return false;

        long newBet = round.getWinPayout();
        if (newBet <= 0) return false;

        long balance = balanceStorage.getBalance(playerId);
        if (balance < newBet) return false;

        balanceStorage.setBalance(playerId, balance - newBet);
        blackjackStorage.addTotalSpent(playerId, newBet);
        BlackjackEngine.EngineResult res = BlackjackEngine.startRound(round, deck, newBet);
        if (res.roundEnded()) {
            resolveRound(newBet, res.payout());
        }
        return true;
    }

    // === DECIDE ACTION ===
    public void handleAction(ServerPlayer player, BlackjackAction action) {
        switch (action) {
            case PLAY -> play();
            case HIT -> hit();
            case STAND -> stand();
            case DOUBLE_DOWN -> doubleDown();
            case FINISH -> finish();
            case DOUBLE_OR_NOTHING -> doubleOrNothing();
        }
    }

    // === GETTERS ===
    public BlackjackRound getRound() { return round; }
    public int getResultId() { return resultId; }
    public long getLastResolvedBet() { return lastResolvedBet; }
    public long getLastResolvedPayout() { return lastResolvedPayout; }

    // === SETTERS ===
    private void resolveRound(long bet, long payout) {
        long safePayout = Math.max(0L, payout);
        if (safePayout > 0) {
            long balance = balanceStorage.getBalance(playerId);
            balanceStorage.setBalance(playerId, safeAdd(balance, safePayout));
            blackjackStorage.addTotalWon(playerId, safePayout);
            blackjackStorage.updateHighestWin(playerId, safePayout);
        }
        blackjackStorage.setLastWin(playerId, safePayout);
        setResolvedResult(bet, safePayout);
    }

    private void setResolvedResult(long bet, long payout) {
        if (bet <= 0) return;
        this.resultId++;
        this.lastResolvedBet = Math.max(0L, bet);
        this.lastResolvedPayout = Math.max(0L, payout);
    }

    private static long safeAdd(long a, long b) {
        if (b > 0 && a > Long.MAX_VALUE - b) {
            return Long.MAX_VALUE;
        }
        return a + b;
    }

}

