package com.github.winexp.aeronauticsextra.logistics.gps;

import net.minecraft.world.phys.Vec3;

public record SatelliteResponse(Vec3 satelliteLocation, double distance) {
}
