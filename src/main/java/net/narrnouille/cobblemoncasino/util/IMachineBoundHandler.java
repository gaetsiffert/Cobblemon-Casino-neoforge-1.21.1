package net.narrnouille.cobblemoncasino.util;

import net.minecraft.core.BlockPos;

public interface IMachineBoundHandler {
    BlockPos getMachinePos();
    default String getMachineKey() {
        return "unknown";
    }
}

