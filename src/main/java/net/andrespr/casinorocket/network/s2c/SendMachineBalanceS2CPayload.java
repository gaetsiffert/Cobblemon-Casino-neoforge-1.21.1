package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SendMachineBalanceS2CPayload(String machineKey, long amount) implements CustomPacketPayload {

    public static final Type<SendMachineBalanceS2CPayload> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "machine_balance"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendMachineBalanceS2CPayload> CODEC =
            StreamCodec.ofMember(SendMachineBalanceS2CPayload::write,
                    SendMachineBalanceS2CPayload::read);

    private static void write(SendMachineBalanceS2CPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeUtf(payload.machineKey());
        buf.writeLong(payload.amount());
    }

    private static SendMachineBalanceS2CPayload read(RegistryFriendlyByteBuf buf) {
        String machineKey = buf.readUtf();
        long amount = buf.readLong();
        return new SendMachineBalanceS2CPayload(machineKey, amount);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}

