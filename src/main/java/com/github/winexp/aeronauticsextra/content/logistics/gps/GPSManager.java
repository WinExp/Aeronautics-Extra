package com.github.winexp.aeronauticsextra.content.logistics.gps;

import com.simibubi.create.foundation.utility.RaycastHelper;
import dev.ryanhcode.sable.Sable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableFloat;

import java.util.LinkedList;

public class GPSManager {
    public static final double BROADCAST_PROPAGATION_SPEED = 128.0 / 20;

    private static final LinkedList<GPSBroadcast> broadcasts = new LinkedList<>();
    private static final LinkedList<GPSBroadcastReceiver> receivers = new LinkedList<>();

    public static void broadcast(GPSBroadcast broadcast) {
        if (broadcast.isAlive() && !broadcast.getLevel().isClientSide) {
            broadcasts.add(broadcast);
        }
    }

    public static void registerReceiver(GPSBroadcastReceiver receiver) {
        if (receiver.isAlive() && !receiver.getLevel().isClientSide) {
            receivers.add(receiver);
        }
    }

    public static float getSignalStrength(Level level, Vec3 fromPos, Vec3 toPos, int maxRange) {
        if (Sable.HELPER.distanceSquaredWithSubLevels(level, fromPos, toPos) >= maxRange * maxRange) return 0;
        MutableFloat strength = new MutableFloat(1);
        BlockPos fromBlockPos = new BlockPos(Mth.floor(fromPos.x), Mth.floor(fromPos.y), Mth.floor(fromPos.z));
        BlockPos targetBlockPos = new BlockPos(Mth.floor(toPos.x), Mth.floor(toPos.y), Mth.floor(toPos.z));
        RaycastHelper.PredicateTraceResult result = new RaycastHelper.PredicateTraceResult();
        while (result.missed()) {
            result = RaycastHelper.rayTraceUntil(fromPos, toPos, blockPos -> {
                if (blockPos.equals(fromBlockPos)) return false;
                BlockState state = level.getBlockState(blockPos);
                float opacity = state.getLightBlock(level, blockPos) / 15f;
                if (!state.isAir()) opacity = 0.02f;
                else if (opacity <= 0) opacity = 0.01f;
                strength.setValue(strength.getValue() * (1 - opacity));
                return blockPos.equals(targetBlockPos);
            });
        }
        return strength.getValue();
    }

    public static void tick() {
        var broadcastIterator = broadcasts.iterator();
        while (broadcastIterator.hasNext()) {
            GPSBroadcast broadcast = broadcastIterator.next();
            broadcast.tick();
            if (!broadcast.isAlive()) {
                broadcastIterator.remove();
            }
        }

        var receiverIterator = receivers.iterator();
        while (receiverIterator.hasNext()) {
            GPSBroadcastReceiver receiver = receiverIterator.next();
            receiver.tick();
            if (!receiver.isAlive()) {
                receiver.getSamplingCompleteCallback().onComplete();
                receiverIterator.remove();
            }
        }
    }

    public static void levelTick(Level level) {
        var iterator = broadcasts.iterator();
        while (iterator.hasNext()) {
            GPSBroadcast broadcast = iterator.next();
            if (level != broadcast.getLevel()) continue;

            AABB oldBoundingBox = broadcast.getBoundingBox();
            broadcast.propagate(BROADCAST_PROPAGATION_SPEED);
            AABB boundingBox = broadcast.getBoundingBox();
            for (GPSBroadcastReceiver receiver : receivers) {
                if (receiver.getLevel() != level) continue;
                Vec3 receiverPos = receiver.getReceiverPos();
                float baseError = receiver.getBaseError();
                GPSBroadcastReceiver.ReceiveCallback receiveCallback = receiver.getReceiveCallback();
                if (boundingBox.contains(receiverPos) && !oldBoundingBox.contains(receiverPos)) {
                    double distance = Math.sqrt(Sable.HELPER.distanceSquaredWithSubLevels(level, broadcast.getCenterPos(), receiverPos));
                    float signalStrength = getSignalStrength(level, broadcast.getCenterPos(), receiverPos, broadcast.getMaxRange());
                    if (signalStrength < 0.01f) continue;
                    float maxError = baseError / signalStrength;
                    distance += level.random.nextFloat() * maxError;
                    receiveCallback.onReceive(new SampleData(broadcast.getVirtualPos(), distance, signalStrength));
                }
            }

            if (broadcast.getBoundingBox().getSize() >= broadcast.getMaxRange() * 2) iterator.remove();
        }
    }
}
