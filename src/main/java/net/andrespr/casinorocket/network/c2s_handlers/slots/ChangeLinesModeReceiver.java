package net.andrespr.casinorocket.network.c2s_handlers.slots;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.data.PlayerCasinoBalanceData;
import net.andrespr.casinorocket.network.c2s.slots.ChangeLinesModeC2SPayload;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.UUID;

public class ChangeLinesModeReceiver {

    public static void handle(ChangeLinesModeC2SPayload packet, IPayloadContext ctx) {

        ServerPlayer player = (ServerPlayer) ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;
        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!"slots".equals(bound.getMachineKey())) return;
        if (!player.containerMenu.stillValid(player)) return;

        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUUID();

        int mode = packet.mode();
        if (mode < 1 || mode > 3) return;

        storage.setLinesMode(uuid, mode);

        long balance = PlayerCasinoBalanceData.get(server).getBalance(uuid);
        int base = storage.getBetBase(uuid);

        CasinoRocketPackets.sendToPlayer(player, new SendMenuSettingsS2CPayload(balance, base, mode));

    }

}


