package com.karas7171.atmossystem.comand;

import com.karas7171.atmossystem.core.AtmosManager;
import com.karas7171.atmossystem.core.AtmosZone;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Function;

public class AtmosSetCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        Function<SetType, ArgumentBuilder<CommandSourceStack, ?>> buildFinalArgs = (type) ->
                Commands.argument("gas", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (Gases g : Gases.values()) builder.suggest(g.name());
                            return builder.buildFuture();
                        })
                        .executes(context -> extractAndExecute(context, type, false))
                        .then(Commands.literal("safe")
                                .executes(context ->  extractAndExecute(context, type, true)));

        Function<SetType, ArgumentBuilder<CommandSourceStack, ?>> buildArguments = (type) ->
                Commands.argument("moles", FloatArgumentType.floatArg(0.01f))
                        .then(Commands.argument("temperature", FloatArgumentType.floatArg(2.7f))
                                .then(buildFinalArgs.apply(type)));

        var set0 = Commands.literal("0")
                .executes(context -> executeSet(context, SetType.ZONE, Gases.O2, 0.01f, 2.7f, false));

        return Commands.literal("set")
                .then(Commands.literal("block").then(buildArguments.apply(SetType.BLOCK)))
                .then(Commands.literal("zone").then(buildArguments.apply(SetType.ZONE)))
                .then(set0);
    }

    private static int extractAndExecute(CommandContext<CommandSourceStack> context, SetType type, boolean safe) throws CommandSyntaxException {
        float moles = FloatArgumentType.getFloat(context, "moles");
        float temperature = FloatArgumentType.getFloat(context, "temperature");
        Gases gas = Gases.valueOf(StringArgumentType.getString(context, "gas").toUpperCase());
        return executeSet(context, type, gas, moles, temperature, safe);
    }

    private static int executeSet(CommandContext<CommandSourceStack> context, SetType type, Gases gas, float moles, float temp, boolean safe) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();

        ServerPlayer player = source.getPlayerOrException();
        BlockPos startPos = player.blockPosition();
        AtmosZone zone = AtmosManager.get().getZone(startPos);
        if (zone == null) {
            source.sendFailure(Component.literal("Зона не найдена!")
                    .withStyle(ChatFormatting.RED)
            );
            return 0;
        }
        int index = zone.getIndex(startPos);
        if (type == SetType.BLOCK) {
            zone.injectGas(index, moles, temp);
        }
        if (type == SetType.ZONE) {
            zone.setGasToAll(moles, temp);
        }

        if (!safe) {
            zone.setDirty(true);
        }
        return Command.SINGLE_SUCCESS;
    }

    enum SetType {
        BLOCK,
        ZONE
    }

    enum Gases {
        O2
    }
}