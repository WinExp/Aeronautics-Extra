package com.github.winexp.simulated_cct_ext.impl.peripherals.simulated;

import com.github.winexp.simulated_cct_ext.mixin_interface.simulated.AnalogTransmissionBlockEntityExtension;
import dan200.computercraft.api.lua.*;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;
import dev.simulated_team.simulated.content.blocks.analog_transmission.AnalogTransmissionBlockEntity;

public class AnalogTransmissionPeripheral extends SimPeripheral<AnalogTransmissionBlockEntity> {
    public AnalogTransmissionPeripheral(final AnalogTransmissionBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final float getOverridePower() {
        return ((AnalogTransmissionBlockEntityExtension) this.blockEntity).sce$getOverrideSignal();
    }

    @LuaFunction
    public final void setOverridePower(final double power) {
        float value = power < 0 ? -1 : (float) power;
        ((AnalogTransmissionBlockEntityExtension) this.blockEntity).sce$setOverrideSignal(value);
    }

    @Override
    public String getType() {
        return "analog_transmission";
    }
}
