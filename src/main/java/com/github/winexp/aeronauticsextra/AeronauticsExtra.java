package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.blocks.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.blocks.AeroExtraBlocks;
import com.github.winexp.aeronauticsextra.logistics.AeroExtraMenuTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraCreativeTabs;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
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
    public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.of(() -> Registrate.create(AeronauticsExtra.MOD_ID));

    public AeronauticsExtra(IEventBus modEventBus, ModContainer modContainer) {
        NeoForge.EVENT_BUS.register(AeroExtraNeoForgeEventHandler.class);
        modEventBus.register(AeroExtraModEventHandler.class);

        AeroExtraBlocks.register();
        AeroExtraBlockEntityTypes.register();
        AeroExtraCreativeTabs.register();
        AeroExtraMenuTypes.register();
    }
}
