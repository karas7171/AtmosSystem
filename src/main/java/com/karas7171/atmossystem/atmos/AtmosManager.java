package com.karas7171.atmossystem.atmos;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtmosManager {
    private static AtmosManager instance;
    private Level currentScanLevel = null;

    private final Map<BlockPos, GasCell> cells = new HashMap<>();

    public static AtmosManager get() {
        if (instance == null) {
            instance = new AtmosManager();
        }
        return instance;
    }

    public void scanFullZone(Level level, BlockPos startPos) {
        this.currentScanLevel = level;

        if (isAirBlock(startPos)) {
            registerCell(startPos);

            scanYLine(startPos);
            scanZLine(startPos);
            scanXLine(startPos);

            List<BlockPos> xLineBlocks = findXLineBlocks(startPos);
            List<BlockPos> zLineForXBlock = new ArrayList<>();

            for (BlockPos xPos : xLineBlocks) {
                scanYLine(xPos);
                scanZLine(xPos);
                zLineForXBlock.addAll(findZlineBlocks(xPos));
            }

            for (BlockPos zPos : zLineForXBlock) {
                scanYLine(zPos);
            }
        }

        this.currentScanLevel = null;
    }

    private boolean isAirBlock(BlockPos pos) {
        if (currentScanLevel == null || !currentScanLevel.isLoaded(pos)) return false;
        BlockState state = currentScanLevel.getBlockState(pos);
        return state.isAir();
    }

    private void scanYLine(BlockPos startPos) {
        BlockPos buffer = startPos.above();
        while (isAirBlock(buffer)) {
            registerCell(buffer);
            buffer = buffer.above();
        }

        buffer = startPos.below();
        while (isAirBlock(buffer)) {
            registerCell(buffer);
            buffer = buffer.below();
        }
    }

    private void scanZLine(BlockPos startPos) {
        BlockPos buffer = startPos.north(); // z - 1;
        while (isAirBlock(buffer)) {
            registerCell(buffer);
            buffer = buffer.north();
        }

        buffer = startPos.south(); // z + 1
        while (isAirBlock(buffer)) {
            registerCell(buffer);
            buffer = buffer.south();
        }
    }

    private List<BlockPos> findZlineBlocks(BlockPos startPos) {
        List<BlockPos> foundblocks = new ArrayList<>();

        BlockPos buffer = startPos.north();
        while (isAirBlock(buffer)) {
            foundblocks.add(buffer);
            buffer = buffer.north();
        }

        buffer = startPos.south();
        while (isAirBlock(buffer)) {
            foundblocks.add(buffer);
            buffer = buffer.south();
        }
        return  foundblocks;
    }

    private void scanXLine(BlockPos startPos) {
        BlockPos buffer = startPos.west(); // x - 1
        while (isAirBlock(buffer)) {
            registerCell(buffer);
            buffer = buffer.west();
        }

        buffer = startPos.east(); // x + 1
        while (isAirBlock(buffer)) {
            registerCell(buffer);
            buffer = buffer.east();
        }
    }

    private List<BlockPos> findXLineBlocks(BlockPos startPos) {
        List<BlockPos> foundBlocks = new ArrayList<>();

        BlockPos buffer = startPos.west();
        while (isAirBlock(buffer)) {
            foundBlocks.add(buffer);
            buffer = buffer.west();
        }

        buffer = startPos.east();
        while (isAirBlock(buffer)) {
            foundBlocks.add(buffer);
            buffer = buffer.east();
        }

        return foundBlocks;
    }

    private void registerCell(BlockPos pos) {
        GasCell cell = new GasCell();
        cell.setPressure(101.325f);
        cell.setTemperature(293.15f);
        cell.setOxygen(41.57f);
        cells.put(pos, cell);
    }

    public GasCell getGasCellInfo(BlockPos pos) {
        GasCell cellInfo = cells.get(pos);

        return  cellInfo;
    }

    public void reportCellInfo (BlockPos pos, CommandSourceStack source) {
        GasCell cellInfo = cells.get(pos);
        StringBuilder message = new StringBuilder();

        message.append("=----------=").append("\n");
        message.append("Позиция: ").append(pos.toShortString()).append("\n");
        if (cellInfo == null) {
            message.append("Газовый тайл отсутствует").append("\n");
            source.sendSuccess(() -> Component.literal(message.toString()), false);
            return;
        }
        message.append("Давление: ").append(cellInfo.getPressure()).append(" кПа\n");
        message.append("Температура: ").append(cellInfo.getTemperature()).append(" К\n");
        message.append("Кислород: ").append(cellInfo.getOxygen()).append(" моль");
        source.sendSuccess(() -> Component.literal(message.toString()), false);
    }
}
