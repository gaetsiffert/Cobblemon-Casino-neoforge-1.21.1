package net.narrnouille.cobblemoncasino.network.c2s_handlers.common;

import net.narrnouille.cobblemoncasino.network.c2s.common.OpenBetScreenC2SPayload;
import net.narrnouille.cobblemoncasino.screen.custom.common.BetScreenHandler;
import net.narrnouille.cobblemoncasino.screen.opening.CommonMachineOpenData;
import net.narrnouille.cobblemoncasino.screen.opening.MenuDataProvider;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class BetScreenReceiver {

    public static void openBetScreen(OpenBetScreenC2SPayload payload, @NotNull IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!player.containerMenu.stillValid(player)) return;

        String machineKey = bound.getMachineKey();
        BlockPos pos = bound.getMachinePos();
        CommonMachineOpenData data = new CommonMachineOpenData(pos, machineKey);

        player.openMenu(new MenuDataProvider<>(
                Component.translatable("gui.cobblemoncasino.bet_menu"),
                data,
                CommonMachineOpenData.CODEC,
                (syncId, inv, p, openData) -> new BetScreenHandler(syncId, inv, openData)
        ));
    }
}
