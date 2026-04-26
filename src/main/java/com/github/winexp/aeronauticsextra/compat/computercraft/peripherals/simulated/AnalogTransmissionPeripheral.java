package com.github.winexp.aeronauticsextra.compat.computercraft.peripherals.simulated;

import com.github.winexp.aeronauticsextra.mixin_interface.simulated.AnalogTransmissionBlockEntityExtension;
import dan200.computercraft.api.lua.*;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;
import dev.simulated_team.simulated.content.blocks.analog_transmission.AnalogTransmissionBlockEntity;

public class AnalogTransmissionPeripheral extends SimPeripheral<AnalogTransmissionBlockEntity> {
    public AnalogTransmissionPeripheral(final AnalogTransmissionBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final float getOverrideGearRatio() {
        return ((AnalogTransmissionBlockEntityExtension) this.blockEntity).aero_extra$getOverrideGearRatio();
    }

    @LuaFunction
    public final void setOverrideGearRatio(final double ratio) {
        ((AnalogTransmissionBlockEntityExtension) this.blockEntity).aero_extra$setOverrideGearRatio((float) ratio);
    }

    @Override
    public String getType() {
        return "analog_transmission";
    }
}
