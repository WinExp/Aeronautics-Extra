package com.github.winexp.aeronauticsextra.compat.computercraft.peripherals.aero_extra;

import com.github.winexp.aeronauticsextra.content.blocks.kinetics.CVTGearshiftBlockEntity;
import dan200.computercraft.api.lua.LuaFunction;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;

public class CVTGearshiftPeripheral extends SimPeripheral<CVTGearshiftBlockEntity> {
    public CVTGearshiftPeripheral(CVTGearshiftBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public float getRatio() {
        return this.blockEntity.getRatio();
    }

    @LuaFunction
    public void setRatio(double ratio) {
        this.blockEntity.setRatio((float) ratio);
    }

    @LuaFunction
    public void offsetRatio(float offset) {
        this.blockEntity.setRatio(this.blockEntity.getRatio() + offset);
    }

    @Override
    public String getType() {
        return "aero_extra:cvt_gearshift";
    }
}
