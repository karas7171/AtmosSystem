package com.karas7171.atmossystem.init;

import com.karas7171.atmossystem.comand.AtmosInitCommand;
import com.karas7171.atmossystem.comand.AtmosRemoveCommand;
import com.karas7171.atmossystem.comand.AtmosScanCommand;
import com.karas7171.atmossystem.comand.AtmosViewCommand;
import net.minecraft.commands.Commands;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("atmos")
                        .then(AtmosInitCommand.register())
                        .then(AtmosScanCommand.register())
                        .then(AtmosRemoveCommand.register())
                        .then(AtmosViewCommand.register())
        );
    }
}
