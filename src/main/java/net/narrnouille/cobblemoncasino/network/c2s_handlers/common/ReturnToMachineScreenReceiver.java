package net.narrnouille.cobblemoncasino.network.c2s_handlers.common;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.block.entity.custom.BlackjackTableEntity;
import net.narrnouille.cobblemoncasino.block.entity.custom.SlotMachineEntity;
import net.narrnouille.cobblemoncasino.network.c2s.common.ReturnToMachineScreenC2SPayload;
import net.narrnouille.cobblemoncasino.util.CobblemonCasinoLogger;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ReturnToMachineScreenReceiver {

    public static void handle(ReturnToMachineScreenC2SPayload payload, IPayloadContext ctx) {

        ServerPlayer player = (ServerPlayer) ctx.player();
        Level world = player.level();

        if (!(player.containerMenu instanceof IMachineBoundHandler bound)) return;
        if (!payload.pos().equals(bound.getMachinePos())) return;
        if (!payload.machineKey().equals(bound.getMachineKey())) return;
        if (!player.containerMenu.stillValid(player)) return;

        BlockPos pos = payload.pos();

        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        if (!world.getChunkSource().hasChunk(chunkX, chunkZ)) return;

        BlockEntity be = world.getBlockEntity(pos);
        if (be == null) return;

        switch (payload.machineKey()) {

            case "slots" -> {
                if (!(be instanceof SlotMachineEntity slot)) return;

                if (slot.isInUse() && !slot.isUsedBy(player)) {
                    CobblemonCasinoLogger.toPlayerTranslated(player, "message.cobblemoncasino.slot_machine_occupied", true);
                    return;
                }
                if (!slot.tryLock(player)) return;

                player.openMenu(slot);
            }

            case "blackjack" -> {
                if (!(be instanceof BlackjackTableEntity table)) return;

                if (table.isInUse() && !table.isUsedBy(player)) {
                    CobblemonCasinoLogger.toPlayerTranslated(player, "message.cobblemoncasino.blackjack_table_occupied", true);
                    return;
                }
                if (!table.tryLock(player)) return;

                player.openMenu(table);
            }

            default -> CobblemonCasino.LOGGER.warn("[ReturnToMachine] Unknown machineKey={} from {}",
                    payload.machineKey(), player.getGameProfile().getName());

        }

    }

}


