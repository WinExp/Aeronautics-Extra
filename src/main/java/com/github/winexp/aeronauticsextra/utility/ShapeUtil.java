package com.github.winexp.aeronauticsextra.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ShapeUtil {
    public static boolean isIntersect(VoxelShape shape1, VoxelShape shape2, BlockPos pos1, BlockPos pos2) {
        if (shape1.isEmpty() || shape2.isEmpty()) return false;
        shape1 = shape1.move(pos1.getX(), pos1.getY(), pos1.getZ());
        shape2 = shape2.move(pos2.getX(), pos2.getY(), pos2.getZ());
        if (!shape1.bounds().intersects(shape2.bounds())) return false;
        for (AABB aabb1 : shape1.toAabbs()) {
            for (AABB aabb2 : shape2.toAabbs()) {
                if (aabb1.intersects(aabb2)) return true;
            }
        }
        return false;
    }

    public static boolean isIntersect(AABB aabb, VoxelShape shape, BlockPos pos) {
        if (shape.isEmpty()) return false;
        shape = shape.move(pos.getX(), pos.getY(), pos.getZ());
        if (!shape.bounds().intersects(aabb)) return false;
        for (AABB aabb1 : shape.toAabbs()) {
            if (aabb.intersects(aabb1)) return true;
        }
        return false;
    }

    public static VoxelShape setOriginAsCenter(VoxelShape shape) {
        return shape.move(-0.5, -0.5, -0.5);
    }
}
