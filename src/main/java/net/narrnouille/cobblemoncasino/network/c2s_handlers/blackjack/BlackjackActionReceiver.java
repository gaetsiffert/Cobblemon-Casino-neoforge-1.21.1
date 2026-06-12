package net.narrnouille.cobblemoncasino.network.c2s_handlers.blackjack;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.block.entity.custom.BlackjackTableEntity;
import net.narrnouille.cobblemoncasino.data.PlayerBlackjackData;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.narrnouille.cobblemoncasino.games.blackjack.BlackjackAction;
import net.narrnouille.cobblemoncasino.games.blackjack.BlackjackGameController;
import net.narrnouille.cobblemoncasino.network.c2s.blackjack.BlackjackActionC2SPayload;
import net.narrnouille.cobblemoncasino.network.s2c.sender.BlackjackStateSender;
import net.narrnouille.cobblemoncasino.util.CobblemonCasinoLogger;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class BlackjackActionReceiver {

    private BlackjackActionReceiver() {}

    public static void handle(BlackjackActionC2SPayload payload, IPayloadContext ctx) {

        ServerPlayer player = (ServerPlayer) ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;

        if (!payload.pos().equals(bound.getMachinePos())) return;
        if (!payload.machineKey().equals(bound.getMachineKey())) return;
        if (!player.containerMenu.stillValid(player)) return;

        if (!(payload.machineKey()).equals("blackjack")) {
            CobblemonCasino.LOGGER.warn("[BlackjackAction] Wrong machineKey={} from {}", payload.machineKey(), player.getGameProfile().getName());
            return;
        }

        Level world = player.level();
        BlockPos pos = payload.pos();

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        if (!world.getChunkSource().hasChunk(chunkX, chunkZ)) return;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof BlackjackTableEntity table)) return;

        if (table.isInUse() && !table.isUsedBy(player)) {
            CobblemonCasinoLogger.toPlayerTranslated(player, "message.cobblemoncasino.blackjack_table_occupied", true);
            return;
        }

        if (!table.tryLock(player)) return;

        BlackjackGameController controller = table.getOrCreateController(player);
        BlackjackAction action = payload.action();
        controller.handleAction(player, action);

        PlayerCasinoBalanceData balanceStorage = PlayerCasinoBalanceData.get(server);
        PlayerBlackjackData storage = PlayerBlackjackData.get(server);
        BlackjackStateSender.send(player, table.getBlockPos(), balanceStorage, storage, controller);

    }

}


