package net.andrespr.casinorocket.network.c2s.slots;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DoSpinC2SPayload() implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "do_spin");
    public static final Type<DoSpinC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, DoSpinC2SPayload> CODEC =
            StreamCodec.unit(new DoSpinC2SPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}

