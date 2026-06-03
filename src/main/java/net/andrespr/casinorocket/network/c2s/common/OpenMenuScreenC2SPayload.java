package net.andrespr.casinorocket.network.c2s.common;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record OpenMenuScreenC2SPayload() implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "open_menu_screen");
    public static final Type<OpenMenuScreenC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenMenuScreenC2SPayload> CODEC =
            StreamCodec.unit(new OpenMenuScreenC2SPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}

