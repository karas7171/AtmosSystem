package com.karas7171.atmossystem;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.IEventBus;

public class Config {

    // ========== СПЕЦИФИКАЦИЯ КОНФИГА ==========
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_DEBUG_LOG;
    public static final ModConfigSpec.IntValue MAGIC_NUMBER;
    public static final ModConfigSpec.ConfigValue<String> SYSTEM_NAME;

    public static boolean enableDebugLog;
    public static int magicNumber;
    public static String systemName;

    static {
        BUILDER.push("General");

        ENABLE_DEBUG_LOG = BUILDER
                .comment("Включить подробное логирование в консоль.")
                .define("enableDebugLog", false);

        MAGIC_NUMBER = BUILDER
                .comment("Просто магическое число для примера.")
                .defineInRange("magicNumber", 42, 0, 100);

        SYSTEM_NAME = BUILDER
                .comment("Название вашей системы.")
                .define("systemName", "AtmosSystem");

        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static final ModConfigSpec SPEC;

    // ========== МЕТОД РЕГИСТРАЦИИ ==========
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(Config::onConfigLoad);
    }

    // ========== ОБРАБОТЧИК СОБЫТИЯ ==========
    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent.Loading event) {
        // Проверяем, что это КОНФИГ НАШЕГО МОДА
        if (event.getConfig().getSpec() == Config.SPEC) {
            enableDebugLog = ENABLE_DEBUG_LOG.get();
            magicNumber = MAGIC_NUMBER.get();
            systemName = SYSTEM_NAME.get();

            Atmossystem.LOGGER.info("✅ Конфигурация загружена: {}, debug={}, magic={}",
                    systemName, enableDebugLog, magicNumber);
        }
    }
}