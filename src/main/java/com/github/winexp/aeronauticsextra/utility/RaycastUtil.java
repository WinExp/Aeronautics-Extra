package com.github.winexp.aeronauticsextra.utility;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import dev.ryanhcode.sable.mixinterface.clip_overwrite.LevelPoseProviderExtension;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.Position;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;

public class RaycastUtil {
    public static Map<Block, Double> blockRaycast(Level level, Vec3 start, Vec3 end) {
        HashMap<Block, Double> blockMap = new HashMap<>();
        var subLevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d((Position) start, end));
        for (SubLevel sublevel : subLevels) {
            Vec3 startIn, endIn;
            if (level instanceof LevelPoseProviderExtension extension) {
                startIn = extension.sable$getPose(sublevel).transformPositionInverse(start);
                endIn =  extension.sable$getPose(sublevel).transformPositionInverse(end);
            } else {
                startIn = sublevel.logicalPose().transformPositionInverse(start);
                endIn = sublevel.logicalPose().transformPositionInverse(end);
            }
            Map<Block, Double> sublevelRaycastResult = blockRaycastNoSublevel(level, startIn, endIn);
            for (Map.Entry<Block, Double> entry : sublevelRaycastResult.entrySet()) {
                blockMap.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }
        Map<Block, Double> raycastResult = blockRaycastNoSublevel(level, start, end);
        for (Map.Entry<Block, Double> entry : raycastResult.entrySet()) {
            blockMap.merge(entry.getKey(), entry.getValue(), Double::sum);
        }
        return blockMap;
    }

    private static Map<Block, Double> blockRaycastNoSublevel(Level level, Vec3 start, Vec3 end) {
        ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty());
        HashMap<Block, Double> blockMap = new HashMap<>();
        BlockGetter.traverseBlocks(start, end, context, (ctx, blockPos) -> {
            Vec3 from = ctx.getFrom(), to = ctx.getTo();
            BlockState state = level.getBlockState(blockPos);
            Block block = state.getBlock();
            AABB fullAABB = new AABB(blockPos);
            double totalLength = getLengthInAABB(fullAABB, from, to);
            if (totalLength < 1e-7) return null;
            VoxelShape shape = ctx.getBlockShape(state, level, blockPos);
            double insideLength = 0;
            for (AABB aabb : shape.toAabbs()) {
                insideLength += getLengthInAABB(aabb.move(blockPos), from, to);
            }
            if (insideLength < 1e-7) insideLength = 0;
            else blockMap.merge(block, insideLength, Double::sum);
            double airLength = totalLength - insideLength;
            if (airLength >= 1e-7) blockMap.merge(Blocks.AIR, airLength, Double::sum);

            return null;
        }, ctx -> null);
        return blockMap;
    }

    public static double getLengthInAABB(AABB aabb, Vec3 from, Vec3 to) {
        double distance = from.distanceTo(to);
        if (distance < 1e-7) return 0;

        double minX = aabb.minX, minY = aabb.minY, minZ = aabb.minZ, maxX = aabb.maxX, maxY = aabb.maxY, maxZ = aabb.maxZ;
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        double tNear = Double.NEGATIVE_INFINITY, tFar = Double.POSITIVE_INFINITY;

        if (Math.abs(dx) < 1e-7) {
            if (from.x > maxX || from.x < minX) return 0;
        } else {
            double t1 = (minX - from.x) / dx, t2 = (maxX - from.x) / dx;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tNear = Math.max(tNear, t1);
            tFar = Math.min(tFar, t2);
            if (tNear > tFar) return 0;
        }

        if (Math.abs(dy) < 1e-7) {
            if (from.y > maxY || from.y < minY) return 0;
        } else {
            double t1 = (minY - from.y) / dy, t2 = (maxY - from.y) / dy;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tNear = Math.max(tNear, t1);
            tFar = Math.min(tFar, t2);
            if (tNear > tFar) return 0;
        }

        if (Math.abs(dz) < 1e-7) {
            if (from.z > maxZ || from.z < minZ) return 0;
        } else {
            double t1 = (minZ - from.z) / dz, t2 = (maxZ - from.z) / dz;
            if (t1 > t2) { double tmp = t1; t1 = t2; t2 = tmp; }
            tNear = Math.max(tNear, t1);
            tFar = Math.min(tFar, t2);
            if (tNear > tFar) return 0;
        }
        tNear = Math.max(0, tNear);
        tFar = Math.min(1, tFar);
        if (tNear > tFar) return 0;

        return distance * (tFar - tNear);
    }
}
