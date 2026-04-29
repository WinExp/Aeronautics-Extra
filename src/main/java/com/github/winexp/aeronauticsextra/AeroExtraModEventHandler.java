package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.logistics.gps.networking.ServerBoundConfigRequest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AeronauticsExtra.MOD_ID)
public class AeroExtraModEventHandler {
    @SubscribeEvent
    public static void onPayloadRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ServerBoundConfigRequest.TYPE, ServerBoundConfigRequest.STREAM_CODEC, new ServerBoundConfigRequest.RequestHandler());
    }
}
