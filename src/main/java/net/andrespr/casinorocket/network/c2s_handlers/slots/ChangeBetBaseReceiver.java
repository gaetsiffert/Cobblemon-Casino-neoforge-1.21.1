package net.andrespr.casinorocket.network.c2s_handlers.slots;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.data.PlayerCasinoBalanceData;
import net.andrespr.casinorocket.network.c2s.slots.ChangeBetBaseC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
import java.util.UUID;

public class ChangeBetBaseReceiver {

    public static void handle(ChangeBetBaseC2SPayload payload, IPayloadContext ctx) {

        ServerPlayer player = (ServerPlayer) ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

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

        CasinoRocketPackets.sendToPlayer(player, new SendMenuSettingsS2CPayload(balance, newBase, lines));
    }

}


