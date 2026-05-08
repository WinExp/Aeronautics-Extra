package com.github.winexp.aeronauticsextra.content.logistics.gps;

import net.minecraft.world.phys.Vec3;

public record GPSSampleData(Vec3 satellitePosition, double distance, float signalStrength) {
}
