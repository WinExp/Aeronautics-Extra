package com.github.winexp.aeronauticsextra.mixin.simulated;

import com.github.winexp.aeronauticsextra.mixin_interface.simulated.AnalogTransmissionBlockEntityExtension;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.simulated_team.simulated.content.blocks.analog_transmission.AnalogTransmissionBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnalogTransmissionBlockEntity.class)
public abstract class AnalogTransmissionBlockEntityMixin extends KineticBlockEntity implements AnalogTransmissionBlockEntityExtension {
    @Unique
    private float sce$overrideSignal = -1;

    @Unique
    private boolean sce$needsUpdate = true;

    @Shadow
    private boolean oversaturated;

    @Shadow
    @Final
    private AnalogTransmissionBlockEntity.AnalogTransmissionCogwheel extraWheel;

    public AnalogTransmissionBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Definition(id = "signal", field = "Ldev/simulated_team/simulated/content/blocks/analog_transmission/AnalogTransmissionBlockEntity;signal:I")
    @Expression("? != this.signal")
    @ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean shouldUpdate(boolean original) {
        return this.sce$needsUpdate || original;
    }

    @WrapMethod(method = "propagateRotationTo")
    private float modifyPower(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs, Operation<Float> original) {
        AnalogTransmissionBlockEntity instance = (AnalogTransmissionBlockEntity) (Object) this;
        if (this.sce$overrideSignal < 0 || (target != this.extraWheel && target != instance)) return original.call(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
        else if (this.sce$overrideSignal > 1) {
            this.oversaturated = true;
            return 0;
        }
        else {
            this.oversaturated = false;
            return this.sce$overrideSignal > 1 ? 1 : this.sce$overrideSignal;
        }
    }

    @Override
    public float aero_extra$getOverrideSignal() {
        return this.sce$overrideSignal;
    }

    @Override
    public void aero_extra$setOverrideSignal(float power) {
        if (this.sce$overrideSignal != power) {
            this.sce$overrideSignal = power;
            this.sce$needsUpdate = true;
        }
    }
}
