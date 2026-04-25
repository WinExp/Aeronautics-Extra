package com.github.winexp.aeronauticsextra.compact.computercraft.peripherals.simulated;

import com.github.winexp.aeronauticsextra.mixin_interface.simulated.AnalogTransmissionBlockEntityExtension;
import dan200.computercraft.api.lua.*;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;
import dev.simulated_team.simulated.content.blocks.analog_transmission.AnalogTransmissionBlockEntity;

public class AnalogTransmissionPeripheral extends SimPeripheral<AnalogTransmissionBlockEntity> {
    public AnalogTransmissionPeripheral(final AnalogTransmissionBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final float getOverridePower() {
        return ((AnalogTransmissionBlockEntityExtension) this.blockEntity).aero_extra$getOverrideSignal();
    }

    @LuaFunction
    public final void setOverridePower(final double power) {
        ((AnalogTransmissionBlockEntityExtension) this.blockEntity).aero_extra$setOverrideSignal((float) power);
    }

    @Override
    public String getType() {
        return "analog_transmission";
    }
}
