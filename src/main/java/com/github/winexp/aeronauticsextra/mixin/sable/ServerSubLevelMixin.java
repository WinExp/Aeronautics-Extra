package com.github.winexp.aeronauticsextra.mixin.sable;

import com.github.winexp.aeronauticsextra.content.entity.physics.EntitySubLevelActor;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.plot.ServerLevelPlot;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSubLevel.class)
public abstract class ServerSubLevelMixin {
    @Shadow
    public abstract ServerLevelPlot getPlot();

    @Inject(method = "prePhysicsTick", at = @At("HEAD"))
    private void onSubLevelPhysicsTick(SubLevelPhysicsSystem physicsSystem, RigidBodyHandle handle, double timeStep, CallbackInfo ci) {
        ServerSubLevel subLevel = (ServerSubLevel) (Object) this;
        ServerLevelPlot plot = this.getPlot();
        if (plot.getLoadedChunks().isEmpty()) return;
        for (Entity entity : physicsSystem.getLevel().getAllEntities()) {
            if (entity instanceof EntitySubLevelActor actor) {
                actor.subLevelPhysicsTick(subLevel, handle, timeStep);
            }
        }
    }
}
