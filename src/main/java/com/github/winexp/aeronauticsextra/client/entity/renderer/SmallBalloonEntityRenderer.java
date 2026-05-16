package com.github.winexp.aeronauticsextra.client.entity.renderer;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.client.entity.model.SmallBalloonEntityModel;
import com.github.winexp.aeronauticsextra.content.entity.SmallBalloonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;

public class SmallBalloonEntityRenderer extends EntityRenderer<SmallBalloonEntity> {
    private static final ResourceLocation DEFAULT_SKIN_LOCATION = AeronauticsExtra.asResource("textures/entity/small_balloon/balloon.png");

    private final SmallBalloonEntityModel balloonModel;

    public SmallBalloonEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.balloonModel = new SmallBalloonEntityModel(context.bakeLayer(SmallBalloonEntityModel.LAYER_LOCATION));
    }

    @Override
    public void render(SmallBalloonEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.scale(-1, -1, -1);
        poseStack.mulPose(entity.getRotation(partialTicks));
        poseStack.mulPose(Axis.YN.rotationDegrees(entity.getViewYRot(partialTicks)));
        this.balloonModel.setupAnim(entity, partialTicks, 0, 0, 0, 0);
        VertexConsumer buffer = bufferSource.getBuffer(this.balloonModel.renderType(this.getTextureLocation(entity)));
        this.balloonModel.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, CommonColors.WHITE);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SmallBalloonEntity smallBalloonEntity) {
        return DEFAULT_SKIN_LOCATION;
    }
}
