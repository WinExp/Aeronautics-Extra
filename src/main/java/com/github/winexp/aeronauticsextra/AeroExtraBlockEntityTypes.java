package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlockEntity;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class AeroExtraBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final BlockEntityEntry<GPSSatelliteBlockEntity> GPS_SATELLITE = REGISTRATE
            .blockEntity("gps_satellite", GPSSatelliteBlockEntity::new)
            .validBlocks(AeroExtraBlocks.GPS_SATELLITE)
            .renderer(() -> GPSSatelliteRenderer::new)
            .register();

    public static void register() {}
}
