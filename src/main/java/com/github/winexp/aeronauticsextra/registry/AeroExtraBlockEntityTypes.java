package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.client.renderer.block.GPSSatelliteRenderer;
import com.github.winexp.aeronauticsextra.content.blocks.gps.receiver.GPSReceiverBlockEntity;
import com.github.winexp.aeronauticsextra.content.blocks.gps.satellite.GPSSatelliteBlockEntity;
import com.github.winexp.aeronauticsextra.content.blocks.kinetics.CVTGearshiftBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;

public class AeroExtraBlockEntityTypes {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final BlockEntityEntry<GPSSatelliteBlockEntity> GPS_SATELLITE = REGISTRATE
            .blockEntity("gps_satellite", GPSSatelliteBlockEntity::new)
            .validBlocks(AeroExtraBlocks.GPS_SATELLITE)
            .renderer(() -> GPSSatelliteRenderer::new)
            .register();
    public static final BlockEntityEntry<GPSReceiverBlockEntity> GPS_RECEIVER = REGISTRATE
            .blockEntity("gps_receiver", GPSReceiverBlockEntity::new)
            .displaySource(AeroExtraDisplaySources.GPS_RECEIVER)
            .validBlocks(AeroExtraBlocks.GPS_RECEIVER)
            .register();

    public static final BlockEntityEntry<CVTGearshiftBlockEntity> CVT_GEARSHIFT = REGISTRATE
            .blockEntity("cvt_gearshift", CVTGearshiftBlockEntity::new)
            .validBlocks(AeroExtraBlocks.CVT_GEARSHIFT)
            .register();

    public static void register() {}
}
