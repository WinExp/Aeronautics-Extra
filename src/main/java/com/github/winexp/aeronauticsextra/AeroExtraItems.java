package com.github.winexp.aeronauticsextra;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

public class AeroExtraItems {
    private static final CreateRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final ItemEntry<Item> BRASS_GPS_CORE = REGISTRATE
            .item("brass_gps_core", Item::new)
            .properties(p -> p.stacksTo(1))
            .lang("Brass GPS Core")
            .register();

    public static void register() {}
}
