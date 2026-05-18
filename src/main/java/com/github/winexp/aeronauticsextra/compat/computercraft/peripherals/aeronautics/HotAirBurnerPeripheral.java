package com.github.winexp.aeronauticsextra.compat.computercraft.peripherals.aeronautics;

import com.github.winexp.aeronauticsextra.mixin_interface.aeronautics.HotAirBurnerBlockEntityExtension;
import dan200.computercraft.api.lua.LuaFunction;
import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;

public class HotAirBurnerPeripheral extends SimPeripheral<HotAirBurnerBlockEntity> {
    public HotAirBurnerPeripheral(HotAirBurnerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final int getHotAirAmount() {
        return ((HotAirBurnerBlockEntityExtension) this.blockEntity).aero_extra$getHotAirAmount();
    }

    @LuaFunction
    public final void setHotAirAmount(int amount) {
        ((HotAirBurnerBlockEntityExtension) this.blockEntity).aero_extra$setHotAirAmount(amount);
    }

    @Override
    public String getType() {
        return "hot_air_burner";
    }
}
