package com.github.winexp.aeronauticsextra.mixin.create_propulsion;

import com.github.winexp.aeronauticsextra.mixin_interface.create_propulsion.ThrusterBlockEntityExtension;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.createpropulsionsimulated.content.thruster.ThrusterBlockEntity;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrusterBlockEntity.class)
public class ThrusterBlockEntityMixin implements ThrusterBlockEntityExtension {
    @Unique
    private double aero_extra$overrideThrust = -1;

    @WrapMethod(method = "getThrottle")
    private double modifyThrottle(Operation<Double> original) {
        if (this.aero_extra$overrideThrust < 0) return original.call();
        else return this.aero_extra$overrideThrust;
    }

    @Inject(method = "setRedstonePower", at = @At(value = "INVOKE", target = "Ldev/createpropulsionsimulated/content/thruster/ThrusterBlockEntity;sync()V"))
    private void overrideWhenRedstoneUpdated(int redstonePower, CallbackInfo ci) {
        this.aero_extra$overrideThrust = -1;
    }

    @Override
    public double aero_extra$getOverrideThrust() {
        return this.aero_extra$overrideThrust;
    }

    @Override
    public void aero_extra$setOverrideThrust(double power) {
        power = Mth.clamp(power, -1, 1);
        if (power < 0) power = -1;
        this.aero_extra$overrideThrust = power;
    }
}
