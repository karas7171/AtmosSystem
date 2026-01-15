package com.karas7171.atmossystem.atmos;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class AtmosManager {
    private static AtmosManager instance;

    private int minX, minZ, minY;
    private int maxX, maxZ, maxY;
    private int sizeX, sizeZ, sizeY;

    private float[] pressure;
    private float[] temperature;
    private float[] oxygen;
    private boolean[] solid;

    List<BlockPos> airBlocks = new ArrayList<>();

    public static AtmosManager get() {
        if (instance == null) {
            instance = new AtmosManager();
        }
        return instance;
    }

    public void scanFullZone(Level level, BlockPos startPos) {

        if (isAirBlock(level, startPos)) {
            registerAirBLock(startPos);

            scanYLine(level, startPos);
            scanZLine(level, startPos);
            scanXLine(level, startPos);

            List<BlockPos> xLineBlocks = findXLineBlocks(level, startPos);
            List<BlockPos> zLineForXBlock = new ArrayList<>();

            for (BlockPos xPos : xLineBlocks) {
                scanYLine(level, xPos);
                scanZLine(level, xPos);
                zLineForXBlock.addAll(findZlineBlocks(level, xPos));
            }

            for (BlockPos zPos : zLineForXBlock) {
                scanYLine(level, zPos);
            }
        }
        createArraysFromAirBlocks(airBlocks);
    }

    private void createArraysFromAirBlocks(List<BlockPos> airBlocks) {
        sizeX = maxX - minX + 1;
        sizeY = maxY - minY + 1;
        sizeZ = maxZ - minZ + 1;
        int totalCell = sizeX * sizeY * sizeZ;

        pressure = new float[totalCell];
        temperature = new float[totalCell];
        oxygen = new float[totalCell];
        solid = new boolean[totalCell]; // true - стена

        Arrays.fill(solid, true);

        for (BlockPos pos : airBlocks) {
            int index = convertToIndex(pos, sizeY, sizeZ);
            solid[index] = false;
            registerCell(index);
        }
    }

    private boolean isAirBlock(Level level, BlockPos pos) {
        if (level == null || !level.isLoaded(pos)) return false;
        BlockState state = level.getBlockState(pos);
        return state.isAir();
    }

    private void scanYLine(Level level, BlockPos startPos) {
        BlockPos buffer = startPos.above();
        while (isAirBlock(level, buffer)) {
            registerAirBLock(buffer);
            buffer = buffer.above();
        }

        buffer = startPos.below();
        while (isAirBlock(level, buffer)) {
            registerAirBLock(buffer);
            buffer = buffer.below();
        }
    }

    private void scanZLine(Level level, BlockPos startPos) {
        BlockPos buffer = startPos.south(); // z + 1
        while (isAirBlock(level, buffer)) {
            registerAirBLock(buffer);
            buffer = buffer.south();
        }

        buffer = startPos.north(); // z - 1
        while (isAirBlock(level, buffer)) {
            registerAirBLock(buffer);
            buffer = buffer.north();
        }
    }

    private List<BlockPos> findZlineBlocks(Level level, BlockPos startPos) {
        List<BlockPos> foundblocks = new ArrayList<>();

        BlockPos buffer = startPos.north();
        while (isAirBlock(level, buffer)) {
            foundblocks.add(buffer);
            buffer = buffer.north();
        }

        buffer = startPos.south();
        while (isAirBlock(level, buffer)) {
            foundblocks.add(buffer);
            buffer = buffer.south();
        }
        return  foundblocks;
    }

    private void scanXLine(Level level, BlockPos startPos) {
        BlockPos buffer = startPos.east(); // x + 1
        while (isAirBlock(level, buffer)) {
            registerAirBLock(buffer);
            buffer = buffer.east();
        }

        buffer = startPos.west(); // x - 1
        while (isAirBlock(level, buffer)) {
            registerAirBLock(buffer);
            buffer = buffer.west();
        }
    }

    private List<BlockPos> findXLineBlocks(Level level, BlockPos startPos) {
        List<BlockPos> foundBlocks = new ArrayList<>();

        BlockPos buffer = startPos.west();
        while (isAirBlock(level, buffer)) {
            foundBlocks.add(buffer);
            buffer = buffer.west();
        }

        buffer = startPos.east();
        while (isAirBlock(level, buffer)) {
            foundBlocks.add(buffer);
            buffer = buffer.east();
        }

        return foundBlocks;
    }
    private void registerAirBLock(BlockPos pos) {
        airBlocks.add(pos);
        updateBounds(pos);
    }

    private void updateBounds(BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (x < minX) minX = x;
        if (x > maxX) maxX = x;
        if (y < minY) minY = y;
        if (y > maxY) maxY = y;
        if (z < minZ) minZ = z;
        if (z > maxZ) maxZ = z;
    }

    private int convertToIndex(BlockPos pos, int sizeY, int sizeZ) {
        int relX =  pos.getX() - minX;
        int relY =  pos.getY() - minY;
        int relZ = pos.getZ() - minZ;

        return (relX * sizeY * sizeZ) + (relY * sizeZ) + relZ;
    }

    private void registerCell(int index) {
        pressure[index] = 101.325f;
        temperature[index] = 293.15f;
        oxygen[index] = 41.57f;
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
        int index = convertToIndex(pos, sizeY, sizeZ);
        message.append("Давление: ").append(pressure[index]).append(" кПа\n");
        message.append("Температура: ").append(temperature[index]).append(" К\n");
        message.append("Кислород: ").append(oxygen[index]).append(" моль");
        source.sendSuccess(() -> Component.literal(message.toString()), false);
    }
}
