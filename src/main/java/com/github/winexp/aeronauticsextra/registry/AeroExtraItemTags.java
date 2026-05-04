package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class AeroExtraItemTags {
    public static final TagKey<Item> GPS_CORE = create("gps_core");

    private static TagKey<Item> create(String path) {
        return create(AeronauticsExtra.MOD_ID, path);
    }
    private static TagKey<Item> create(String namespace, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
