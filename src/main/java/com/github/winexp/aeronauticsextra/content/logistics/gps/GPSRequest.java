package com.github.winexp.aeronauticsextra.content.logistics.gps;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSRequest {
    private final Level level;
    private final Vec3 receiverPos;
    private final GPSCallback callback;
    private int aliveTime;

    public GPSRequest(Level level, Vec3 receiverPos, GPSCallback callback) {
        this(level, receiverPos, callback, 40);
    }

    public GPSRequest(Level level, Vec3 receiverPos, GPSCallback callback, int aliveTime) {
        this.level = level;
        this.receiverPos = receiverPos;
        this.callback = callback;
        this.aliveTime = aliveTime;
    }

    public Level getLevel() {
        return this.level;
    }

    public Vec3 getReceiverPos() {
        return this.receiverPos;
    }

    public GPSCallback getCallback() {
        return this.callback;
    }

    public void tick() {
        this.aliveTime--;
    }

    public int getAliveTime() {
        return this.aliveTime;
    }

    public boolean isAlive() {
        return this.aliveTime > 0;
    }

    public interface GPSCallback {
        void accept(List<SatelliteResponse> responses);
    }
}
