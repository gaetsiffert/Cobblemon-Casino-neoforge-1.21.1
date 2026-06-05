package net.andrespr.casinorocket.network.c2s_handlers.common;

import net.andrespr.casinorocket.network.c2s.common.OpenWithdrawScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.common.WithdrawScreenHandler;
import net.andrespr.casinorocket.screen.opening.CommonMachineOpenData;
import net.andrespr.casinorocket.screen.opening.MenuDataProvider;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class WithdrawScreenReceiver {

    public static void openWithdrawScreen(OpenWithdrawScreenC2SPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        server.execute(() -> {
            if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
            if (!player.containerMenu.stillValid(player)) return;

            String machineKey = bound.getMachineKey();
            BlockPos pos = bound.getMachinePos();
            CommonMachineOpenData data = new CommonMachineOpenData(pos, machineKey);

            player.openMenu(new MenuDataProvider<>(
                    Component.literal("Withdraw Menu"),
                    data,
                    CommonMachineOpenData.CODEC,
                    (syncId, inv, p, openData) -> new WithdrawScreenHandler(syncId, inv, openData)
            ));
        });
    }
}
