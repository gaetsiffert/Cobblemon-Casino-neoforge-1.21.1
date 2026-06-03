package net.andrespr.casinorocket.network.c2s_handlers.common;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.blackjack.BlackjackWithdrawLogic;
import net.andrespr.casinorocket.games.slot.SlotsWithdrawLogic;
import net.andrespr.casinorocket.network.c2s.common.DoWithdrawC2SPayload;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class DoWithdrawReceiver {

    public static void handle(DoWithdrawC2SPayload packet, IPayloadContext ctx) {

        ServerPlayer player = (ServerPlayer) ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!packet.pos().equals(bound.getMachinePos())) return;
        if (!packet.machineKey().equals(bound.getMachineKey())) return;

        switch (packet.machineKey()) {
            case "slots" -> SlotsWithdrawLogic.handle(player, server);
            case "blackjack" -> BlackjackWithdrawLogic.handle(player, server);
            default -> CasinoRocket.LOGGER.warn("[Withdraw] Unknown machineKey={} from {}",
                    packet.machineKey(), player.getGameProfile().getName());
        }
    }

}


