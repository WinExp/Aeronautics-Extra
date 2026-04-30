package com.github.winexp.aeronauticsextra.neoforge;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.logistics.gps.networking.ServerBoundConfigRequest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AeronauticsExtra.MOD_ID)
public class AeroExtraModBusEventHandler {
    @SubscribeEvent
    public static void onPayloadRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ServerBoundConfigRequest.TYPE, ServerBoundConfigRequest.STREAM_CODEC, new ServerBoundConfigRequest.RequestHandler());
    }
}
