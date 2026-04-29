package com.github.winexp.aeronauticsextra.blocks;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.blocks.gps.GPSSatelliteBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class AeroExtraBlockEntityTypes {
    private static final Registrate REGISTRATE = AeronauticsExtra.REGISTRATE.get();

    public static final BlockEntityEntry<GPSSatelliteBlockEntity> GPS_SATELLITE = REGISTRATE.blockEntity("gps_satellite", GPSSatelliteBlockEntity::new)
            .validBlocks(AeroExtraBlocks.GPS_SATELLITE)
            .register();

    public static void register() {}
}
