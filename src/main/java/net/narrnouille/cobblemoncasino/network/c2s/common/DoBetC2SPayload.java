package net.narrnouille.cobblemoncasino.network.c2s.common;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DoBetC2SPayload(String machineKey, BlockPos pos) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "do_bet");
    public static final Type<DoBetC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, DoBetC2SPayload> CODEC =
            StreamCodec.ofMember(DoBetC2SPayload::write, DoBetC2SPayload::read);

    private static void write(DoBetC2SPayload p, RegistryFriendlyByteBuf buf) {
        buf.writeUtf(p.machineKey());
        buf.writeBlockPos(p.pos());
    }

    private static DoBetC2SPayload read(RegistryFriendlyByteBuf buf) {
        return new DoBetC2SPayload(buf.readUtf(), buf.readBlockPos());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

}

