package com.github.winexp.aeronauticsextra.neoforge;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.client.renderer.GPSSatelliteRenderer;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ConfigScreen;
import com.github.winexp.aeronauticsextra.content.logistics.gps.networking.ServerBoundConfigRequest;
import com.github.winexp.aeronauticsextra.data.AeroExtraBlockStateProvider;
import com.github.winexp.aeronauticsextra.data.AeroExtraBlockTagsProvider;
import com.github.winexp.aeronauticsextra.data.AeroExtraItemModelProvider;
import com.github.winexp.aeronauticsextra.data.AeroExtraItemTagsProvider;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraMenuTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = AeronauticsExtra.MOD_ID)
public class AeroExtraModBusEventHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new AeroExtraBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new AeroExtraItemModelProvider(output, existingFileHelper));

        BlockTagsProvider blockTagsProvider = new AeroExtraBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new AeroExtraItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
    }

    @SubscribeEvent
    public static void onPayloadRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ServerBoundConfigRequest.TYPE, ServerBoundConfigRequest.STREAM_CODEC, new ServerBoundConfigRequest.RequestHandler());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AeroExtraBlockEntityTypes.GPS_SATELLITE.get(), GPSSatelliteRenderer::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(AeroExtraMenuTypes.GPS_SATELLITE_CONFIG.get(), ConfigScreen::new);
    }
}
