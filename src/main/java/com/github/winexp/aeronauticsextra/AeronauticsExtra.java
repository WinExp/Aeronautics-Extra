package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.registry.*;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(AeronauticsExtra.MOD_ID)
public class AeronauticsExtra {
    public static final String MOD_ID = "aeronauticsextra";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final NonNullSupplier<SimulatedRegistrate> REGISTRATE = NonNullSupplier.lazy(() ->
            (SimulatedRegistrate) new SimulatedRegistrate(AeroExtraCreativeTabs.GEOMATICS, MOD_ID)
                    .defaultCreativeTab((ResourceKey<CreativeModeTab>) null));

    public AeronauticsExtra(IEventBus modEventBus, ModContainer modContainer) {
        setTooltips();
        getRegistrate().registerEventListeners(modEventBus);
        AeroExtraDataComponents.register(modEventBus);
        AeroExtraBlocks.register();
        AeroExtraBlockEntityTypes.register();
        AeroExtraItems.register();
        AeroExtraEntityTypes.register();
        AeroExtraMenuTypes.register();

        AeroExtraDisplaySources.register();
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    private static void setTooltips() {
        getRegistrate().setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        );
    }

    public static SimulatedRegistrate getRegistrate() {
        return REGISTRATE.get();
    }
}
