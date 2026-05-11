package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.item.SmallBalloonItem;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AeroExtraItems {
    private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(AeronauticsExtra.MOD_ID);

    public static final DeferredItem<BlockItem> GPS_SATELLITE = register("gps_satellite", () ->
            new BlockItem(AeroExtraBlocks.GPS_SATELLITE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> GPS_RECEIVER = register("gps_receiver", () ->
            new BlockItem(AeroExtraBlocks.GPS_RECEIVER.get(), new Item.Properties()));

    public static final DeferredItem<Item> ANDESITE_ANTENNA = register("andesite_antenna", () ->
            new Item(new Item.Properties()
                    .component(AeroExtraDataComponents.GPS_BROADCAST_STRENGTH, 0.8f)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL, 7)));
    public static final DeferredItem<Item> BRASS_ANTENNA = register("brass_antenna", () ->
            new Item(new Item.Properties()
                    .component(AeroExtraDataComponents.GPS_BROADCAST_STRENGTH, 1.6f)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL, 3)));

    public static final DeferredItem<SmallBalloonItem> SMALL_BALLOON = register("small_balloon", () ->
            new SmallBalloonItem(new Item.Properties()
                    .component(DataComponents.BASE_COLOR, DyeColor.WHITE)));

    private static <T extends Item> DeferredItem<T> register(String id, Supplier<T> supplier) {
        Supplier<T> supplier1 = () -> {
            T item = supplier.get();
            TooltipModifier modifier = new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
            TooltipModifier.REGISTRY.register(item, modifier);
            return item;
        };
        return REGISTER.register(id, supplier1);
    }

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
