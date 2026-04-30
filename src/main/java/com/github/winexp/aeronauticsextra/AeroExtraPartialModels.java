package com.github.winexp.aeronauticsextra;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AeroExtraPartialModels {
    public static final PartialModel
            GPS_SATELLITE_BORDER = block("gps_satellite/block");

    private static PartialModel block(String path) {
        return PartialModel.of(AeronauticsExtra.asResource("block/" + path));
    }
}
