package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.neoforge.AeroExtraModBusEventHandler;
import com.github.winexp.aeronauticsextra.neoforge.AeroExtraNeoForgeEventHandler;
import com.github.winexp.aeronauticsextra.registry.*;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(AeronauticsExtra.MOD_ID)
public class AeronauticsExtra {
    public static final String MOD_ID = "aeronauticsextra";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AeronauticsExtra(IEventBus modEventBus, ModContainer modContainer) {
        AeroExtraDataComponents.register(modEventBus);
        AeroExtraBlocks.register(modEventBus);
        AeroExtraBlockEntityTypes.register(modEventBus);
        AeroExtraItems.register(modEventBus);
        AeroExtraCreativeTabs.register(modEventBus);
        AeroExtraMenuTypes.register(modEventBus);

        NeoForge.EVENT_BUS.register(AeroExtraNeoForgeEventHandler.class);
        modEventBus.register(AeroExtraModBusEventHandler.class);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
