package com.github.winexp.aeronauticsextra.blocks;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.blocks.gps.GPSSatelliteBlock;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;

public class AeroExtraBlocks {
    private static final Registrate REGISTRATE = AeronauticsExtra.REGISTRATE.get();

    public static final BlockEntry<GPSSatelliteBlock> GPS_SATELLITE = REGISTRATE.block("gps_satellite", GPSSatelliteBlock::new)
            .initialProperties(SharedProperties::stone)
            .simpleItem()
            .register();

    public static void register() {}
}
