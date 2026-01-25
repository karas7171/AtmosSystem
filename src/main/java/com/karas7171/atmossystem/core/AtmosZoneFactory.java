package com.karas7171.atmossystem.core;

import com.karas7171.atmossystem.core.logic.AtmosLogic;
import com.karas7171.atmossystem.core.logic.AtmosProgressListener;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.function.BiFunction;

public class AtmosZoneFactory {

    public AtmosZone createAtmosZone(Level level, BlockPos pos, BiFunction<Level, BlockPos, List<BlockPos>> finder, int ID) {
        List<BlockPos> airBlocks = finder.apply(level, pos);
        ZoneBounds zoneBounds = createAtmosZoneBounds(airBlocks);

        return new AtmosZone(
                ID,
                zoneBounds,
                101.325f,
                293.15f,
                41.57f,
                airBlocks
        );
    }

    public List<BlockPos> findAirBlocks(Level level, BlockPos startPos, AtmosLogic logic, AtmosProgressListener listener) {
        return logic.createAtmosZoneAirBlocks(startPos, pos -> isAirBlock(level, pos), listener);
    }

    private boolean isAirBlock(Level level, BlockPos pos) {
        if (level == null || !level.isLoaded(pos)) return false;
        BlockState state = level.getBlockState(pos);
        return state.isAir();
    }

    private ZoneBounds createAtmosZoneBounds (List<BlockPos> airBlocks) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (BlockPos pos : airBlocks) {

            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
            if (z < minZ) minZ = z;
            if (z > maxZ) maxZ = z;
        }
        return new ZoneBounds(minX, maxX, minY, maxY, minZ, maxZ);
    }
}
