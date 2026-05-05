package com.github.winexp.aeronauticsextra.neoforge;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.commands.GPSCommand;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.utility.RaycastUtil;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.Map;

@EventBusSubscriber(modid = AeronauticsExtra.MOD_ID)
public class AeroExtraNeoForgeEventHandler {
    @SubscribeEvent
    public static void onPostTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();
        if (!level.isClientSide && level.tickRateManager().runsNormally()) {
            GPSManager.levelTick(level);
            if (level.dimension().compareTo(Level.OVERWORLD) == 0) {
                GPSManager.tick();
            }
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        var gpsNode = Commands.literal("gps");
        gpsNode.then(Commands.literal("get_locate_info")
                .then(Commands.argument("sampling_time", IntegerArgumentType.integer(1, 40))
                        .executes(GPSCommand::getSatelliteInfo)));
        gpsNode.then(Commands.literal("locate")
                .then(Commands.argument("sampling_time", IntegerArgumentType.integer(1, 40))
                        .executes(GPSCommand::locate)));
        dispatcher.register(gpsNode);
        dispatcher.register(Commands.literal("raycast")
                .then(Commands.argument("start_pos", Vec3Argument.vec3())
                        .then(Commands.argument("end_pos", Vec3Argument.vec3())
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    WorldCoordinates startPos = context.getArgument("start_pos", WorldCoordinates.class);
                                    WorldCoordinates endPos = context.getArgument("end_pos", WorldCoordinates.class);
                                    for (Map.Entry<Block, Double> entry : RaycastUtil.blockRaycast(source.getLevel(), startPos.getPosition(source), endPos.getPosition(source), (pos, state) -> true).entrySet()) {
                                        source.sendSuccess(() -> entry.getKey().getName().append(": ").append("%.2f".formatted(entry.getValue())), false);
                                    }

                                    return 0;
                                }))));
    }
}
