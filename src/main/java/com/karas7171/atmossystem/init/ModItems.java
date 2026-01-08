package com.karas7171.atmossystem.init;

import com.karas7171.atmossystem.Atmossystem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Atmossystem.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        Atmossystem.LOGGER.debug("[AtmosSystem] Registered Items");
    }
}
