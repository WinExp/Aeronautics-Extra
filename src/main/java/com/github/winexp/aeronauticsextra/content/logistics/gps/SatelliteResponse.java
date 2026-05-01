package com.github.winexp.aeronauticsextra.content.logistics.gps;

import net.minecraft.world.phys.Vec3;

public record SatelliteResponse(Vec3 satellitePosition, double distance) {
}
