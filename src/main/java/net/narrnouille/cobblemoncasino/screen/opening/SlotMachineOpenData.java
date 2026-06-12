package net.narrnouille.cobblemoncasino.screen.opening;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SlotMachineOpenData(BlockPos pos, String machineKey, long balance, int betBase, int linesMode) {

    public static final StreamCodec<RegistryFriendlyByteBuf, SlotMachineOpenData> CODEC =
            StreamCodec.ofMember(SlotMachineOpenData::write, SlotMachineOpenData::read);

    private static void write(SlotMachineOpenData data, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeUtf(data.machineKey());
        buf.writeLong(data.balance());
        buf.writeInt(data.betBase());
        buf.writeInt(data.linesMode());
    }

    private static SlotMachineOpenData read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String machineKey = buf.readUtf();
        long balance = buf.readLong();
        int betBase = buf.readInt();
        int linesMode = buf.readInt();
        return new SlotMachineOpenData(pos, machineKey, balance, betBase, linesMode);
    }

}

