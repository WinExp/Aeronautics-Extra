package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSReceiverBlockEntity;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AeroExtraBlockEntityTypes {
    private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AeronauticsExtra.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GPSSatelliteBlockEntity>> GPS_SATELLITE = REGISTER
            .register("gps_satellite", () -> BlockEntityType.Builder
                    .of(GPSSatelliteBlockEntity::new, AeroExtraBlocks.GPS_SATELLITE.get())
                    .build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GPSReceiverBlockEntity>> GPS_RECEIVER = REGISTER
            .register("gps_receiver", () -> BlockEntityType.Builder
                    .of(GPSReceiverBlockEntity::new, AeroExtraBlocks.GPS_RECEIVER.get())
                    .build(null));

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
