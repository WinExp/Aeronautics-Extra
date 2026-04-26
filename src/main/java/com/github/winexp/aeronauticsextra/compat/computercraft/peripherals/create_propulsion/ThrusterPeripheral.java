package com.github.winexp.aeronauticsextra.compat.computercraft.peripherals.create_propulsion;

import com.github.winexp.aeronauticsextra.mixin_interface.create_propulsion.ThrusterBlockEntityExtension;
import dan200.computercraft.api.lua.LuaFunction;
import dev.createpropulsionsimulated.content.thruster.ThrusterBlockEntity;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;

public class ThrusterPeripheral<T extends ThrusterBlockEntity> extends SimPeripheral<T> {
    private final String name;

    public ThrusterPeripheral(T blockEntity, String name) {
        super(blockEntity);
        this.name = name;
    }

    @LuaFunction
    public double getOverrideThrottle() {
        return ((ThrusterBlockEntityExtension) this.blockEntity).aero_extra$getOverrideThrottle();
    }

    @LuaFunction
    public void setOverrideThrottle(final double throttle) {
        ((ThrusterBlockEntityExtension) this.blockEntity).aero_extra$setOverrideThrottle(throttle);
    }

    @Override
    public String getType() {
        return "cps:" + this.name;
    }
}
