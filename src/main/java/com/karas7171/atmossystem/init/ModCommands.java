package com.karas7171.atmossystem.init;

import com.karas7171.atmossystem.comand.AtmosInitCommand;
import com.karas7171.atmossystem.comand.AtmosScan;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        AtmosInitCommand.register(event.getDispatcher());
        AtmosScan.register(event.getDispatcher());
    }
}
