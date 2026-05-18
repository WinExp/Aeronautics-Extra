package com.github.winexp.aeronauticsextra.mixin_interface.create;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public interface CogwheelPlacementExtensionExt {
    void aero_extra$setAxisProvider(Function<BlockState, Direction.Axis> provider);
}
