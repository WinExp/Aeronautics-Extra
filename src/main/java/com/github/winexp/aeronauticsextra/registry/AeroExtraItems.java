package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AeroExtraItems {
    private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(AeronauticsExtra.MOD_ID);

    public static final DeferredItem<BlockItem> GPS_SATELLITE = REGISTER
            .register("gps_satellite", () -> new BlockItem(AeroExtraBlocks.GPS_SATELLITE.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> GPS_RECEIVER = REGISTER
            .register("gps_receiver", () -> new BlockItem(AeroExtraBlocks.GPS_RECEIVER.get(), new Item.Properties()));

    public static final DeferredItem<Item> ANDESITE_GPS_CORE = REGISTER
            .register("andesite_gps_core", () -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_STRENGTH, 0.8f)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL, 7)));
    public static final DeferredItem<Item> BRASS_GPS_CORE = REGISTER
            .register("brass_gps_core", () -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_STRENGTH, 1.6f)
                    .component(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL, 3)));

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
