package com.github.winexp.aeronauticsextra.content.commands;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSRequest;
import com.github.winexp.aeronauticsextra.content.logistics.gps.TrilaterationResolver;
import com.github.winexp.aeronauticsextra.content.logistics.gps.SatelliteResponse;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSCommand {
    public static int getPendingRequests(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        List<GPSRequest> requests = GPSManager.getRequests();
        StringBuilder builder = new StringBuilder("GPS pending requests: ");
        for (GPSRequest request : requests) {
            builder.append("    Level: ");
            builder.append(request.getLevel().dimension().location());
            builder.append("\n    Receiver location: ");
            builder.append(request.getReceiverPos().toString());
            builder.append("\n\n");
        }
        source.sendSuccess(() -> Component.literal(builder.toString().trim()), false);

        return 0;
    }

    public static int getSatelliteInfo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GPSManager.request(new GPSRequest(source.getLevel(), source.getPosition(), responses -> {
            StringBuilder builder = new StringBuilder("GPS locate responses:\n");
            for (SatelliteResponse response : responses) {
                builder.append("    Satellite position: ");
                builder.append(response.satellitePosition().toString());
                builder.append("\n    Distance: ");
                builder.append(response.distance());
                builder.append("\n\n");
            }
            source.sendSuccess(() -> Component.literal(builder.toString().trim()), false);
        }));

        return 0;
    }

    public static int locate(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GPSManager.request(new GPSRequest(source.getLevel(), source.getPosition(), responses -> {
            TrilaterationResolver.LocateResult result = TrilaterationResolver.locate(responses);
            if (!result.isEmpty()) {
                Vec3 pos = result.position();
                source.sendSuccess(() -> Component.literal("X: %.2f, Y: %.2f, Z: %.2f".formatted(pos.x, pos.y, pos.z)), false);
            } else {
                source.sendFailure(Component.literal("GPS locate failed"));
            }
        }));

        return 0;
    }
}
