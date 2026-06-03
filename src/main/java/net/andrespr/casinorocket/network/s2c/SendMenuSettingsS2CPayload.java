package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SendMenuSettingsS2CPayload(long balance, int betBase, int linesMode) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "slot_machine_menu_settings");
    public static final Type<SendMenuSettingsS2CPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, SendMenuSettingsS2CPayload> CODEC =
            StreamCodec.ofMember(SendMenuSettingsS2CPayload::write, SendMenuSettingsS2CPayload::read);

    private static void write(SendMenuSettingsS2CPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeLong(payload.balance());
        buf.writeInt(payload.betBase());
        buf.writeInt(payload.linesMode());
    }

    private static SendMenuSettingsS2CPayload read(RegistryFriendlyByteBuf buf) {
        return new SendMenuSettingsS2CPayload(
                buf.readLong(),
                buf.readInt(),
                buf.readInt()
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

}

