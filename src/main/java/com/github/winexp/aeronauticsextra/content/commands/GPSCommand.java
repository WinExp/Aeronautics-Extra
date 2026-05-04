package com.github.winexp.aeronauticsextra.content.commands;

import com.github.winexp.aeronauticsextra.content.logistics.gps.*;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class GPSCommand {
    public static int getSatelliteInfo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        int samplingTime = context.getArgument("sampling_time", Integer.class);
        source.sendSuccess(() -> Component.literal("Requesting GPS information..."), false);
        ArrayList<SampleData> sampleDataList = new ArrayList<>();
        GPSBroadcastReceiver receiver = new GPSBroadcastReceiver(source.getLevel(), source.getPosition(), 0.1f, sampleDataList::add, () -> {
            StringBuilder builder = new StringBuilder("GPS locate responses:\n");
            for (SampleData sampleData : sampleDataList) {
                Vec3 pos = sampleData.satellitePosition();
                builder.append("    Satellite position: (%.2f, %.2f, %.2f)".formatted(pos.x, pos.y, pos.z));
                builder.append("\n    Distance: %.2f".formatted(sampleData.distance()));
                builder.append("\n    Signal strength: %.2f".formatted(sampleData.signalStrength()));
                builder.append("\n\n");
            }
            source.sendSuccess(() -> Component.literal(builder.toString().trim()), false);
        }, samplingTime);
        GPSManager.registerReceiver(receiver);

        return 0;
    }

    public static int locate(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        int samplingTime = context.getArgument("sampling_time", Integer.class);
        source.sendSuccess(() -> Component.literal("Locating..."), false);
        ArrayList<SampleData> sampleDataList = new ArrayList<>();
        GPSBroadcastReceiver receiver = new GPSBroadcastReceiver(source.getLevel(), source.getPosition(), 0.1f, sampleDataList::add, () -> {
            TrilaterationResolver.LocateResult result = TrilaterationResolver.locate(sampleDataList);
            if (!result.isEmpty()) {
                Vec3 pos = result.position();
                source.sendSuccess(() -> Component.literal("X: %.2f, Y: %.2f, Z: %.2f".formatted(pos.x, pos.y, pos.z)), false);
            } else {
                source.sendFailure(Component.literal("GPS locate failed"));
            }
        }, samplingTime);
        GPSManager.registerReceiver(receiver);

        return 0;
    }
}
