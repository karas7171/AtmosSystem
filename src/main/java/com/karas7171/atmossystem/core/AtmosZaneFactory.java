package com.karas7171.atmossystem.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class AtmosZaneFactory {
    public AtmosZone createAtmosZone(Level level, BlockPos pos) {
        List<BlockPos> airBlocks = createAtmosZoneAirBlocks(level, pos);
        ZoneBounds zoneBounds = createAtmosZoneBounds(airBlocks);

        return new AtmosZone(
                -1,
                zoneBounds,
                101.325f,
                293.15f,
                41.57f,
                airBlocks
        );
    }

    private List<BlockPos> createAtmosZoneAirBlocks(Level level, BlockPos pos) {
        List<BlockPos> foundZForXBlock = new ArrayList<>();

        List<BlockPos> airBlocks = new ArrayList<>(scanLine(level, pos, AxisDirection.Y_POS, AxisDirection.Y_NEG));
        List<BlockPos> foundZBlock = new ArrayList<>(scanLine(level, pos, AxisDirection.Z_POS, AxisDirection.Z_NEG));
        airBlocks.addAll(foundZBlock);
        for (BlockPos zPos : foundZBlock) {
            airBlocks.addAll(scanLine(level, zPos, AxisDirection.Y_POS, AxisDirection.Y_NEG));
        }
        List<BlockPos> foundXBlock = new ArrayList<>(scanLine(level, pos, AxisDirection.X_POS, AxisDirection.X_NEG));
        airBlocks.addAll(foundXBlock);
        for (BlockPos xPos : foundXBlock) {
            foundZForXBlock.addAll(scanLine(level, xPos, AxisDirection.Z_POS, AxisDirection.Z_NEG));
            airBlocks.addAll(foundZForXBlock);
        }
        for (BlockPos xPos : foundZForXBlock) {
            airBlocks.addAll(scanLine(level, xPos, AxisDirection.Y_POS, AxisDirection.Y_NEG));
        }
        return airBlocks;
    }

    private boolean isAirBlock(Level level, BlockPos pos) {
        if (level == null || !level.isLoaded(pos)) return false;
        BlockState state = level.getBlockState(pos);
        return state.isAir();
    }

    private enum AxisDirection {
        X_POS, X_NEG,
        Y_POS, Y_NEG,
        Z_POS, Z_NEG,
    }

    private BlockPos move(BlockPos pos, AxisDirection dir) {
        return switch (dir) {
            case X_POS -> pos.east();
            case X_NEG -> pos.west();
            case Y_POS -> pos.above();
            case Y_NEG -> pos.below();
            case Z_POS -> pos.south();
            case Z_NEG -> pos.north();
        };
    }

    private List<BlockPos> scanLine(Level level, BlockPos pos, AxisDirection posDir, AxisDirection negDir) {
        List<BlockPos> airBLocks = new ArrayList<>();

        BlockPos buffer = move(pos, posDir);
        while (isAirBlock(level, buffer)) {
            airBLocks.add(buffer);
            buffer = move(buffer, posDir);
        }

        buffer = move(pos, negDir);
        while (isAirBlock(level, buffer)) {
            airBLocks.add(buffer);
            buffer = move(buffer, negDir);
        }
        return airBLocks;
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
