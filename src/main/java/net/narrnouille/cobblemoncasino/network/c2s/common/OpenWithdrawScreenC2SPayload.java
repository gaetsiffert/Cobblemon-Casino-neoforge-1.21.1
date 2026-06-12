package net.narrnouille.cobblemoncasino.network.c2s.common;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenWithdrawScreenC2SPayload() implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "open_withdraw_screen");
    public static final Type<OpenWithdrawScreenC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenWithdrawScreenC2SPayload> CODEC =
            StreamCodec.unit(new OpenWithdrawScreenC2SPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}

