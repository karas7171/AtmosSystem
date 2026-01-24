package com.karas7171.atmossystem.core;

import com.karas7171.atmossystem.core.logic.AtmosLogic;
import com.karas7171.atmossystem.core.logic.AxialLogic;
import com.karas7171.atmossystem.core.logic.BFSLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtmosManager {
    private final Map<Integer, AtmosZone> atmosZones = new HashMap<>();
    int nextID = 0;

    private static AtmosManager instance;

    public static AtmosManager get() {
        if (instance == null) {
            instance = new AtmosManager();
        }
        return instance;
    }

    private final AtmosLogic BFSLogic = new BFSLogic();

    public AtmosLogic getBFSLogic() {
        return BFSLogic;
    }

    private final AtmosLogic AxialLogic = new AxialLogic();

    public AtmosLogic getAxialLogic() {
        return AxialLogic;
    }

    private final AtmosZoneFactory zoneFactory = new AtmosZoneFactory();

    public AtmosZoneFactory getZoneFactory() {
        return zoneFactory;
    }

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

    public void createAndAddZone(Level level, BlockPos pos, AtmosLogic atmosLogic) {
        List<BlockPos> airBlocks = zoneFactory.findAirBlocks(level, pos, atmosLogic);
        int ID = nextID++;
        AtmosZone zone = zoneFactory.createAtmosZone(level, pos, (l, p) -> airBlocks, ID);
        atmosZones.put(ID, zone);
    }

    public void removeZone(AtmosZone zone) {
        int ID = zone.getID();
        atmosZones.remove(ID);
    }
}
