package com.github.winexp.aeronauticsextra.client.events;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.client.gui.GPSSatelliteConfigScreen;
import com.github.winexp.aeronauticsextra.client.model.SmallBalloonEntityModel;
import com.github.winexp.aeronauticsextra.client.renderer.block.BlockOutlineRenderer;
import com.github.winexp.aeronauticsextra.client.renderer.block.GPSSatelliteRenderer;
import com.github.winexp.aeronauticsextra.client.renderer.entity.SmallBalloonEntityRenderer;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraMenuTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = AeronauticsExtra.MOD_ID)
public class ClientEvents {
    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SmallBalloonEntityModel.LAYER_LOCATION, SmallBalloonEntityModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AeroExtraEntityTypes.SMALL_BALLOON.get(), SmallBalloonEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AeroExtraBlockEntityTypes.GPS_SATELLITE.get(), GPSSatelliteRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(AeroExtraMenuTypes.GPS_SATELLITE_CONFIG.get(), GPSSatelliteConfigScreen::new);
    }



    @SubscribeEvent
    public static void onRenderBlockHighlight(RenderHighlightEvent.Block event) {
        Level level = event.getLevelRenderer().level;
        BlockPos blockPos = event.getTarget().getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        Block block = blockState.getBlock();
        Vec3 hitPos = event.getTarget().getLocation();
        if (block instanceof BlockOutlineRenderer renderer) {
            PoseStack poseStack = event.getPoseStack();
            VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
            event.setCanceled(renderer.renderHighlight(poseStack, consumer, event.getCamera(), level, blockState, blockPos, hitPos));
        }
    }
}
