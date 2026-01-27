package com.karas7171.atmossystem.client.particle;

import com.karas7171.atmossystem.init.ModParticles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = "atmossystem", value = Dist.CLIENT)
public class ClientRegistration {
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.O2_PARTICLE.get(), spriteSet ->
                (type, level, x, y, z, vx, vy, vz) -> {
            O2Particle p = new O2Particle(level, x, y, z, vx, vy, vz);
            p.pickSprite(spriteSet);
            return p;
                });
    }
}
