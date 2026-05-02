package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class AeroExtraItems {
    private static final CreateRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final ItemEntry<Item> ANDESITE_GPS_CORE = REGISTRATE
            .item("andesite_gps_core", Item::new)
            .properties(p -> p.stacksTo(1)
                    .component(AeroExtraDataComponents.GPS_ERROR, 0.15))
            .lang("Andesite GPS Core")
            .register();
    public static final ItemEntry<Item> BRASS_GPS_CORE = REGISTRATE
            .item("brass_gps_core", Item::new)
            .properties(p -> p.stacksTo(1)
                    .component(AeroExtraDataComponents.GPS_ERROR, 0.05))
            .lang("Brass GPS Core")
            .register();

    public static void register() {}
}
