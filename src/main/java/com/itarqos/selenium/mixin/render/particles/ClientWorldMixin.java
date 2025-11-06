package com.itarqos.selenium.mixin.render.particles;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void selenium$filterParticles1(ParticleEffect effect, double x, double y, double z, double vx, double vy, double vz, CallbackInfo ci) {
        if (shouldBlock(effect)) {
            ci.cancel();
        }
    }

    // Boolean overload is addImportantParticle in Yarn 1.21.4
    @Inject(method = "addImportantParticle(Lnet/minecraft/particle/ParticleEffect;ZDDDDDD)V", at = @At("HEAD"), cancellable = true)
    private void selenium$filterParticles2(ParticleEffect effect, boolean alwaysSpawn, double x, double y, double z, double vx, double vy, double vz, CallbackInfo ci) {
        if (shouldBlock(effect)) {
            ci.cancel();
        }
    }

    private static boolean shouldBlock(ParticleEffect effect) {
        SeleniumConfig cfg = SeleniumClient.CONFIG;
        if (cfg == null) return false;
        if (cfg.disableAllParticles) return true;
        Identifier id = Registries.PARTICLE_TYPE.getId(effect.getType());
        return id != null && cfg.disabledParticleIds.contains(id.toString());
    }
}
