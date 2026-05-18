package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AeroExtraTags {
    public enum Namespace {
        AERO_EXTRA(AeronauticsExtra.MOD_ID),
        SABLE("sable");

        public final String id;

        Namespace(String id) {
            this.id = id;
        }

        public ResourceLocation id(String path) {
            return ResourceLocation.fromNamespaceAndPath(this.id, path);
        }

        public ResourceLocation id(Enum<?> entry, @Nullable String pathOverride) {
            return this.id(pathOverride != null ? pathOverride : Lang.asId(entry.name()));
        }
    }

    public enum ItemTags {
        ANTENNAS;

        public final TagKey<Item> tag;

        ItemTags() {
            this(Namespace.AERO_EXTRA);
        }

        ItemTags(Namespace namespace) {
            this(namespace, null);
        }

        ItemTags(Namespace namespace, @Nullable String pathOverride) {
            this.tag = TagKey.create(Registries.ITEM, namespace.id(this, pathOverride));
        }
    }

    public enum BlockTags {
        SABLE_HEAVY(Namespace.SABLE, "heavy");

        public final TagKey<Block> tag;

        BlockTags() {
            this(Namespace.AERO_EXTRA);
        }

        BlockTags(Namespace namespace) {
            this(namespace, null);
        }

        BlockTags(Namespace namespace, @Nullable String pathOverride) {
            this.tag = TagKey.create(Registries.BLOCK, namespace.id(this, pathOverride));
        }

        public boolean matches(BlockState state) {
            return state.is(this.tag);
        }
    }
}
