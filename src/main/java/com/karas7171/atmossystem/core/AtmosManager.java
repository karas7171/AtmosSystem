package com.karas7171.atmossystem.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtmosManager {
    private final Map<Integer, AtmosZone> atmosZones = new HashMap<>();
    int nextID = 0;

    public AtmosZone getZone(BlockPos pos) {
        if (atmosZones.isEmpty()) {
            return null;
        }
        for (AtmosZone atmosZone : atmosZones.values()) {
            List<BlockPos> blocks = atmosZone.getAirBlocks();

            for (BlockPos block : blocks) {
                if (block.equals(pos)) {
                    return atmosZone;
                }
            }
        }
        return null;
    }

    private static AtmosManager instance;

    public static AtmosManager get() {
        if (instance == null) {
            instance = new AtmosManager();
        }
        return instance;
    }

    public void createAndAddZone(Level level, BlockPos pos) {
        AtmosZaneFactory factory = new AtmosZaneFactory();
        AtmosZone zoneData = factory.createAtmosZone(level, pos);

        AtmosZone zone = new AtmosZone(zoneData, nextID);
        atmosZones.put(nextID, zone);
        nextID++;
    }
}
