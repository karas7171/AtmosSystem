package com.karas7171.atmossystem.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;

public class AtmosVisualizer {


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
}
