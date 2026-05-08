package com.github.winexp.aeronauticsextra.content.commands;

import com.github.winexp.aeronauticsextra.utility.RaycastUtil;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public class RaycastCommand {
    public static int raycast(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        WorldCoordinates startPos = context.getArgument("start_pos", WorldCoordinates.class);
        WorldCoordinates endPos = context.getArgument("end_pos", WorldCoordinates.class);
        for (Map.Entry<Block, RaycastUtil.Section> entry : RaycastUtil.blockRaycast(source.getLevel(), startPos.getPosition(source), endPos.getPosition(source)).entries()) {
            source.sendSuccess(() -> entry.getKey().getName().append(": ").append("%.2f".formatted(entry.getValue().length())), false);
        }

        return 0;
    }
}
