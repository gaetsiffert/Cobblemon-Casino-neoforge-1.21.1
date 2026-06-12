package net.narrnouille.cobblemoncasino.network.c2s_handlers.chip_table;

import net.narrnouille.cobblemoncasino.network.c2s.chip_table.ChipTableConvertC2SPayload;
import net.narrnouille.cobblemoncasino.screen.custom.chip_table.ChipTableScreenHandler;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ChipTableConvertReceiver {

    private ChipTableConvertReceiver() {}

    public static void handle(ChipTableConvertC2SPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        if (!(player.containerMenu instanceof ChipTableScreenHandler handler)) return;
        if (handler.isWalletMode()) return;
        if (!payload.pos().equals(handler.getTablePos())) return;
        if (!handler.stillValid(player)) return;

        handler.convert(player, payload.mode(), payload.cobbledollarAmount());
    }

}
