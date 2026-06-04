package net.andrespr.casinorocket.screen.opening;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ChipTableOpenData(BlockPos pos, boolean walletMode) {

    public static final StreamCodec<RegistryFriendlyByteBuf, ChipTableOpenData> CODEC =
            StreamCodec.ofMember(ChipTableOpenData::write, ChipTableOpenData::read);

    private static void write(ChipTableOpenData data, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeBoolean(data.walletMode());
    }

    private static ChipTableOpenData read(RegistryFriendlyByteBuf buf) {
        return new ChipTableOpenData(buf.readBlockPos(), buf.readBoolean());
    }

}
