package net.narrnouille.cobblemoncasino.network.c2s_handlers.slots;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.narrnouille.cobblemoncasino.data.PlayerSlotMachineData;
import net.narrnouille.cobblemoncasino.games.slot.SlotMachineConstants;
import net.narrnouille.cobblemoncasino.games.slot.SlotSpinEngine;
import net.narrnouille.cobblemoncasino.games.slot.SlotSpinResult;
import net.narrnouille.cobblemoncasino.games.slot.SlotSymbol;
import net.narrnouille.cobblemoncasino.network.c2s.slots.DoSpinC2SPayload;
import net.narrnouille.cobblemoncasino.network.s2c.SendSpinResultS2CPayload;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineScreenHandler;
import net.narrnouille.cobblemoncasino.sound.SlotJackpotSoundScheduler;
import net.narrnouille.cobblemoncasino.util.MoneyCalculator;
import net.narrnouille.cobblemoncasino.network.CobblemonCasinoPackets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

public class DoSpinReceiver {

    private static final int JACKPOT_SOUND_DELAY_TICKS = 54;

    public static void handle(DoSpinC2SPayload payload, IPayloadContext context) {

        ServerPlayer player = (ServerPlayer) context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.containerMenu instanceof SlotMachineScreenHandler handler)) {
            return;
        }
        if (!player.containerMenu.stillValid(player)) return;

        PlayerCasinoBalanceData balanceStorage = PlayerCasinoBalanceData.get(server);
        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUUID();

        long balance = balanceStorage.getBalance(uuid);
        int betBase = storage.getBetBase(uuid);
        int linesMode = storage.getLinesMode(uuid);

        if (betBase <= 0 || linesMode <= 0) return;

        int betMultiplier = SlotMachineConstants.getBetMultiplierForMode(linesMode);
        long cost = (long) betBase * betMultiplier;

        if (balance < cost) return;
        storage.addTotalSpent(uuid, cost);

        long afterCost = balance - cost;

        SlotSpinEngine.SpinOutcome outcome = SlotSpinEngine.spinOutcome(betBase, linesMode);
        SlotSpinEngine.SpinStop stop = outcome.stop();
        SlotSpinResult spinResult = outcome.result();

        if (isJackpot(spinResult)) {
            SlotJackpotSoundScheduler.schedule(player.serverLevel(), handler.getMachinePos(), JACKPOT_SOUND_DELAY_TICKS);
        }

        int stop1 = stop.index1();
        int stop2 = stop.index2();
        int stop3 = stop.index3();

        long spinWin = spinResult.totalWin();
        storage.addTotalWon(uuid, spinWin);
        storage.setLastWin(uuid, spinWin);
        storage.updateHighestWin(uuid, spinWin);

        long newBalance = MoneyCalculator.safeAdd(afterCost, spinWin, SlotMachineConstants.MAX_BALANCE);
        balanceStorage.setBalance(uuid, newBalance);

        if (CobblemonCasino.CONFIG.slotMachine.debug) {
            CobblemonCasino.LOGGER.debug("[SlotDebug] {} stops=({}, {}, {}) mid=[{}, {}, {}] win={}",
                    player.getName().getString(),
                    stop1, stop2, stop3,
                    spinResult.matrix()[1][0], spinResult.matrix()[1][1], spinResult.matrix()[1][2],
                    spinResult.totalWin()
            );
        }

        CobblemonCasinoPackets.sendToPlayer(player,
                SendSpinResultS2CPayload.from(newBalance, linesMode, stop1, stop2, stop3, spinResult)
        );
    }

    private static boolean isJackpot(SlotSpinResult result) {
        return result.lines().stream()
                .anyMatch(line -> line.win() && line.symbol() == SlotSymbol.SEVEN);
    }

}


