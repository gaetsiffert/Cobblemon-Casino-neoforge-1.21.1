package net.andrespr.casinorocket.util;

import net.minecraft.core.BlockPos;

public interface IMachineBoundHandler {
    BlockPos getMachinePos();
    default String getMachineKey() {
        return "unknown";
    }
}

