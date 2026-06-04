package net.andrespr.casinorocket.screen.opening;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record BlackjackTableOpenData(BlockPos pos, String machineKey, long balance, int betIndex, long[] betValues) {

    public static final StreamCodec<RegistryFriendlyByteBuf, BlackjackTableOpenData> CODEC =
            StreamCodec.ofMember(BlackjackTableOpenData::write, BlackjackTableOpenData::read);

    private static void write(BlackjackTableOpenData data, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeUtf(data.machineKey());
        buf.writeLong(data.balance());
        buf.writeInt(data.betIndex());
        writeLongArray(buf, data.betValues());
    }

    private static BlackjackTableOpenData read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String machineKey = buf.readUtf();
        long balance = buf.readLong();
        int betIndex = buf.readInt();
        long[] betValues = readLongArray(buf);
        return new BlackjackTableOpenData(pos, machineKey, balance, betIndex, betValues);
    }

    private static void writeLongArray(RegistryFriendlyByteBuf buf, long[] arr) {
        buf.writeVarInt(arr.length);
        for (long value : arr) buf.writeLong(value);
    }

    private static long[] readLongArray(RegistryFriendlyByteBuf buf) {
        int length = buf.readVarInt();
        long[] arr = new long[length];
        for (int i = 0; i < length; i++) arr[i] = buf.readLong();
        return arr;
    }

}

