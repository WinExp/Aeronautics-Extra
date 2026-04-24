package com.github.winexp.simulated_cct_ext.impl.peripherals.aeronautics;

import com.github.winexp.simulated_cct_ext.mixin_interface.aeronautics.HotAirBurnerBlockEntityExtension;
import dan200.computercraft.api.lua.LuaFunction;
import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;

public class HotAirBurnerPeripheral extends SimPeripheral<HotAirBurnerBlockEntity> {
    public HotAirBurnerPeripheral(HotAirBurnerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final int getHotAirAmount() {
        return ((HotAirBurnerBlockEntityExtension) this.blockEntity).sce$getHotAirAmount();
    }

    @LuaFunction
    public final void setHotAirAmount(int amount) {
        ((HotAirBurnerBlockEntityExtension) this.blockEntity).sce$setHotAirAmount(amount);
    }

    @Override
    public String getType() {
        return "hot_air_burner";
    }
}
