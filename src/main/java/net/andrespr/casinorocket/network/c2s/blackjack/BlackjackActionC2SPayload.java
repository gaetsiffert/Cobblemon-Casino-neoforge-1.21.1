package net.andrespr.casinorocket.network.c2s.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.blackjack.BlackjackAction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record BlackjackActionC2SPayload(BlockPos pos, String machineKey, BlackjackAction action) implements CustomPacketPayload {

    public static final Type<BlackjackActionC2SPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "blackjack_action"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BlackjackActionC2SPayload> CODEC =
            StreamCodec.ofMember(BlackjackActionC2SPayload::write, BlackjackActionC2SPayload::read);

    private static void write(BlackjackActionC2SPayload p, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(p.pos());
        buf.writeUtf(p.machineKey());
        buf.writeEnum(p.action());
    }

    private static BlackjackActionC2SPayload read(RegistryFriendlyByteBuf buf) {
        return new BlackjackActionC2SPayload(
                buf.readBlockPos(),
                buf.readUtf(),
                buf.readEnum(BlackjackAction.class)
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}

