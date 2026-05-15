package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.client.gui.GPSSatelliteConfigScreen;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.SatelliteConfigMenu;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;

public class AeroExtraMenuTypes {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final MenuEntry<SatelliteConfigMenu> GPS_SATELLITE_CONFIG = REGISTRATE
            .menu("gps_satellite_config", SatelliteConfigMenu::new, () -> GPSSatelliteConfigScreen::new)
            .register();

    public static void register() {}
}
