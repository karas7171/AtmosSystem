package com.karas7171.atmossystem;

import com.karas7171.atmossystem.init.ModCommands;
import com.karas7171.atmossystem.init.ModItems;
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
        // === ШАГ 1: РЕГИСТРАЦИЯ ВСЕХ КОМПОНЕНТОВ ===
        // ВСЁ регистрируется через переданный IEventBus
        // Это ОФИЦИАЛЬНЫЙ ПАТТЕРН NeoForge

        // 1. Регистрируем предметы (наш менеджер предметов)
        ModItems.register(modEventBus);

        // 2. Регистрируем конфиг (ТАК ЖЕ, как предметы!)
        Config.register(modEventBus);  // ← ВОТ ОН, ПРАВИЛЬНЫЙ ВЫЗОВ!

        // 3. Регистрируем блоки (когда появятся)
        // ModBlocks.register(modEventBus);

        // === ШАГ 2: СТАНДАРТНЫЕ НАСТРОЙКИ ===
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("✅ Мод '{}' инициализирован. Все компоненты зарегистрированы.", MOD_ID);

        NeoForge.EVENT_BUS.register(ModCommands.class);
    }

    // ========== COMMON SETUP ==========
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Выполняется после регистрации ВСЕГО
        // Здесь можно: сетевые пакеты, межмодовое взаимодействие и т.д.
        LOGGER.info("⚙️ Common setup выполнен.");
    }

    // ========== СЕРВЕРНЫЕ СОБЫТИЯ ==========
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Выполняется при запуске сервера/одиночного мира
        LOGGER.info("🌍 Сервер запущен.");
    }

}