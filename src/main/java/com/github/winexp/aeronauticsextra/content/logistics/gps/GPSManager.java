package com.github.winexp.aeronauticsextra.content.logistics.gps;

import com.github.winexp.aeronauticsextra.utility.RaycastUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
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

    public static void init() {
        broadcasts.clear();
        receivers.clear();
    }

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

    public static void tick(MinecraftServer server) {
        if (!server.tickRateManager().runsNormally()) return;

        for (Level level : server.getAllLevels()) {
            levelTick(level);
        }

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
                receiver.getSamplingDoneCallback().onComplete();
                receiverIterator.remove();
            }
        }
    }

    public static float getSignalStrength(Level level, Vec3 fromPos, Vec3 toPos, float baseStrength, int maxRange) {
        Vec3 distance = fromPos.subtract(toPos);
        if (Math.abs(distance.x) >= maxRange && Math.abs(distance.y) >= maxRange && Math.abs(distance.z) >= maxRange) return 0;
        var blockMap = RaycastUtil.blockRaycast(level, fromPos, toPos);
        double weightedFactor = 1;
        for (Map.Entry<Block, RaycastUtil.Section> entry : blockMap.entries()) {
            Block block = entry.getKey();
            BlockState state = block.defaultBlockState();
            double length = entry.getValue().length();
            double opacity = state.getLightBlock(level, BlockPos.ZERO) / 15.0;
            if (block.defaultBlockState().isAir()) opacity = 0.0075;
            else if (opacity <= 0) opacity = 0.012;
            weightedFactor += opacity * length;
        }
        return (float) (baseStrength / weightedFactor);
    }

    private static void levelTick(Level level) {
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
                    Vec3 broadcastPos = broadcast.getCenterPos();
                    double distance = broadcastPos.distanceTo(receiverPos);
                    Vec3 broadcastActualPos;
                    if (broadcast.getAntennaShape().isEmpty()) {
                        broadcastActualPos = broadcastPos;
                    } else {
                        broadcastActualPos = broadcast.getAntennaShape().move(broadcastPos.x, broadcastPos.y, broadcastPos.z).closestPointTo(receiverPos).get();
                    }
                    Vec3 receiverActualPos;
                    if (receiver.getAntennaShape().isEmpty()) {
                        receiverActualPos = receiverPos;
                    } else {
                        receiverActualPos = receiver.getAntennaShape().move(receiverPos.x, receiverPos.y, receiverPos.z).closestPointTo(broadcastPos).get();
                    }
                    float signalStrength = getSignalStrength(level, broadcastActualPos, receiverActualPos, broadcast.getSignalStrength(), broadcast.getMaxRange());
                    if (signalStrength < 0.01f) continue;
                    float maxError = baseError / signalStrength;
                    int sign = level.random.nextBoolean() ? 1 : -1;
                    distance += sign * level.random.nextFloat() * maxError;
                    receiveCallback.onReceive(new GPSSampleData(broadcast.getUUID(), broadcast.getVirtualPos(), distance, signalStrength));
                }
            }

            if (broadcast.getBoundingBox().getSize() >= broadcast.getMaxRange() * 2) iterator.remove();
        }
    }
}
