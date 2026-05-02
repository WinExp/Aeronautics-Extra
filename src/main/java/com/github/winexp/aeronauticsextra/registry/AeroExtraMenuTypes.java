package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ConfigMenu;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ConfigScreen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.inventory.MenuType;

public class AeroExtraMenuTypes {
    private static final CreateRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final RegistryEntry<MenuType<?>, MenuType<ConfigMenu>> GPS_SATELLITE_CONFIG = REGISTRATE
            .menu("gps_satellite", ConfigMenu::new, () -> ConfigScreen::new)
            .register();

    public static void register() {}
}
