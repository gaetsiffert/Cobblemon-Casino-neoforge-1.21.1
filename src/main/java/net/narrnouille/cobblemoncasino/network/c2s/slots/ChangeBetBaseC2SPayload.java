package net.narrnouille.cobblemoncasino.network.c2s.slots;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChangeBetBaseC2SPayload(boolean increase) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "change_bet_base");
    public static final Type<ChangeBetBaseC2SPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeBetBaseC2SPayload> CODEC =
            StreamCodec.ofMember(ChangeBetBaseC2SPayload::write, ChangeBetBaseC2SPayload::read);

    private static void write(ChangeBetBaseC2SPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(payload.increase());
    }

    private static ChangeBetBaseC2SPayload read(RegistryFriendlyByteBuf buf) {
        return new ChangeBetBaseC2SPayload(buf.readBoolean());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

}

