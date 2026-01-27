package com.karas7171.atmossystem.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;

public class O2Particle extends TextureSheetParticle {
    protected O2Particle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz);
        this.friction = 1.0F;
        this.xd = vx; this.yd = vy; this.zd = vz;
        this.quadSize = 0.375F;
        this.lifetime = 60;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0F - ((float)this.age / (float)this.lifetime);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return  ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
