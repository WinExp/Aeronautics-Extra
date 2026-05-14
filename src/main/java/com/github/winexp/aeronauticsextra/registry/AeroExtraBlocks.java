package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.blocks.gps.receiver.GPSReceiverBlock;
import com.github.winexp.aeronauticsextra.content.blocks.gps.satellite.GPSSatelliteBlock;
import com.github.winexp.aeronauticsextra.content.blocks.kinetics.CVTGearshiftBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class AeroExtraBlocks {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final BlockEntry<GPSSatelliteBlock> GPS_SATELLITE = builder(AeroExtraCreativeTabs.GEOMATICS, "gps_satellite", GPSSatelliteBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.isRedstoneConductor(AeroExtraBlocks::never))
            .tag(AeroExtraTags.BlockTags.SABLE_SUPER_HEAVY.tag)
            .item().transform(ModelGen.customItemModel("gps_satellite", "block"))
            .transform(ModelTransform.directionalBlock(GPSSatelliteBlock.FACING, "gps_satellite/block"))
            .register();
    public static final BlockEntry<GPSReceiverBlock> GPS_RECEIVER = builder(AeroExtraCreativeTabs.GEOMATICS, "gps_receiver", GPSReceiverBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.isRedstoneConductor(AeroExtraBlocks::never))
            .tag(AeroExtraTags.BlockTags.SABLE_HEAVY.tag)
            .simpleItem()
            .transform(ModelTransform.blockWithDefaultModel())
            .register();

    public static final BlockEntry<CVTGearshiftBlock> CVT_GEARSHIFT = builder(AeroExtraCreativeTabs.SIMULATED, "cvt_gearshift", CVTGearshiftBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.isRedstoneConductor(AeroExtraBlocks::never))
            .simpleItem()
            .transform(TagGen.axeOrPickaxe())
            .register();

    private static <T extends Block> BlockBuilder<T, CreateRegistrate> builder(ResourceLocation section, String id, NonNullFunction<BlockBehaviour.Properties, T> factory) {
        return REGISTRATE.inSection(section).block(id, factory)
                .setData(ProviderType.LANG, (ctx, prov) -> {});
    }

    private static boolean never(BlockState var1, BlockGetter var2, BlockPos var3) {
        return false;
    }

    public static void register() {}

    private static class ModelTransform {
        private static <T extends Block> NonNullFunction<BlockBuilder<T, CreateRegistrate>, BlockBuilder<T, CreateRegistrate>> blockWithDefaultModel() {
            return builder -> builder.blockstate((ctx, prov) ->
                    prov.simpleBlock(ctx.getEntry(), prov.models().getExistingFile(prov.modLoc("block/" + builder.getName()))));
        }

        private static <T extends Block> NonNullFunction<BlockBuilder<T, CreateRegistrate>, BlockBuilder<T, CreateRegistrate>> directionalBlock(DirectionProperty property, String modelPath) {
            return builder -> builder.blockstate((ctx, prov) -> {
                prov.getVariantBuilder(ctx.getEntry()).forAllStates((state) -> {
                    Direction dir = state.getValue(property);
                    return ConfiguredModel.builder().modelFile(prov.models().getExistingFile(prov.modLoc("block/" + modelPath)))
                            .rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0))
                            .rotationY(dir.getAxis().isVertical() ? 0 : (int) (dir.toYRot() + 180) % 360)
                            .build();
                });
            });
        }
    }
}
