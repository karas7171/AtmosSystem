package com.karas7171.atmossystem;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    static {
        BUILDER.push("General");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static final ModConfigSpec SPEC;

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == Config.SPEC) {

            Atmossystem.LOGGER.info("AtmosSystem configuration loaded.");
        }
    }
}