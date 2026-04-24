package com.github.winexp.simulated_cct_ext.impl.peripherals.aeronautics;

import com.github.winexp.simulated_cct_ext.mixin_interface.aeronautics.HotAirBurnerBlockEntityExtension;
import dan200.computercraft.api.lua.LuaFunction;
import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import dev.simulated_team.simulated.compat.computercraft.peripherals.SimPeripheral;

public class SteamVentPeripheral extends SimPeripheral<SteamVentBlockEntity> {
    public SteamVentPeripheral(SteamVentBlockEntity blockEntity) {
        super(blockEntity);
    }

    @LuaFunction
    public final int getSteamAmount() {
        return ((HotAirBurnerBlockEntityExtension) this.blockEntity).sce$getHotAirAmount();
    }

    @LuaFunction
    public final void setSteamAmount(int amount) {
        ((HotAirBurnerBlockEntityExtension) this.blockEntity).sce$setHotAirAmount(amount);
    }

    @Override
    public String getType() {
        return "steam_vent";
    }
}
