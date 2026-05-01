package com.github.winexp.aeronauticsextra.content.commands;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSRequest;
import com.github.winexp.aeronauticsextra.content.logistics.gps.SatelliteResponse;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class GPSCommand implements Command<CommandSourceStack> {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
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
}
