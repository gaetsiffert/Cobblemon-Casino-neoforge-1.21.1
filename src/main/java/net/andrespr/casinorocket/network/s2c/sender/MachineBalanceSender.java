package net.andrespr.casinorocket.network.s2c.sender;

import net.andrespr.casinorocket.network.s2c.SendMachineBalanceS2CPayload;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.server.level.ServerPlayer;

public final class MachineBalanceSender {

    private MachineBalanceSender() {}

    public static void send(ServerPlayer player, String machineKey, long amount) {
        CasinoRocketPackets.sendToPlayer(player, new SendMachineBalanceS2CPayload(machineKey, amount));
    }

}

