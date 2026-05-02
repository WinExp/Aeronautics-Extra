package com.github.winexp.aeronauticsextra.compat.computercraft.peripherals.aero_extra;

import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSReceiverBlockEntity;
import dan200.computercraft.api.lua.LuaFunction;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSReceiverPeripheral extends SimPeripheral<GPSReceiverBlockEntity> {
    public GPSReceiverPeripheral(GPSReceiverBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final List<Double> getPosition() {
        Vec3 position = this.blockEntity.getCurrentPos();
        if (position == null) return null;
        else return List.of(position.x, position.y, position.z);
    }

    @Override
    public String getType() {
        return "aero_extra:gps_receiver";
    }
}
