package net.andrespr.casinorocket.network.c2s.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChangeBlackjackBetIndexC2SPayload(boolean increase) implements CustomPacketPayload {

    public static final Type<ChangeBlackjackBetIndexC2SPayload> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "blackjack_change_bet_index"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeBlackjackBetIndexC2SPayload> CODEC =
            StreamCodec.ofMember(ChangeBlackjackBetIndexC2SPayload::write, ChangeBlackjackBetIndexC2SPayload::read);

    private static void write(ChangeBlackjackBetIndexC2SPayload p, RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(p.increase());
    }

    private static ChangeBlackjackBetIndexC2SPayload read(RegistryFriendlyByteBuf buf) {
        return new ChangeBlackjackBetIndexC2SPayload(buf.readBoolean());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}

