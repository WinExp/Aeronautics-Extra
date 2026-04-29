package com.github.winexp.aeronauticsextra.logistics.gps;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSRequest {
    private final Level level;
    private final Vec3 position;
    private final GPSCallback callback;
    private int aliveTime = 40;

    public GPSRequest(Level level, Vec3 position, GPSCallback callback) {
        this.level = level;
        this.position = position;
        this.callback = callback;
    }

    public GPSRequest(Level level, Vec3 position, GPSCallback callback, int aliveTime) {
        this(level, position, callback);
        this.aliveTime = aliveTime;
    }

    public Level getLevel() {
        return this.level;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public GPSCallback getCallback() {
        return this.callback;
    }

    public void tick() {
        this.aliveTime--;
    }

    public boolean isAlive() {
        return this.aliveTime > 0;
    }

    public interface GPSCallback {
        void accept(List<SatelliteResponse> responses);
    }
}
