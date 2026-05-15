package com.github.winexp.aeronauticsextra.client.entity.model;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.entity.SmallBalloonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class SmallBalloonEntityModel extends EntityModel<SmallBalloonEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(AeronauticsExtra.asResource("small_balloon_entity"), "main");
	private final ModelPart body;

	public SmallBalloonEntityModel(ModelPart root) {
		this.body = root.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		PartDefinition body = partDefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0)
				.addBox(-5.0F, -12.0F, -5.0F, 10.0F, 12.0F, 10.0F,
						new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void setupAnim(SmallBalloonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}