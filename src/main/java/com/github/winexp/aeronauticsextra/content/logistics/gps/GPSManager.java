package com.github.winexp.aeronauticsextra.content.logistics.gps;

import com.github.winexp.aeronauticsextra.utility.RaycastUtil;
import dev.ryanhcode.sable.Sable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.Map;

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

    public static float getSignalStrength(Level level, Vec3 fromPos, Vec3 toPos, float baseStrength, int maxRange) {
        Vec3 distance = fromPos.subtract(toPos);
        if (Math.abs(distance.x) >= maxRange && Math.abs(distance.y) >= maxRange && Math.abs(distance.z) >= maxRange) return 0;
        BlockPos fromBlockPos = BlockPos.containing(fromPos);
        BlockPos targetBlockPos = BlockPos.containing(toPos);
        var blockMap = RaycastUtil.blockRaycast(level, fromPos, toPos, (pos, state) -> !pos.equals(fromBlockPos) && !pos.equals(targetBlockPos));
        double weightedFactor = 1;
        for (Map.Entry<Block, Double> entry : blockMap.entrySet()) {
            Block block = entry.getKey();
            BlockState state = block.defaultBlockState();
            double length = entry.getValue();
            double opacity = state.getLightBlock(level, BlockPos.ZERO) / 15.0;
            if (block.defaultBlockState().isAir()) opacity = 0.01;
            else if (opacity <= 0) opacity = 0.02;
            weightedFactor += opacity * length;
        }
        return (float) (baseStrength / weightedFactor);
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
                Vec3 receiverPos = Sable.HELPER.projectOutOfSubLevel(level, receiver.getReceiverPos());
                float baseError = receiver.getBaseError();
                GPSBroadcastReceiver.ReceiveCallback receiveCallback = receiver.getReceiveCallback();
                if (boundingBox.contains(receiverPos) && !oldBoundingBox.contains(receiverPos)) {
                    Vec3 broadcastPos = Sable.HELPER.projectOutOfSubLevel(level, broadcast.getCenterPos());
                    double distance = broadcastPos.distanceTo(receiverPos);
                    float signalStrength = getSignalStrength(level, broadcastPos, receiverPos, broadcast.getSignalStrength(), broadcast.getMaxRange());
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
