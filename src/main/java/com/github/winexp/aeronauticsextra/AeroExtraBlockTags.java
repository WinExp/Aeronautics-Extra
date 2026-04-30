package com.github.winexp.aeronauticsextra;

import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AeroExtraBlockTags {
    public static final TagKey<Block> SUPER_HEAVY = create("sable", "super_heavy");

    private static TagKey<Block> create(String path) {
        return create(AeronauticsExtra.MOD_ID, path);
    }
    private static TagKey<Block> create(String namespace, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    public static void addGenerator() {
        AeronauticsExtra.getRegistrate().addDataGenerator(ProviderType.BLOCK_TAGS, AeroExtraBlockTags::generateBlockTags);
    }

    private static void generateBlockTags(RegistrateTagsProvider<Block> providerIn) {
        TagGen.CreateTagsProvider<Block> provider = new TagGen.CreateTagsProvider<>(providerIn, Block::builtInRegistryHolder);
    }
}
