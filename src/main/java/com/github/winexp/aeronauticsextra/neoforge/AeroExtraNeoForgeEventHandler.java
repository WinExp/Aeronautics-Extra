package com.github.winexp.aeronauticsextra.neoforge;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.commands.GPSCommand;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

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
                .then(Commands.argument("sampling_time", IntegerArgumentType.integer(1))
                        .executes(GPSCommand::getSatelliteInfo)));
        gpsNode.then(Commands.literal("locate")
                .then(Commands.argument("sampling_time", IntegerArgumentType.integer(1))
                        .executes(GPSCommand::locate)));
        dispatcher.register(gpsNode);
    }
}
