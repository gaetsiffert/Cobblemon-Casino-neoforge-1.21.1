package net.andrespr.casinorocket.network.c2s_handlers.common;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.blackjack.BlackjackBetLogic;
import net.andrespr.casinorocket.games.slot.SlotsBetLogic;
import net.andrespr.casinorocket.network.c2s.common.DoBetC2SPayload;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class DoBetReceiver {

    public static void handle(DoBetC2SPayload payload, IPayloadContext context) {

        ServerPlayer player = (ServerPlayer) context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!payload.pos().equals(bound.getMachinePos())) return;
        if (!payload.machineKey().equals(bound.getMachineKey())) return;

        switch (payload.machineKey()) {
            case "slots" -> SlotsBetLogic.handle(player, server);
            case "blackjack" -> BlackjackBetLogic.handle(player, server);
            default -> CasinoRocket.LOGGER.warn("[Bet] Unknown machineKey={} from {}",
                    payload.machineKey(), player.getGameProfile().getName());
        }
    }

}


