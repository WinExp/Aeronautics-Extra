package com.github.winexp.aeronauticsextra.content.logistics.gps;

import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record GPSSampleData(UUID satelliteUUID, Vec3 satellitePosition, double distance, float signalStrength) {
}
