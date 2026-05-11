package com.github.winexp.aeronauticsextra.events;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.commands.GPSCommand;
import com.github.winexp.aeronauticsextra.content.commands.RaycastCommand;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.networking.ServerBoundConfigRequest;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = AeronauticsExtra.MOD_ID)
public class CommonEvents {
    @SubscribeEvent
    public static void onPayloadRegister(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(ServerBoundConfigRequest.TYPE, ServerBoundConfigRequest.STREAM_CODEC, new ServerBoundConfigRequest.RequestHandler());
    }



    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        GPSManager.init();
    }

    @SubscribeEvent
    public static void onPostTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        GPSManager.tick(server);
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var gpsNode = Commands.literal("gps").requires(source -> source.hasPermission(2));
        gpsNode.then(Commands.literal("get_locate_info")
                .then(Commands.argument("sampling_time", IntegerArgumentType.integer(1, 40))
                        .executes(GPSCommand::getSatelliteInfo)));
        gpsNode.then(Commands.literal("locate")
                .then(Commands.argument("sampling_time", IntegerArgumentType.integer(1, 40))
                        .executes(GPSCommand::locate)));
        dispatcher.register(gpsNode);
        dispatcher.register(Commands.literal("raycast").requires(source -> source.hasPermission(2))
                .then(Commands.argument("start_pos", Vec3Argument.vec3())
                        .then(Commands.argument("end_pos", Vec3Argument.vec3())
                                .executes(RaycastCommand::raycast))));
    }
}
