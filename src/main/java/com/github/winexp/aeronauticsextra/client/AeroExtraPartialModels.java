package com.github.winexp.aeronauticsextra.client;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AeroExtraPartialModels {
    public static final PartialModel
            BRASS_GPS_CORE = block("gps_satellite/core");

    private static PartialModel block(String path) {
        return PartialModel.of(AeronauticsExtra.asResource("block/" + path));
    }

    public static void init() {}
}
