package com.github.winexp.aeronauticsextra.content.logistics.gps;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GPSBroadcastReceiver {
    public static final int MAX_SAMPLING_TIME = 200;

    private final Level level;
    private final Vec3 receiverPos;
    private final float baseError;
    private final ReceiveCallback receiveCallback;
    private final SamplingCompleteCallback samplingCompleteCallback;
    private int samplingTime;

    public GPSBroadcastReceiver(Level level, Vec3 receiverPos, float baseError, ReceiveCallback receiveCallback, SamplingCompleteCallback samplingCompleteCallback, int samplingTime) {
        this.level = level;
        this.receiverPos = receiverPos;
        this.baseError = baseError;
        this.receiveCallback = receiveCallback;
        this.samplingCompleteCallback = samplingCompleteCallback;
        if (samplingTime < 0) throw new IllegalArgumentException("Sampling time must be non-negative");
        this.samplingTime = Math.min(samplingTime, MAX_SAMPLING_TIME);
    }

    public Level getLevel() {
        return this.level;
    }

    public Vec3 getReceiverPos() {
        return this.receiverPos;
    }

    public float getBaseError() {
        return this.baseError;
    }

    public ReceiveCallback getReceiveCallback() {
        return this.receiveCallback;
    }

    public SamplingCompleteCallback getSamplingCompleteCallback() {
        return this.samplingCompleteCallback;
    }

    public void tick() {
        this.samplingTime--;
    }

    public boolean isAlive() {
        return this.samplingTime > 0;
    }

    public interface ReceiveCallback {
        void onReceive(SampleData sampleData);
    }

    public interface SamplingCompleteCallback {
        void onComplete();
    }
}
