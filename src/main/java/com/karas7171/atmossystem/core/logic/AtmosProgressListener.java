package com.karas7171.atmossystem.core.logic;

import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface AtmosProgressListener {
    void onBlockFound(BlockPos current, BlockPos next);
}
