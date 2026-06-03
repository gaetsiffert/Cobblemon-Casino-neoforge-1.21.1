package net.andrespr.casinorocket.screen.opening;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record CommonMachineOpenData(BlockPos pos, String machineKey) {

    public static final StreamCodec<RegistryFriendlyByteBuf, CommonMachineOpenData> CODEC =
            StreamCodec.ofMember(CommonMachineOpenData::write, CommonMachineOpenData::read);

    private static void write(CommonMachineOpenData data, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeUtf(data.machineKey());
    }

    private static CommonMachineOpenData read(RegistryFriendlyByteBuf buf) {
        return new CommonMachineOpenData(
                buf.readBlockPos(),
                buf.readUtf()
        );
    }

}

