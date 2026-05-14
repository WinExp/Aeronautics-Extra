package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.client.gui.GPSSatelliteConfigScreen;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.GPSSatelliteConfigMenu;
import com.tterrag.registrate.util.entry.MenuEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;

public class AeroExtraMenuTypes {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final MenuEntry<GPSSatelliteConfigMenu> GPS_SATELLITE_CONFIG = REGISTRATE
            .menu("gps_satellite_config", GPSSatelliteConfigMenu::new, () -> GPSSatelliteConfigScreen::new)
            .register();

    public static void register() {}
}
