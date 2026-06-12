package net.narrnouille.cobblemoncasino.network.c2s.common;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DoWithdrawC2SPayload(String machineKey, BlockPos pos) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "do_withdraw");
    public static final Type<DoWithdrawC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, DoWithdrawC2SPayload> CODEC =
            StreamCodec.ofMember(DoWithdrawC2SPayload::write, DoWithdrawC2SPayload::read);

    private static void write(DoWithdrawC2SPayload p, RegistryFriendlyByteBuf buf) {
        buf.writeUtf(p.machineKey());
        buf.writeBlockPos(p.pos());
    }

    private static DoWithdrawC2SPayload read(RegistryFriendlyByteBuf buf) {
        String key = buf.readUtf();
        BlockPos pos = buf.readBlockPos();
        return new DoWithdrawC2SPayload(key, pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

}

