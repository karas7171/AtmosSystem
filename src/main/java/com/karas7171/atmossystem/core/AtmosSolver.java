package com.karas7171.atmossystem.core;

import java.util.Map;

import static com.karas7171.atmossystem.core.AtmosZone.ZoneDimensions.getDimensions;

public class AtmosSolver {
    public static void tick() {
        Map<Integer, AtmosZone> atmosZones = AtmosManager.get().getAllZones();
        for (AtmosZone zone : atmosZones.values()) {
            if (zone.getDirty()) {
                calculateDiffusion(zone);
            }
        }
    }

    private static void calculateDiffusion(AtmosZone zone) {
        AtmosZone.ZoneDimensions dim = getDimensions(zone);
        int dx = dim.dx();
        int dy = dim.dy();
        int dz = dim.dz();

        int sizeX = dim.sizeX();
        int sizeY = dim.sizeY();
        int sizeZ = dim.sizeZ();

        float[] oxgFront = zone.getOxygenFront();
        float[] tempFront = zone.getTemperatureFront();
        float[] oxgBack = zone.getOxygenBack();
        float[] tempBack = zone.getTemperatureBack();
        float[] presFront = zone.getPressure();
        boolean[] isSolid = zone.getSolid();

        for (int i = 0; i < oxgFront.length; i++) {
            oxgBack[i] = oxgFront[i];
            tempBack[i] = oxgFront[i] * tempFront[i];
        }

        float diffRate = 0.5f;
        int[] neighbors = new int[6];
        int changesCount = 0;

        for (int i = 0; i < oxgFront.length; i++) {
            if (isSolid[i] || oxgFront[i] < 0.01f) continue;

            float currentOxg = oxgFront[i];
            float currentTemp = tempFront[i];

            int x = i / (sizeY * sizeZ);
            int y = (i % (sizeY * sizeZ)) / sizeZ;
            int z = i % sizeZ;

            int neighborCount = 0;

            if (x + 1 < sizeX && !isSolid[i + dx]) neighbors[neighborCount++] = i + dx;
            if (x - 1 >= 0    && !isSolid[i - dx]) neighbors[neighborCount++] = i - dx;
            if (y + 1 < sizeY && !isSolid[i + dy]) neighbors[neighborCount++] = i + dy;
            if (y - 1 >= 0    && !isSolid[i - dy]) neighbors[neighborCount++] = i - dy;
            if (z + 1 < sizeZ && !isSolid[i + dz]) neighbors[neighborCount++] = i + dz;
            if (z - 1 >= 0    && !isSolid[i - dz]) neighbors[neighborCount++] = i - dz;

            if (neighborCount > 0) {
                for (int n = 0; n < neighborCount; n++) {
                    int nIdx = neighbors[n];

                    float dealtOxg = (currentOxg - oxgFront[nIdx]) / (neighborCount + 1);

                    if (dealtOxg >= 0.01) {
                        float moveAmount = dealtOxg * diffRate;

                        oxgBack[i] -= moveAmount;
                        oxgBack[nIdx] += moveAmount;

                        float energyAmount = moveAmount * currentTemp;
                        tempBack[i] -= energyAmount;
                        tempBack[nIdx] += energyAmount;

                        changesCount++;
                    }
                }
            }
        }
        float R = 8.31f;
        for (int j = 0; j < oxgBack.length; j++) {
            float amount = oxgBack[j];

            if (oxgBack[j] >= 0.01) {
                tempBack[j] /= amount;
                presFront[j] = amount * R * tempBack[j];
            } else {
                tempBack[j] = 2.7f;
                presFront[j] = 0.0f;
            }
        }
        zone.swapOxygenBuffers();
        zone.swapTemperatureBuffers();

        if (changesCount == 0) zone.setDirty(false);
    }
}
