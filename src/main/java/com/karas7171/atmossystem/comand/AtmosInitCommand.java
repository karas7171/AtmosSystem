package com.karas7171.atmossystem.comand;

import com.karas7171.atmossystem.core.AtmosManager;
import com.karas7171.atmossystem.core.logic.AtmosLogic;
import com.karas7171.atmossystem.core.logic.AtmosProgressListener;
import com.karas7171.atmossystem.util.AtmosVisualizer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class AtmosInitCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        var axial = Commands.literal("axial")
                .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getAxialLogic(), false, false))
                .then(Commands.literal("visual")
                        .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getAxialLogic(), false, true))
                        .then(Commands.literal("bench")
                                .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getAxialLogic(), true, true))))
                .then(Commands.literal("bench")
                        .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getAxialLogic(), true, false)));

        var visual = Commands.literal("visual")
                .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getBFSLogic(), false, true))
                .then(Commands.literal("bench")
                        .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getBFSLogic(), true, true)));

        var bench = Commands.literal("bench")
                .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getBFSLogic(), true, false));


        return Commands.literal("init")
                .executes(ctx -> executeInitSmart(ctx, AtmosManager.get().getBFSLogic(), false, false))
                .then(axial)
                .then(visual)
                .then(bench);
    }

    private static int executeInitSmart(CommandContext<CommandSourceStack> context, AtmosLogic logic, boolean showTime, boolean isVisual) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayerOrException();
        Level level = player.level();
        BlockPos pos = player.blockPosition();
        ServerLevel serverLevel = player.level();
        if (AtmosManager.get().getZone(pos) != null) {
            source.sendFailure(Component.literal("Зона уже инициализирована"));
            return 0;
        }

        AtmosProgressListener listener = isVisual
                ? (current, next) -> AtmosVisualizer.visualize(serverLevel, current, next)
                : null;

        long start = System.nanoTime();
        AtmosManager.get().createAndAddZone(level, pos, logic, listener);
        long end = System.nanoTime();

        String msg = "Зона инициализирована на " + pos.toShortString();

        if (showTime) {
            double time = (end - start) / 1_000_000_000.0;

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
