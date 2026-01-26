package com.karas7171.atmossystem.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

@EventBusSubscriber(modid = "atmossystem")
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

        DustParticleOptions dust = new DustParticleOptions(0x0000FF, 1.0f);

        level.sendParticles(
                dust,
                x, y, z,
                1,
                0, 0, 0,
                0.0
        );
    }

    public static void visualize(ServerLevel level, BlockPos start, BlockPos end) {
        double x = start.getX() + 0.5;
        double y = start.getY() + 0.5;
        double z = start.getZ() + 0.5;

        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();
        double dz = end.getZ() - start.getZ();

        DustParticleOptions dust = new DustParticleOptions(0x0000FF, 1.0f);

        level.sendParticles(
            dust,
            x, y, z,
            0,
            dx, dy, dz,
            0.5
        );
    }

    public static void clearVISUAL_QUEUE() {
        VISUAL_QUEUE.clear();
    }
}
