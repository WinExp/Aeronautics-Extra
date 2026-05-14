package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.display_source.GPSReceiverDisplaySource;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;

public class AeroExtraDisplaySources {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final RegistryEntry<DisplaySource, GPSReceiverDisplaySource> GPS_RECEIVER = REGISTRATE
            .displaySource("gps_receiver", GPSReceiverDisplaySource::new)
            .register();

    public static void register() {}
}
