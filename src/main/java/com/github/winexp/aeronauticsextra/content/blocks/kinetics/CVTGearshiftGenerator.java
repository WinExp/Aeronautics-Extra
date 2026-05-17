package com.github.winexp.aeronauticsextra.content.blocks.kinetics;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class CVTGearshiftGenerator {
    public static <T extends CVTGearshiftBlock> void generate(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov) {
        prov.getVariantBuilder(ctx.getEntry()).forAllStates((state) -> {
            Direction dir = state.getValue(CVTGearshiftBlock.FACING);
            Direction.Axis axis = dir.getAxis();
            boolean axisAlongFirst = state.getValue(CVTGearshiftBlock.AXIS_ALONG_FIRST_COORDINATE);
            boolean isPowered = state.getValue(CVTGearshiftBlock.LEFT_POWERED) || state.getValue(CVTGearshiftBlock.RIGHT_POWERED);
            int rotX, rotY;
            String suffix = "block" + (isPowered ? "_on" : "");
            if (axisAlongFirst) {
                if (axis == Direction.Axis.X) {
                    rotX = 0;
                    rotY = (int) (dir.toYRot() + 90) % 360;
                } else if (axis == Direction.Axis.Z) {
                    rotX = 90;
                    rotY = dir == Direction.SOUTH ? 90 : -90;
                } else {
                    rotX = 90;
                    rotY = dir == Direction.DOWN ? 90 : -90;
                    suffix += "_vertical";
                }
            } else {
                if (axis == Direction.Axis.Z) {
                    rotX = 0;
                    rotY = (int) (dir.toYRot() + 90) % 360;
                } else if (axis == Direction.Axis.X) {
                    rotX = 90;
                    rotY = dir == Direction.EAST ? 0 : 180;
                } else {
                    rotX = 90;
                    rotY = dir == Direction.DOWN ? 0 : 180;
                    suffix += "_vertical";
                }
            }
            return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc("block/cvt_gearshift/" + suffix)))
                    .rotationX(rotX)
                    .rotationY(rotY)
                    .build();
        });
    }
}
