package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AeroExtraBlockTags {
    public static final TagKey<Block> HEAVY = create("sable", "heavy");
    public static final TagKey<Block> SUPER_HEAVY = create("sable", "super_heavy");

    private static TagKey<Block> create(String path) {
        return create(AeronauticsExtra.MOD_ID, path);
    }
    private static TagKey<Block> create(String namespace, String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
