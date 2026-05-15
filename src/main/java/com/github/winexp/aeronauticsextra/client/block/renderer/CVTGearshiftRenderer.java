package com.github.winexp.aeronauticsextra.client.block.renderer;

import com.github.winexp.aeronauticsextra.client.AeroExtraPartialModels;
import com.github.winexp.aeronauticsextra.content.blocks.kinetics.CVTGearshiftBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.simulated_team.simulated.content.blocks.analog_transmission.AnalogTransmissionBlock;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class CVTGearshiftRenderer extends KineticBlockEntityRenderer<CVTGearshiftBlockEntity> {
    public CVTGearshiftRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CVTGearshiftBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

        BlockState state = be.getBlockState();
        Direction.Axis axis = ((IRotate) state.getBlock()).getRotationAxis(state);
        Direction direction = Direction.fromAxisAndDirection(state.getValue(AnalogTransmissionBlock.AXIS), Direction.AxisDirection.POSITIVE);

        SuperByteBuffer cogwheel = kineticRotationTransform(
                CachedBuffers.partialFacingVertical(AeroExtraPartialModels.CVT_GEARSHIFT_COG, state, direction),
                be.getExtraKinetics(),
                axis,
                getAngleForBe(be.getExtraKinetics(), be.getBlockPos(), axis),
                light);

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());
        cogwheel.renderInto(ms, vb);
    }

    @Override
    protected BlockState getRenderedBlockState(CVTGearshiftBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
