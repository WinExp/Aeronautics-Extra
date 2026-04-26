package com.github.winexp.aeronauticsextra.mixin.simulated;

import com.github.winexp.aeronauticsextra.mixin_interface.simulated.GimbalSensorBlockEntityExtension;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.simulated_team.simulated.compat.computercraft.peripherals.GimbalSensorPeripheral;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(GimbalSensorPeripheral.class)
public abstract class GimbalSensorPeripheralMixin extends SimPeripheral<GimbalSensorBlockEntity> {
    public GimbalSensorPeripheralMixin(GimbalSensorBlockEntity blockEntity) {
        super(blockEntity);
    }

    @WrapMethod(method = "getAngles")
    private List<Double> addYAngle(Operation<List<Double>> original) {
        double yAngle = Math.toDegrees(((GimbalSensorBlockEntityExtension) this.blockEntity).aero_extra$getYAngle());
        ArrayList<Double> angles = new ArrayList<>(original.call());
        angles.add(yAngle);
        return angles;
    }
}
