package com.karas7171.atmossystem;

import com.karas7171.atmossystem.init.ModCommands;
import com.karas7171.atmossystem.init.ModItems;
import com.karas7171.atmossystem.init.ModParticles;
import com.karas7171.atmossystem.util.AtmosVisualizer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

@Mod(Atmossystem.MOD_ID)
public class Atmossystem {

    public static final String MOD_ID = "atmossystem";
    public static final Logger LOGGER = LogUtils.getLogger();

    // ========== КОНСТРУКТОР МОДА ==========
    public Atmossystem(IEventBus modEventBus, ModContainer modContainer) {

        ModItems.register(modEventBus);

        Config.register(modEventBus);

        ModParticles.PARTICLES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        NeoForge.EVENT_BUS.register(ModCommands.class);
        NeoForge.EVENT_BUS.register(AtmosVisualizer.class);

        LOGGER.info("✅ Мод '{}' инициализирован. Все компоненты зарегистрированы.", MOD_ID);
    }

    // ========== COMMON SETUP ==========
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("⚙️ Common setup выполнен.");
    }

    // ========== СЕРВЕРНЫЕ СОБЫТИЯ ==========
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("🌍 Сервер запущен.");
    }

}