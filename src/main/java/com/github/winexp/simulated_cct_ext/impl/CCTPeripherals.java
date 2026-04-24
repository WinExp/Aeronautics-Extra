package com.github.winexp.simulated_cct_ext.impl;

import com.github.winexp.simulated_cct_ext.impl.peripherals.aeronautics.SteamVentPeripheral;
import com.github.winexp.simulated_cct_ext.impl.peripherals.simulated.AnalogTransmissionPeripheral;
import com.github.winexp.simulated_cct_ext.impl.peripherals.aeronautics.HotAirBurnerPeripheral;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.simulated_team.simulated.service.compat.SimPeripheralService;

public class CCTPeripherals {
    public static void init(SimPeripheralService service) {
        service.addPeripheral(SimBlockEntityTypes.SIMPLE_BE, AnalogTransmissionPeripheral::new);
        service.addPeripheral(AeroBlockEntityTypes.HOT_AIR_BURNER, HotAirBurnerPeripheral::new);
        service.addPeripheral(AeroBlockEntityTypes.STEAM_VENT, SteamVentPeripheral::new);
    }
}
