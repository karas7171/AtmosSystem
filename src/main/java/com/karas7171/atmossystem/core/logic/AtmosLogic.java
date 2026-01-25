package com.karas7171.atmossystem.core.logic;

import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.function.Predicate;

public interface AtmosLogic {
    List<BlockPos> createAtmosZoneAirBlocks(BlockPos pos, Predicate<BlockPos> predicate, AtmosProgressListener listener);
}
