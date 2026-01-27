package com.karas7171.atmossystem.util;

import com.karas7171.atmossystem.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AtmosVisualizer {

    private static final Queue<VisualTask> VISUAL_QUEUE = new ConcurrentLinkedDeque<>();

    public static void addVisualTask(BlockPos pos, ServerLevel level) {
        VISUAL_QUEUE.add(new VisualTask(pos, level));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (!VISUAL_QUEUE.isEmpty()) {
            for (int i = 0; i < 5; i++) {
                VisualTask task = VISUAL_QUEUE.poll();
                if (task == null) break;
                drawSinglePoint(task.level(), task.pos());
            }
        }
    }

    record VisualTask(BlockPos pos, ServerLevel level) {}

    private static void drawSinglePoint(ServerLevel level, BlockPos pos) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        level.sendParticles(
                ModParticles.O2_PARTICLE.get(),
                x, y, z,
                0,
                0, 0, 0,
                0.01
        );
    }

    public static void visualize(ServerLevel level, BlockPos start, BlockPos end) {
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

    public static void clearVISUAL_QUEUE() {
        VISUAL_QUEUE.clear();
    }
}
