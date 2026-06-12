package net.narrnouille.cobblemoncasino.network.c2s_handlers.common;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.games.blackjack.BlackjackBetLogic;
import net.narrnouille.cobblemoncasino.games.slot.SlotsBetLogic;
import net.narrnouille.cobblemoncasino.network.c2s.common.DoBetC2SPayload;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
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
        if (!player.containerMenu.stillValid(player)) return;

        switch (payload.machineKey()) {
            case "slots" -> SlotsBetLogic.handle(player, server);
            case "blackjack" -> BlackjackBetLogic.handle(player, server);
            default -> CobblemonCasino.LOGGER.warn("[Bet] Unknown machineKey={} from {}",
                    payload.machineKey(), player.getGameProfile().getName());
        }
    }

}


