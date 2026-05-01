package com.github.winexp.aeronauticsextra.content.commands;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSRequest;
import com.github.winexp.aeronauticsextra.content.logistics.gps.TrilaterationSolver;
import com.github.winexp.aeronauticsextra.content.logistics.gps.SatelliteResponse;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class GPSCommand {
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
            TrilaterationSolver.LocateResult result = TrilaterationSolver.locate(responses);
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
