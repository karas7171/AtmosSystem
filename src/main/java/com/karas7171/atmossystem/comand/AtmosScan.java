package com.karas7171.atmossystem.comand;

import com.karas7171.atmossystem.core.AtmosManager;
import com.karas7171.atmossystem.core.AtmosZone;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AtmosScan {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("atmos")
                        .then(Commands.literal("scan")
                                .executes(AtmosScan::executeScan)
                        )
                        .then(Commands.literal("info")
                                .executes(AtmosScan::executeInfo)
                        )
        );
    }

    private static int executeScan(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayerOrException();
        BlockPos startPos = player.blockPosition();
        AtmosZone zone = AtmosManager.get().getZone(startPos);
        if (zone == null) {
            source.sendFailure(Component.literal("Зона атмосферы для этой позиции не найдена"));
            return 0;
        }

        zone.reportCellInfo(startPos, source);

        return Command.SINGLE_SUCCESS;
    }

    private static int executeInfo(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(
                () -> Component.literal("Сканирование текущего газового тайла"),
                false
        );
        return Command.SINGLE_SUCCESS;
    }
}
