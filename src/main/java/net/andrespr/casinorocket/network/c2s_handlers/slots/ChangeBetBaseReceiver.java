package net.andrespr.casinorocket.network.c2s_handlers.slots;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.slots.ChangeBetBaseC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.UUID;

public class ChangeBetBaseReceiver {

    public static void handle(ChangeBetBaseC2SPayload payload, ServerPlayNetworking.Context ctx) {

        ServerPlayerEntity player = ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUuid();

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
        long balance = storage.getBalance(uuid);
        int lines = storage.getLinesMode(uuid);

        ServerPlayNetworking.send(player, new SendMenuSettingsS2CPayload(balance, newBase, lines));
    }

}