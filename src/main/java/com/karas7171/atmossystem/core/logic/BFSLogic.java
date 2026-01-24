package com.karas7171.atmossystem.core.logic;

import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.function.Predicate;

public class BFSLogic implements AtmosLogic {
    @Override
    public List<BlockPos> createAtmosZoneAirBlocks(BlockPos pos, Predicate<BlockPos> predicate) {
        List<BlockPos> airBlocks = new ArrayList<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        if (!predicate.test(pos)) return airBlocks;

        airBlocks.add(pos);
        queue.add(pos);
        visited.add(pos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            for (BlockPos next : neighbours(current)) {
                if (visited.contains(next)) continue;
                if (!predicate.test(next)) continue;

                airBlocks.add(next);
                queue.add(next);
                visited.add(next);
            }
        }
        return airBlocks;
    }

    private List<BlockPos> neighbours(BlockPos pos) {
        return List.of(
                pos.east(), pos.west(),
                pos.above(), pos.below(),
                pos.north(), pos.south()
        );
    }
}