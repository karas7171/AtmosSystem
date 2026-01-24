package com.karas7171.atmossystem.comand;

import com.karas7171.atmossystem.core.AtmosManager;
import com.karas7171.atmossystem.core.AtmosZone;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class AtmosRemoveCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("remove")
                .executes(AtmosRemoveCommand::executeRemove);
    }

    private static int executeRemove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayerOrException();
        BlockPos startPos = player.blockPosition();
        AtmosZone zone = AtmosManager.get().getZone(startPos);
        if (zone == null) {
            source.sendFailure(Component.literal("Зона атмосферы для этой позиции не найдена")
                    .withStyle(ChatFormatting.RED)
            );
            return 0;
        }

        AtmosManager.get().removeZone(zone);
        source.sendSuccess(
                () -> Component.literal("Зона удалена"), false
        );

        return Command.SINGLE_SUCCESS;
    }
}

