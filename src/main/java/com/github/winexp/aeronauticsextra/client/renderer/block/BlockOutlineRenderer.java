package com.github.winexp.aeronauticsextra.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface BlockOutlineRenderer {
    boolean renderHighlight(PoseStack poseStack, VertexConsumer vertexConsumer, Camera camera, Level level, BlockState blockState, BlockPos blockPos, Vec3 hitPos);

    static Vec3 getRenderPos(BlockPos blockPos, Vec3 cameraPos) {
        return new Vec3(blockPos.getX() - cameraPos.x, blockPos.getY() - cameraPos.y, blockPos.getZ() - cameraPos.z);
    }
}
