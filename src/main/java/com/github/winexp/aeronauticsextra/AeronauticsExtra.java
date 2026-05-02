package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.data.AeroExtraDataGen;
import com.github.winexp.aeronauticsextra.neoforge.AeroExtraModBusEventHandler;
import com.github.winexp.aeronauticsextra.neoforge.AeroExtraNeoForgeEventHandler;
import com.github.winexp.aeronauticsextra.registry.*;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;

@Mod(AeronauticsExtra.MOD_ID)
public class AeronauticsExtra {
    private static final NonNullSupplier<CreateRegistrate> REGISTRATE = NonNullSupplier.lazy(() -> CreateRegistrate.create(AeronauticsExtra.MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
            .setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)))));
    public static final String MOD_ID = "aeronauticsextra";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static CreateRegistrate getRegistrate() {
        return REGISTRATE.get();
    }

    public AeronauticsExtra(IEventBus modEventBus, ModContainer modContainer) {
        getRegistrate().registerEventListeners(modEventBus);
        getRegistrate().setCreativeTab(AeroExtraCreativeTabs.AERO_EXTRA);

        AeroExtraItems.register();
        AeroExtraDataComponents.register(modEventBus);
        AeroExtraBlocks.register();
        AeroExtraBlockEntityTypes.register();
        AeroExtraCreativeTabs.register(modEventBus);
        AeroExtraMenuTypes.register();

        NeoForge.EVENT_BUS.register(AeroExtraNeoForgeEventHandler.class);
        modEventBus.register(AeroExtraDataGen.class);
        modEventBus.register(AeroExtraModBusEventHandler.class);
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
