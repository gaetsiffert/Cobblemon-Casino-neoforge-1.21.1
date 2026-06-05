package net.andrespr.casinorocket.network.c2s_handlers.common;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.data.PlayerCasinoBalanceData;
import net.andrespr.casinorocket.data.PlayerSlotMachineData;
import net.andrespr.casinorocket.network.c2s.common.OpenMenuScreenC2SPayload;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreenHandler;
import net.andrespr.casinorocket.screen.opening.MenuDataProvider;
import net.andrespr.casinorocket.screen.opening.SlotMachineOpenData;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import java.util.UUID;

public class MenuScreenReceiver {

    public static void openMenuScreen(OpenMenuScreenC2SPayload payload, IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!player.containerMenu.stillValid(player)) return;

        String key = bound.getMachineKey();
        BlockPos pos = bound.getMachinePos();

        switch (key) {
            case "slots" -> {
                PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
                UUID uuid = player.getUUID();

                long balance = PlayerCasinoBalanceData.get(server).getBalance(uuid);
                int betBase = storage.getBetBase(uuid);
                int lines = storage.getLinesMode(uuid);

                SlotMachineOpenData data = new SlotMachineOpenData(pos, key, balance, betBase, lines);
                player.openMenu(new MenuDataProvider<>(
                        Component.literal("Slot Machine Menu"),
                        data,
                        SlotMachineOpenData.CODEC,
                        (syncId, inv, p, openData) -> new SlotMachineMenuScreenHandler(syncId, inv, openData)
                ));
            }
            case "blackjack" -> player.displayClientMessage(Component.literal("This machine has no menu."), true);
            default -> CasinoRocket.LOGGER.warn("[MenuOpen] Unknown machineKey={} from {}", key, player.getGameProfile().getName());
        }
    }
}
