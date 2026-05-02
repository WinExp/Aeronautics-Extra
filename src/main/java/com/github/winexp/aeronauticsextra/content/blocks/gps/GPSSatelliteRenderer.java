package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.AeroExtraPartialModels;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class GPSSatelliteRenderer extends SmartBlockEntityRenderer<GPSSatelliteBlockEntity> {
    private static final RenderType TRANSLUCENT_NO_CULL = RenderType
            .create("translucent",DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS,
                    786432, true, true, RenderType.CompositeState.builder()
                            .setLightmapState(RenderType.LIGHTMAP)
                            .setShaderState(RenderType.RENDERTYPE_TRANSLUCENT_SHADER)
                            .setCullState(RenderStateShard.NO_CULL)
                            .setTextureState(RenderType.BLOCK_SHEET_MIPPED)
                            .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                            .setOutputState(RenderType.TRANSLUCENT_TARGET)
                            .createCompositeState(true));

    public GPSSatelliteRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(GPSSatelliteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        BlockState blockState = be.getBlockState();

        VertexConsumer vb = buffer.getBuffer(TRANSLUCENT_NO_CULL);
        float scale = be.coreScaler.getValue(partialTicks);
        float angle = be.coreRotation.getValue(partialTicks);
        float transparency = Math.max(0, scale - 0.1f) / 0.9f;
        if (scale > 0) {
            float translate = (1 - (scale * 0.8f)) / 2;
            SuperByteBuffer core = CachedBuffers.partial(AeroExtraPartialModels.BRASS_GPS_CORE, blockState);
            core.translate(translate, translate, translate)
                    .color(255, 255, 255, (int) (transparency * 255))
                    .scale(scale * 0.8f)
                    .rotateCentered(-AngleHelper.rad(angle), Direction.Axis.Y)
                    .light(light).renderInto(ms, vb);
        }
    }
}
