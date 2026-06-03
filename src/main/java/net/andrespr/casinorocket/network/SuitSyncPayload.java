package net.andrespr.casinorocket.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SuitSyncPayload(int entityId, int suitValue) implements CustomPacketPayload {

    public static final Type<SuitSyncPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath("casinorocket", "sync_suit"));

    public static final StreamCodec<FriendlyByteBuf, SuitSyncPayload> CODEC = StreamCodec.ofMember(
            (payload, buf) -> { // Encoder
                buf.writeInt(payload.entityId());
                buf.writeInt(payload.suitValue());
            },
            buf -> new SuitSyncPayload(buf.readInt(), buf.readInt()) // Decoder
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}

