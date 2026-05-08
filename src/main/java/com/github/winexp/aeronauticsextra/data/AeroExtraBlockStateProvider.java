package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlock;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AeroExtraBlockStateProvider extends BlockStateProvider {
    public AeroExtraBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AeronauticsExtra.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile satelliteModel = this.models().getExistingFile(this.modLoc("block/gps_satellite/block"));
        this.directionalBlock(AeroExtraBlocks.GPS_SATELLITE.get(), GPSSatelliteBlock.FACING, satelliteModel);
        this.simpleBlockItem(AeroExtraBlocks.GPS_SATELLITE.get(), satelliteModel);
        this.blockWithItemWithDefaultModel(AeroExtraBlocks.GPS_RECEIVER.get());
    }

    private ModelFile getDefaultBlockModel(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        return new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "block/" + key.getPath()), this.models().existingFileHelper);
    }

    private void directionalBlock(Block block, DirectionProperty property, ModelFile model) {
        this.getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(property);
            return ConfiguredModel.builder().modelFile(model).rotationX(dir == Direction.DOWN ? 180 : (dir.getAxis().isHorizontal() ? 90 : 0)).rotationY(dir.getAxis().isVertical() ? 0 : ((int) dir.toYRot() + 180) % 360).build();
        });
    }

    private void blockWithItemWithDefaultModel(Block block) {
        this.blockWithDefaultModel(block);
        this.blockItemWithDefaultModel(block);
    }

    private void blockWithDefaultModel(Block block) {
        this.getVariantBuilder(block).partialState().setModels(ConfiguredModel.builder()
                .modelFile(this.getDefaultBlockModel(block)).build());
    }

    private void blockItemWithDefaultModel(Block block) {
        this.simpleBlockItem(block, getDefaultBlockModel(block));
    }
}
