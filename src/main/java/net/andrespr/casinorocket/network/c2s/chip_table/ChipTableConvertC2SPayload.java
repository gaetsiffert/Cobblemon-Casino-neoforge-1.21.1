package net.andrespr.casinorocket.network.c2s.chip_table;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.chip_table.ChipTableConversionMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ChipTableConvertC2SPayload(BlockPos pos, ChipTableConversionMode mode, long cobbledollarAmount) implements CustomPacketPayload {

    public static final Type<ChipTableConvertC2SPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "chip_table_convert"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ChipTableConvertC2SPayload> CODEC =
            StreamCodec.ofMember(ChipTableConvertC2SPayload::write, ChipTableConvertC2SPayload::read);

    private static void write(ChipTableConvertC2SPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(payload.pos());
        buf.writeEnum(payload.mode());
        buf.writeVarLong(payload.cobbledollarAmount());
    }

    private static ChipTableConvertC2SPayload read(RegistryFriendlyByteBuf buf) {
        return new ChipTableConvertC2SPayload(buf.readBlockPos(), buf.readEnum(ChipTableConversionMode.class), buf.readVarLong());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }

}
