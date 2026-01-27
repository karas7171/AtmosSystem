package com.karas7171.atmossystem.init;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, "atmossystem");

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> O2_PARTICLE =
            PARTICLES.register("o2_particle", () -> new SimpleParticleType(false));
}
