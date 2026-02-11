package com.karas7171.atmossystem.core;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.List;

public class AtmosZone {
    private final int ID;

    private final ZoneBounds bounds;
    private final int sizeX, sizeZ, sizeY;

    final float[] pressure;
    private float[] temperatureFront;
    private float[] temperatureBack;
    private float[] oxygenFront;
    private float[] oxygenBack;
    private final boolean[] solid;

    private final List<BlockPos> airBlocks;

    private boolean isDirty;

    public AtmosZone(int ID,
                     ZoneBounds zoneBounds,
                     float initialTemperature,
                     float initialOxygen,
                     List<BlockPos> airBlocks) {
        this.ID = ID;
        this.bounds = zoneBounds;

        this.sizeX = bounds.maxX() - bounds.minX() + 1;
        this.sizeY = bounds.maxY() - bounds.minY() + 1;
        this.sizeZ = bounds.maxZ() - bounds.minZ() + 1;
        int totalCell = sizeX * sizeY * sizeZ;

        oxygenFront = new float[totalCell];
        oxygenBack = new float[totalCell];

        temperatureFront = new float[totalCell];
        temperatureBack = new float[totalCell];

        pressure = new float[totalCell];

        solid = new boolean[totalCell];

        Arrays.fill(oxygenFront, initialOxygen);
        Arrays.fill(oxygenBack, initialOxygen);

        Arrays.fill(temperatureFront, initialTemperature);
        Arrays.fill(temperatureBack, initialTemperature);

        for (int i = 0; i < totalCell; i++) {
            pressure[i] = initialOxygen * 8.314f * initialTemperature;
        }

        Arrays.fill(solid, true);

        for (BlockPos pos : airBlocks) {
            int index = getIndex(pos);
            if (index >= 0 && index < totalCell) {
                solid[index] = false;
            }
        }

        this.airBlocks = airBlocks;
    }

    public int getIndex(BlockPos pos) {
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
        message.append("Давление: ").append(String.format("%.2f", pressure[index]/1000.0f)).append(" кПа\n");
        message.append("Температура: ").append(String.format("%.2f", temperatureFront[index])).append(" К\n");
        message.append("Кислород: ").append(String.format("%.2f", oxygenFront[index])).append(" моль");

        source.sendSuccess(() -> Component.literal(message.toString()), false);
    }

    public int getID() {
        return ID;
    }

    public boolean getDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        this.isDirty = dirty;
    }

    public float[] getPressure() {
        return pressure;
    }

    public float[] getTemperatureFront() {
        return temperatureFront;
    }

    public float[] getTemperatureBack() {
        return temperatureBack;
    }

    public float[] getOxygenFront() {
        return oxygenFront;
    }

    public float[] getOxygenBack() {
        return oxygenBack;
    }

    public boolean[] getSolid() {
        return solid;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getSizeZ() {
        return sizeZ;
    }

    public record ZoneDimensions(int sizeX, int sizeY, int sizeZ, int dx, int dy, int dz) {
        public static ZoneDimensions getDimensions(AtmosZone zone) {
            int sx = zone.getSizeX();
            int sy = zone.getSizeY();
            int sz = zone.getSizeZ();

            int dx = sy * sz;
            int dy = sz;
            int dz = 1;

            return new ZoneDimensions(sx, sy, sz, dx, dy, dz);
        }
    }

    public void injectGas(int index, float moles, float temp) {
        oxygenFront[index] = moles;
        temperatureFront[index] = temp;
        oxygenBack[index] = moles;
        temperatureBack[index] = temp;
    }

    public void setGasToAll(float moles, float temp) {
        Arrays.fill(oxygenFront, moles);
        Arrays.fill(temperatureFront, temp);
        Arrays.fill(oxygenBack, moles);
        Arrays.fill(temperatureBack, temp);
    }

    public void swapOxygenBuffers() {
        float[] oxygen = oxygenFront;
        oxygenFront = oxygenBack;
        oxygenBack = oxygen;
    }

    public void swapTemperatureBuffers() {
        float[] temperature = temperatureFront;
        temperatureFront = temperatureBack;
        temperatureBack = temperature;
    }
}
