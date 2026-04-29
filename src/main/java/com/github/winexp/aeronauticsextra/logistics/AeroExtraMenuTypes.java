package com.github.winexp.aeronauticsextra.logistics;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.logistics.gps.gui.ConfigMenu;
import com.github.winexp.aeronauticsextra.logistics.gps.gui.ConfigScreen;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.inventory.MenuType;

public class AeroExtraMenuTypes {
    private static final Registrate REGISTRATE = AeronauticsExtra.REGISTRATE.get();

    public static final RegistryEntry<MenuType<?>, MenuType<ConfigMenu>> GPS_SATELLITE_CONFIG = REGISTRATE.menu("gps_satellite", ConfigMenu::new, () -> ConfigScreen::new)
            .register();

    public static void register() {}
}
