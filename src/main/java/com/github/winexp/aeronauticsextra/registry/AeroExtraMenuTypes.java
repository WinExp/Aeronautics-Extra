package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ConfigMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AeroExtraMenuTypes {
    private static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.MENU, AeronauticsExtra.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<ConfigMenu>> GPS_SATELLITE_CONFIG = REGISTER
            .register("gps_satellite", () -> IMenuTypeExtension.create(ConfigMenu::new));

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
