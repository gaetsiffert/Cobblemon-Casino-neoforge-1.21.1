package net.narrnouille.cobblemoncasino.network.c2s.common;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ReturnToMachineScreenC2SPayload(BlockPos pos, String machineKey) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "return_to_machine");
    public static final Type<ReturnToMachineScreenC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, ReturnToMachineScreenC2SPayload> CODEC =
            StreamCodec.ofMember(ReturnToMachineScreenC2SPayload::write, ReturnToMachineScreenC2SPayload::read);

    private static void write(ReturnToMachineScreenC2SPayload p, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(p.pos());
        buf.writeUtf(p.machineKey());
    }

    private static ReturnToMachineScreenC2SPayload read(RegistryFriendlyByteBuf buf) {
        return new ReturnToMachineScreenC2SPayload(buf.readBlockPos(), buf.readUtf());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

}

