package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.item.SmallBalloonItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public class AeroExtraItems {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final ItemEntry<Item> ANDESITE_ANTENNA = builder(AeroExtraCreativeTabs.GEOMATICS, "andesite_antenna", Item::new)
            .properties(p -> p
                    .component(AeroExtraDataComponents.GPS_BROADCAST_STRENGTH, 0.8f)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL, 7))
            .tag(AeroExtraTags.ItemTags.ANTENNAS.tag)
            .register();
    public static final ItemEntry<Item> BRASS_ANTENNA = builder(AeroExtraCreativeTabs.GEOMATICS, "brass_antenna", Item::new)
            .properties(p -> p
                    .component(AeroExtraDataComponents.GPS_BROADCAST_STRENGTH, 1.6f)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL, 3))
            .tag(AeroExtraTags.ItemTags.ANTENNAS.tag)
            .register();

    public static final ItemEntry<SmallBalloonItem> SMALL_BALLOON = builder(AeroExtraCreativeTabs.AERONAUTICS_EXTRA, "small_balloon", SmallBalloonItem::new)
            .properties(p -> p
                    .component(DataComponents.BASE_COLOR, DyeColor.WHITE))
            .register();

    private static <T extends Item> ItemBuilder<T, CreateRegistrate> builder(ResourceLocation section, String id, NonNullFunction<Item.Properties, T> factory) {
        return REGISTRATE.inSection(section).item(id, factory)
                .setData(ProviderType.LANG, (ctx, prov) -> {});
    }

    public static void register() {}
}
