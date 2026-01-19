package com.karas7171.atmossystem.comand;

import com.karas7171.atmossystem.core.AtmosManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class AtmosInitCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("atmos")
                        .then(Commands.literal("init")
                                .executes(AtmosInitCommand::executeInit)
                        )
                .then(Commands.literal("info")
                        .executes(AtmosInitCommand::executeInfo)
                )
        );
    }

    private static int executeInit(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayerOrException();
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        if (AtmosManager.get().getZone(pos) != null) {
            source.sendFailure(Component.literal("Зона уже инициализирована"));
            return 0;
        }
        AtmosManager.get().createAndAddZone(level, pos);

        source.sendSuccess(
                () -> Component.literal("Зона проверяется и инициализируется, на позиции: " + pos.toShortString()),
                false
        );

        return Command.SINGLE_SUCCESS;
    }

    private static int executeInfo(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(
                () -> Component.literal("Инициализация газовой системы"),
                false
        );
        return Command.SINGLE_SUCCESS;
    }
}
