package net.andrespr.casinorocket.network.c2s_handlers.blackjack;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.andrespr.casinorocket.block.entity.custom.BlackjackTableEntity;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.data.PlayerCasinoBalanceData;
import net.andrespr.casinorocket.games.blackjack.BlackjackGameController;
import net.andrespr.casinorocket.games.blackjack.BlackjackPhase;
import net.andrespr.casinorocket.network.c2s.blackjack.ChangeBlackjackBetIndexC2SPayload;
import net.andrespr.casinorocket.network.s2c.sender.BlackjackStateSender;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ChangeBlackjackBetIndexReceiver {

    private ChangeBlackjackBetIndexReceiver() {}

    public static void handle(ChangeBlackjackBetIndexC2SPayload payload, IPayloadContext ctx) {

        ServerPlayer player = (ServerPlayer) ctx.player();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!"blackjack".equals(bound.getMachineKey())) return;
        if (!player.containerMenu.stillValid(player)) return;

        Level world = player.level();
        BlockPos pos = bound.getMachinePos();

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof BlackjackTableEntity table)) return;

        BlackjackGameController controller = table.getOrCreateController(player);

        if (controller.getRound().getPhase() != BlackjackPhase.IDLE) return;

        PlayerBlackjackData storage = PlayerBlackjackData.get(server);

        if (payload.increase()) {
            storage.incrementBetIndex(player.getUUID());
        } else {
            storage.decrementBetIndex(player.getUUID());
        }

        BlackjackStateSender.send(player, pos, PlayerCasinoBalanceData.get(server), storage, controller);

    }

}


