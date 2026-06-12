package net.narrnouille.cobblemoncasino.network.c2s_handlers.common;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.narrnouille.cobblemoncasino.data.PlayerSlotMachineData;
import net.narrnouille.cobblemoncasino.network.c2s.common.OpenMenuScreenC2SPayload;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineMenuScreenHandler;
import net.narrnouille.cobblemoncasino.screen.opening.MenuDataProvider;
import net.narrnouille.cobblemoncasino.screen.opening.SlotMachineOpenData;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
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
                        Component.translatable("gui.cobblemoncasino.slot_machine_menu"),
                        data,
                        SlotMachineOpenData.CODEC,
                        (syncId, inv, p, openData) -> new SlotMachineMenuScreenHandler(syncId, inv, openData)
                ));
            }
            case "blackjack" -> player.displayClientMessage(Component.translatable("message.cobblemoncasino.machine_has_no_menu"), true);
            default -> CobblemonCasino.LOGGER.warn("[MenuOpen] Unknown machineKey={} from {}", key, player.getGameProfile().getName());
        }
    }
}
