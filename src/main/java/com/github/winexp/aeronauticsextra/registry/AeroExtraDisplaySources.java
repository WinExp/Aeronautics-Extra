package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.display_source.GPSReceiverDisplaySource;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.registry.CreateRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AeroExtraDisplaySources {
    private static final DeferredRegister<DisplaySource> REGISTER = DeferredRegister.create(CreateRegistries.DISPLAY_SOURCE, AeronauticsExtra.MOD_ID);

    public static final DeferredHolder<DisplaySource, GPSReceiverDisplaySource> GPS_RECEIVER = register("gps_receiver",
            GPSReceiverDisplaySource::new, AeroExtraBlockEntityTypes.GPS_RECEIVER);

    private static <T extends DisplaySource> DeferredHolder<DisplaySource, T> register(String name, Supplier<T> supplier,
                                                                                       Supplier<? extends BlockEntityType<?>> blockEntityType) {
        Supplier<T> supplier1 = () -> {
            T source = supplier.get();
            DisplaySource.BY_BLOCK_ENTITY.add(blockEntityType.get(), source);
            return source;
        };
        return REGISTER.register(name, supplier1);
    }

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
