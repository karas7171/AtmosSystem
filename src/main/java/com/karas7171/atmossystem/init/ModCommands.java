package com.karas7171.atmossystem.init;

import com.karas7171.atmossystem.comand.*;
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
                        .then(AtmosUpdateCommand.register())
                        .then(AtmosSetCommand.register()));
    }
}
