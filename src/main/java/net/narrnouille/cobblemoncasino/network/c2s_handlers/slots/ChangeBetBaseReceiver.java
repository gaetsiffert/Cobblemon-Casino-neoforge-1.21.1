package net.narrnouille.cobblemoncasino.network.c2s_handlers.slots;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.narrnouille.cobblemoncasino.data.PlayerSlotMachineData;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.narrnouille.cobblemoncasino.network.c2s.slots.ChangeBetBaseC2SPayload;
import net.narrnouille.cobblemoncasino.network.s2c.SendMenuSettingsS2CPayload;
import net.narrnouille.cobblemoncasino.games.slot.SlotMachineConstants;
import net.narrnouille.cobblemoncasino.network.CobblemonCasinoPackets;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
import java.util.UUID;

public class ChangeBetBaseReceiver {

    public static void handle(ChangeBetBaseC2SPayload payload, IPayloadContext ctx) {

        ServerPlayer player = (ServerPlayer) ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;
        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!"slots".equals(bound.getMachineKey())) return;
        if (!player.containerMenu.stillValid(player)) return;

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUUID();

        List<Integer> bets = SlotMachineConstants.betValues();
        if (bets.isEmpty()) return;

        int index = storage.getBetIndex(uuid);

        if (payload.increase()) {
            if (index < bets.size() - 1) index++;
        } else {
            if (index > 0) index--;
        }

        storage.setBetIndex(uuid, index);

        int newBase = bets.get(index);
        long balance = PlayerCasinoBalanceData.get(server).getBalance(uuid);
        int lines = storage.getLinesMode(uuid);

        CobblemonCasinoPackets.sendToPlayer(player, new SendMenuSettingsS2CPayload(balance, newBase, lines));
    }

}


