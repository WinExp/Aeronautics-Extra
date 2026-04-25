package com.github.winexp.aeronauticsextra.compact.computercraft;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.compact.computercraft.peripherals.aeronautics.SteamVentPeripheral;
import com.github.winexp.aeronauticsextra.compact.computercraft.peripherals.create_propulsion.ThrusterPeripheral;
import com.github.winexp.aeronauticsextra.compact.computercraft.peripherals.simulated.AnalogTransmissionPeripheral;
import com.github.winexp.aeronauticsextra.compact.computercraft.peripherals.aeronautics.HotAirBurnerPeripheral;
import dev.createpropulsionsimulated.registry.CPSBlockEntities;
import dev.eriksonn.aeronautics.index.AeroBlockEntityTypes;
import dev.simulated_team.simulated.index.SimBlockEntityTypes;
import dev.simulated_team.simulated.service.compat.SimPeripheralService;
import net.neoforged.fml.ModList;

public class AeroExtraPeripherals {
    public static void init(SimPeripheralService service) {
        AeronauticsExtra.LOGGER.info("Loading Aeronautics peripherals...");
        service.addPeripheral(SimBlockEntityTypes.SIMPLE_BE, AnalogTransmissionPeripheral::new);
        service.addPeripheral(AeroBlockEntityTypes.HOT_AIR_BURNER, HotAirBurnerPeripheral::new);
        service.addPeripheral(AeroBlockEntityTypes.STEAM_VENT, SteamVentPeripheral::new);

        if (ModList.get().isLoaded("createpropulsionsimulated")) {
            AeronauticsExtra.LOGGER.info("Loading Create Propulsion: Simulated peripherals...");
            service.addPeripheral(CPSBlockEntities.THRUSTER, be -> new ThrusterPeripheral<>(be, "thruster"));
            service.addPeripheral(CPSBlockEntities.ION_THRUSTER, be -> new ThrusterPeripheral<>(be, "ion_thruster"));
            service.addPeripheral(CPSBlockEntities.CREATIVE_THRUSTER, be -> new ThrusterPeripheral<>(be, "creative_thruster"));
        }
    }
}
