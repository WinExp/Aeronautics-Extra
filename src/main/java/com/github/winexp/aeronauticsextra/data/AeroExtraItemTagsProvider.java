package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.registry.AeroExtraItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static com.github.winexp.aeronauticsextra.registry.AeroExtraItemTags.*;

public class AeroExtraItemTagsProvider extends ItemTagsProvider {
    public AeroExtraItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, AeronauticsExtra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ANTENNA).add(AeroExtraItems.ANDESITE_ANTENNA.get());
        this.tag(ANTENNA).add(AeroExtraItems.BRASS_ANTENNA.get());
    }
}
