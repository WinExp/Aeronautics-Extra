package com.github.winexp.aeronauticsextra.mixin.create;

import com.github.winexp.aeronauticsextra.mixin_interface.create.CogwheelPlacementExtensionExt;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.simulated_team.simulated.util.placement_helpers.CogwheelPlacementExtension;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(CogwheelPlacementExtension.class)
public class CogwheelPlacementExtensionMixin implements CogwheelPlacementExtensionExt {
    @Unique
    private Function<BlockState, Direction.Axis> aero_extra$axisProvider = state -> null;

    @WrapOperation(method = "getOffset", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasProperty(Lnet/minecraft/world/level/block/state/properties/Property;)Z", ordinal = 0))
    private boolean redirectAxis(BlockState state, Property<Direction.Axis> property, Operation<Boolean> original,
                                 @Share("axis") LocalRef<Direction.Axis> axisRef) {
        Direction.Axis axis = this.aero_extra$axisProvider.apply(state);
        axisRef.set(axis);
        if (axis != null) {
            return true;
        }
        return original.call(state, property);
    }

    @WrapOperation(method = "getOffset", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;", ordinal = 0))
    private Comparable<Direction.Axis> modifyAxis(BlockState state, Property<Direction.Axis> property, Operation<Comparable<Direction.Axis>> original,
                                                  @Share("axis") LocalRef<Direction.Axis> axisRef) {
        Direction.Axis axis = axisRef.get();
        if (axis != null) {
            return axis;
        }
        return original.call(state, property);
    }

    @Override
    public void aero_extra$setAxisProvider(@NotNull Function<BlockState, Direction.Axis> modifier) {
        this.aero_extra$axisProvider = modifier;
    }
}
