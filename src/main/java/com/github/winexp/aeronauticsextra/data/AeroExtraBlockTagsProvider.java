package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.github.winexp.aeronauticsextra.registry.AeroExtraBlockTags.*;

public class AeroExtraBlockTagsProvider extends BlockTagsProvider {
    public AeroExtraBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AeronauticsExtra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(AeroExtraBlocks.GPS_SATELLITE.get())
                .add(AeroExtraBlocks.GPS_RECEIVER.get());
        this.tag(HEAVY)
                .add(AeroExtraBlocks.GPS_RECEIVER.get());
        this.tag(SUPER_HEAVY)
                .add(AeroExtraBlocks.GPS_SATELLITE.get());
    }
}
