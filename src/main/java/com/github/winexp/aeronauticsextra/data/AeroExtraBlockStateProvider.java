package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AeroExtraBlockStateProvider extends BlockStateProvider {
    private final ExistingFileHelper existingFileHelper;

    public AeroExtraBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, AeronauticsExtra.MOD_ID, exFileHelper);
        this.existingFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        ModelFile gpsSatelliteModel = this.models().withExistingParent("gps_satellite", this.modLoc("block/gps_satellite/block"));
        this.simpleBlockWithItem(AeroExtraBlocks.GPS_SATELLITE.get(), gpsSatelliteModel);
        this.blockWithDefaultModel(AeroExtraBlocks.GPS_RECEIVER.get());
        this.blockItemWithDefaultModel(AeroExtraBlocks.GPS_RECEIVER.get());
    }

    private ModelFile getDefaultBlockModel(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        return new ModelFile.ExistingModelFile(ResourceLocation.fromNamespaceAndPath(key.getNamespace(), "block/" + key.getPath()), this.existingFileHelper);
    }

    private void blockWithDefaultModel(Block block) {
        this.getVariantBuilder(block).partialState().setModels(ConfiguredModel.builder()
                .modelFile(this.getDefaultBlockModel(block)).build());
    }

    private void blockItemWithDefaultModel(Block block) {
        this.simpleBlockItem(block, getDefaultBlockModel(block));
    }
}
