package net.narrnouille.cobblemoncasino.network.s2c.sender;

import net.narrnouille.cobblemoncasino.network.s2c.SendMachineBalanceS2CPayload;
import net.narrnouille.cobblemoncasino.network.CobblemonCasinoPackets;
import net.minecraft.server.level.ServerPlayer;

public final class MachineBalanceSender {

    private MachineBalanceSender() {}

    public static void send(ServerPlayer player, String machineKey, long amount) {
        CobblemonCasinoPackets.sendToPlayer(player, new SendMachineBalanceS2CPayload(machineKey, amount));
    }

}

