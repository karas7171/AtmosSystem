package com.karas7171.atmossystem.core;

import com.karas7171.atmossystem.Atmossystem;
import com.karas7171.atmossystem.util.AtmosVisualizer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Atmossystem.MOD_ID)

public class ServerTickHandler {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
            AtmosSolver.tick();
            AtmosVisualizer.tick();
    }
}

