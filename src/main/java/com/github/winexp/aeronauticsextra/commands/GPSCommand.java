package com.github.winexp.aeronauticsextra.commands;

import com.github.winexp.aeronauticsextra.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.logistics.gps.GPSRequest;
import com.github.winexp.aeronauticsextra.logistics.gps.SatelliteResponse;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class GPSCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        GPSManager.request(new GPSRequest(source.getLevel(), source.getPosition(), responses -> {
            source.sendSuccess(() -> Component.literal("GPS locate responses:"), false);
            for (SatelliteResponse response : responses) {
                String message = "----Satellite position: " + response.satelliteLocation().toString() + '\n'
                        + "    Distance: " + response.distance();
                source.sendSuccess(() -> Component.literal(message), false);
            }
        }));

        return 0;
    }
}
