package com.karas7171.atmossystem.comand;

import com.karas7171.atmossystem.core.AtmosManager;
import com.karas7171.atmossystem.core.logic.AtmosLogic;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class AtmosInitCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("init")
                .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getBFSLogic(), false))
                .then(Commands.literal("bench")
                        .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getBFSLogic(), true))
                )
                .then(Commands.literal("axial")
                        .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getAxialLogic(), false))
                        .then(Commands.literal("bench")
                                .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getAxialLogic(), true)))
                );
    }

    private static int executeInitSmart(CommandContext<CommandSourceStack> context, AtmosLogic logic, boolean showTime) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayerOrException();
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        if (AtmosManager.get().getZone(pos) != null) {
            source.sendFailure(Component.literal("Зона уже инициализирована"));
            return 0;
        }
        long start = System.nanoTime();
        AtmosManager.get().createAndAddZone(level, pos, logic);
        long end = System.nanoTime();

        String msg = "Зона инициализирована на " + pos.toShortString();

        if (showTime) {
            double time = (end - start) / 1_000_000_000.0;

            String timeMessage = "";
            if (time >= 60) {
                int minute = (int) (time / 60);
                double second = (time % 60);
                msg += String.format("\nВремя затрачено: %d минут, %.2f секунд", minute, second);
            } else {
                msg += String.format("\nВремя затрачено: %.3f секунд", time);
            }
        }
        String finalMsg = msg;
        source.sendSuccess(
                () -> Component.literal(finalMsg),
                false
        );

        return Command.SINGLE_SUCCESS;
    }
}
