package com.karas7171.atmossystem.core;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AtmosZone {
    private final int ID;

    private final ZoneBounds bounds;
    private final int sizeX, sizeZ, sizeY;

    private final float[] pressure;
    private final float[] temperature;
    private final float[] oxygen;
    private final boolean[] solid;

    private final List<BlockPos> airBlocks;

    public AtmosZone(int ID,
                     ZoneBounds zoneBounds,
                     float initalPressure,
                     float initialTemperature,
                     float initialOxygen,
                     List<BlockPos> airBlocks) {
        this.ID = ID;
        this.bounds = zoneBounds;

        this.sizeX = bounds.maxX() - bounds.minX() + 1;
        this.sizeY = bounds.maxY() - bounds.minY() + 1;
        this.sizeZ = bounds.maxZ() - bounds.minZ() + 1;
        int totalCell = sizeX * sizeY * sizeZ;

        pressure = new float[totalCell];
        temperature = new float[totalCell];
        oxygen = new float[totalCell];
        solid = new boolean[totalCell];

        Arrays.fill(pressure, initalPressure);
        Arrays.fill(temperature, initialTemperature);
        Arrays.fill(oxygen, initialOxygen);
        Arrays.fill(solid, false);

        this.airBlocks = airBlocks;
    }

    public AtmosZone(AtmosZone original, int newID) {
        this.ID = newID;

        this.bounds = original.bounds;
        this.sizeX = original.sizeX;
        this.sizeZ = original.sizeZ;
        this.sizeY = original.sizeY;

        this.pressure = Arrays.copyOf(original.pressure, original.pressure.length);
        this.temperature = Arrays.copyOf(original.temperature, original.temperature.length);
        this.oxygen = Arrays.copyOf(original.oxygen, original.oxygen.length);
        this.solid = Arrays.copyOf(original.solid, original.solid.length);

        this.airBlocks = new ArrayList<>(original.airBlocks);
    }

    private int getIndex(BlockPos pos) {
        int relX =  pos.getX() - bounds.minX();
        int relY =  pos.getY() - bounds.minY();
        int relZ = pos.getZ() - bounds.minZ();

        return (relX * sizeY * sizeZ) + (relY * sizeZ) + relZ;
    }

    public List<BlockPos> getAirBlocks() {
        return airBlocks;
    }

    public void reportCellInfo (BlockPos pos, CommandSourceStack source) {
        StringBuilder message = new StringBuilder();

        message.append("=----------=").append("\n");
        message.append("Позиция: ").append(pos.toShortString()).append("\n");
        if (pressure == null) {
            message.append("Газовый тайл отсутствует");
            source.sendSuccess(() -> Component.literal(message.toString()), false);
            return;
        }
        int index = getIndex(pos);
        message.append("Давление: ").append(pressure[index]).append(" кПа\n");
        message.append("Температура: ").append(temperature[index]).append(" К\n");
        message.append("Кислород: ").append(oxygen[index]).append(" моль");
        source.sendSuccess(() -> Component.literal(message.toString()), false);
    }
}
