package com.karas7171.atmossystem.util;

import com.karas7171.atmossystem.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AtmosVisualizer {

    private static final Map<UUID, Queue<VisualTask>> VISUAL_TASKS = new ConcurrentHashMap<>();

    public static void addVisualTask(BlockPos pos, ServerLevel level, UUID taskID) {
        Queue<VisualTask> queue = VISUAL_TASKS.computeIfAbsent(taskID, k -> new LinkedList<>());
        queue.add(new VisualTask(pos, level));
    }

    public static void tick() {
        if (VISUAL_TASKS.isEmpty()) return;

        var iterator = VISUAL_TASKS.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            Queue<VisualTask> queue = entry.getValue();
            if (queue.isEmpty()) {
                iterator.remove();
                continue;
            }

            for (int i = 0; i < 2; i++) {
                VisualTask task = queue.poll();
                if (task == null) break;
                visualize(task.level(), task.pos());
            }

            if (queue.isEmpty()) iterator.remove();
        }
    }

    record VisualTask(BlockPos pos, ServerLevel level) {}

    public static void visualize(ServerLevel level, BlockPos start) {
        double x = start.getX() + 0.5;
        double y = start.getY() + 0.5;
        double z = start.getZ() + 0.5;

        level.sendParticles(
            ModParticles.O2_PARTICLE.get(),
            x, y, z,
            0,
            0, 0, 0,
            0.01
        );
    }

    public static void clearVISUAL_QUEUE(UUID taskId) {
        VISUAL_TASKS.remove(taskId);
    }

    public static void runViewCycle(ServerLevel level, List<BlockPos> airBlocks, BlockPos StartPos) {
        for (BlockPos pos : airBlocks) {
            if (pos.distSqr(StartPos) <= 1024) {
                visualize(level, pos);
            }
        }
    }
}
