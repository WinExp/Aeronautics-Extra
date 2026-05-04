package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class AeroExtraDataComponents {
    private static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AeronauticsExtra.MOD_ID);

    public static final DataComponentType<Float> GPS_BROADCAST_STRENGTH = register("gps_broadcast_strength", builder -> builder
            .persistent(Codec.floatRange(0, Float.MAX_VALUE))
            .networkSynchronized(ByteBufCodecs.FLOAT));
    public static final DataComponentType<Integer> GPS_BROADCAST_INTERVAL = register("gps_broadcast_interval", builder -> builder
            .persistent(Codec.intRange(1, Integer.MAX_VALUE))
            .networkSynchronized(ByteBufCodecs.INT));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        REGISTER.register(name, () -> type);
        return type;
    }

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
