package com.github.winexp.aeronauticsextra;

import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
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

    public static void addGenerator() {
        AeronauticsExtra.getRegistrate().addDataGenerator(ProviderType.ITEM_TAGS, AeroExtraItemTags::generateTags);
    }

    private static void generateTags(RegistrateTagsProvider<Item> providerIn) {
        TagGen.CreateTagsProvider<Item> provider = new TagGen.CreateTagsProvider<>(providerIn, Item::builtInRegistryHolder);
        provider.tag(GPS_CORE).add(AeroExtraItems.ANDESITE_GPS_CORE.get());
        provider.tag(GPS_CORE).add(AeroExtraItems.BRASS_GPS_CORE.get());
    }
}
