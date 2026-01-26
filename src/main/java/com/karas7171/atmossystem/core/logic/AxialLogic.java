package com.karas7171.atmossystem.core.logic;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AxialLogic implements AtmosLogic {
    @Override
    public List<BlockPos> createAtmosZoneAirBlocks(BlockPos pos, Predicate<BlockPos> predicate, AtmosProgressListener listener) {
        List<BlockPos> airBlocks = new ArrayList<>();
        List<BlockPos> foundZForXBlock = new ArrayList<>();

        if (predicate.test(pos)) airBlocks.add(pos);

        airBlocks.addAll((scanLine(pos, AxisDirection.Y_POS, AxisDirection.Y_NEG, predicate, listener)));

        List<BlockPos> foundZBlock = new ArrayList<>(scanLine(pos, AxisDirection.Z_POS, AxisDirection.Z_NEG, predicate, listener));
        airBlocks.addAll(foundZBlock);
        for (BlockPos zPos : foundZBlock) {
            airBlocks.addAll(scanLine(zPos, AxisDirection.Y_POS, AxisDirection.Y_NEG, predicate, listener));
        }

        List<BlockPos> foundXBlock = new ArrayList<>(scanLine(pos, AxisDirection.X_POS, AxisDirection.X_NEG, predicate, listener));
        airBlocks.addAll(foundXBlock);
        for (BlockPos xPos : foundXBlock) {
            airBlocks.addAll(scanLine(xPos, AxisDirection.Y_POS, AxisDirection.Y_NEG, predicate, listener));
        }

        for (BlockPos xPos : foundXBlock) {
            List<BlockPos> zBlocksForThisX = scanLine(xPos, AxisDirection.Z_POS, AxisDirection.Z_NEG, predicate, listener);
            foundZForXBlock.addAll(zBlocksForThisX);
            airBlocks.addAll(zBlocksForThisX);
        }
        for (BlockPos xPos : foundZForXBlock) {
            airBlocks.addAll(scanLine(xPos, AxisDirection.Y_POS, AxisDirection.Y_NEG, predicate, listener));
        }
        return airBlocks;
    }

    private List<BlockPos> scanLine(BlockPos pos, AxisDirection posDir, AxisDirection negDir, Predicate<BlockPos> predicate, AtmosProgressListener listener) {
        List<BlockPos> airBLocks = new ArrayList<>();

        BlockPos buffer = move(pos, posDir);
        while (predicate.test(buffer)) {
            airBLocks.add(buffer);
            BlockPos prev = buffer;
            buffer = move(buffer, posDir);
            if (listener != null) {
                listener.onBlockFound(prev, buffer);
            }
        }

        buffer = move(pos, negDir);
        while (predicate.test(buffer)) {
            airBLocks.add(buffer);
            BlockPos prev = buffer;
            buffer = move(buffer, negDir);
            if (listener != null) {
                listener.onBlockFound(prev, buffer);
            }
        }
        return airBLocks;
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
}
