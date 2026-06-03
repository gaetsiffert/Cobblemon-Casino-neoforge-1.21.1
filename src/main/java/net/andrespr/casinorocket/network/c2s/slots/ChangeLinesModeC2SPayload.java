package net.andrespr.casinorocket.network.c2s.slots;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChangeLinesModeC2SPayload(int mode) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "change_lines_mode");
    public static final Type<ChangeLinesModeC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeLinesModeC2SPayload> CODEC =
            StreamCodec.ofMember(ChangeLinesModeC2SPayload::write, ChangeLinesModeC2SPayload::read);

    private static void write(ChangeLinesModeC2SPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeInt(payload.mode());
    }

    private static ChangeLinesModeC2SPayload read(RegistryFriendlyByteBuf buf) {
        return new ChangeLinesModeC2SPayload(buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

}

