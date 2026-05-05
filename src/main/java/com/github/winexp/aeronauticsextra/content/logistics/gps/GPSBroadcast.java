package com.github.winexp.aeronauticsextra.content.logistics.gps;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GPSBroadcast {
    public static final int BROADCAST_MAX_RANGE = 256;

    private final Level level;
    private final Vec3 virtualPos;
    private final Vec3 centerPos;
    private final float signalStrength;
    private final int maxRange;
    private int aliveTime = 200;
    private AABB boundingBox;

    public GPSBroadcast(Level level, Vec3 virtualPos, Vec3 centerPos, float signalStrength, int maxRange) {
        this.level = level;
        this.virtualPos = virtualPos;
        this.centerPos = centerPos;
        this.maxRange = Math.min(BROADCAST_MAX_RANGE, maxRange);
        this.signalStrength = signalStrength;
        this.boundingBox = AABB.ofSize(centerPos, 0, 0, 0);
    }

    public Level getLevel() {
        return this.level;
    }

    public Vec3 getVirtualPos() {
        return this.virtualPos;
    }

    public Vec3 getCenterPos() {
        return this.centerPos;
    }

    public int getMaxRange() {
        return this.maxRange;
    }

    public float getSignalStrength() {
        return this.signalStrength;
    }

    public AABB getBoundingBox() {
        return this.boundingBox;
    }

    public void propagate(double increment) {
        if (increment < 0) throw new IllegalArgumentException("Increment must be non-negative");
        this.boundingBox = this.boundingBox.inflate(increment);
    }

    public void tick() {
        this.aliveTime--;
    }

    public boolean isAlive() {
        return this.aliveTime > 0;
    }
}
