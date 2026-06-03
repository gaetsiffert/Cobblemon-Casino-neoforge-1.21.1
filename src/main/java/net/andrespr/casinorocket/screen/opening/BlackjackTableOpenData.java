package net.andrespr.casinorocket.screen.opening;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record BlackjackTableOpenData(BlockPos pos, String machineKey, long balance, int betIndex) {

    public static final StreamCodec<RegistryFriendlyByteBuf, BlackjackTableOpenData> CODEC =
            StreamCodec.ofMember(BlackjackTableOpenData::write, BlackjackTableOpenData::read);

    private static void write(BlackjackTableOpenData data, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeUtf(data.machineKey());
        buf.writeLong(data.balance());
        buf.writeInt(data.betIndex());
    }

    private static BlackjackTableOpenData read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String machineKey = buf.readUtf();
        long balance = buf.readLong();
        int betIndex = buf.readInt();
        return new BlackjackTableOpenData(pos, machineKey, balance, betIndex);
    }

}

