package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.AeroExtraPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class GPSSatelliteRenderer extends SmartBlockEntityRenderer<GPSSatelliteBlockEntity> {
    public GPSSatelliteRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(GPSSatelliteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if (!(be instanceof GPSSatelliteBlockEntity)) return;

        BlockState blockState = be.getBlockState();
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        SuperByteBuffer border = CachedBuffers.block(blockState);
        border.light(light).renderInto(ms, vb);

        float scale = be.coreScaler.getValue(partialTicks);
        float angle = be.coreRotation.getValue(partialTicks);
        float translate = (1 - scale) / 2;
        if (scale > 0) {
            SuperByteBuffer core = CachedBuffers.partial(AeroExtraPartialModels.BRASS_GPS_CORE, blockState);
            core.translate(translate, translate + scale, translate)
                    .scale(scale)
                    .rotateCentered(AngleHelper.rad(angle), Direction.Axis.Y)
                    .light(light).renderInto(ms, vb);
        }
    }
}
