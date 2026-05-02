package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSReceiverBlockEntity;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlockEntity;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class AeroExtraBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final BlockEntityEntry<GPSSatelliteBlockEntity> GPS_SATELLITE = REGISTRATE
            .blockEntity("gps_satellite", GPSSatelliteBlockEntity::new)
            .renderer(() -> GPSSatelliteRenderer::new)
            .validBlocks(AeroExtraBlocks.GPS_SATELLITE)
            .register();
    public static final BlockEntityEntry<GPSReceiverBlockEntity> GPS_RECEIVER = REGISTRATE
            .blockEntity("gps_receiver", GPSReceiverBlockEntity::new)
            .validBlocks(AeroExtraBlocks.GPS_RECEIVER)
            .register();

    public static void register() {}
}
