package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredItem;

public class AeroExtraCreativeTabs {
    public static final ResourceLocation GEOMATICS = AeronauticsExtra.asResource("geomatics");

    public static void register() {
        addItemsToSection(GEOMATICS, AeroExtraItems.GPS_SATELLITE, AeroExtraItems.GPS_RECEIVER);
        addItemsToSection(GEOMATICS, AeroExtraItems.ANDESITE_ANTENNA, AeroExtraItems.BRASS_ANTENNA);
    }

    private static void addItemsToSection(ResourceLocation section, DeferredItem<?>... items) {
        for (DeferredItem<?> itemGetter : items) {
            SimulatedRegistrate.TAB_ITEMS.add(itemGetter::asItem);
            SimulatedRegistrate.ITEM_TO_SECTION.put(itemGetter.getId(), section);
        }
    }
}
