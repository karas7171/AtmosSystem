package com.karas7171.atmossystem.comand;

import com.karas7171.atmossystem.core.AtmosManager;
import com.karas7171.atmossystem.core.AtmosZone;
import com.karas7171.atmossystem.util.AtmosVisualizer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class AtmosViewCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("view")
                .executes(AtmosViewCommand::executeView);
    }

    private static int executeView(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayerOrException();
        ServerLevel level = source.getLevel();
        BlockPos startPos = player.blockPosition();
        AtmosZone zone = AtmosManager.get().getZone(startPos);
        if (zone == null) {
            source.sendFailure(Component.literal("Зона атмосферы для этой позиции не найдена")
                    .withStyle(ChatFormatting.RED)
            );
            return 0;
        }
        List<BlockPos> airBlocks = new ArrayList<>(zone.getAirBlocks());

        AtmosVisualizer.runViewCycle(level, airBlocks, startPos);
        return Command.SINGLE_SUCCESS;
    }
}
