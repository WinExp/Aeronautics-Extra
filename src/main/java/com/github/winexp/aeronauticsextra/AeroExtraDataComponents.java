package com.github.winexp.aeronauticsextra;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class AeroExtraDataComponents {
    private static final DeferredRegister.DataComponents REGISTER = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, AeronauticsExtra.MOD_ID);

    public static final DataComponentType<Double> GPS_ERROR = register("gps_error", builder -> builder
            .persistent(Codec.doubleRange(0, Double.MAX_VALUE))
            .networkSynchronized(ByteBufCodecs.DOUBLE));

    private static <T> DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        DataComponentType<T> type = builder.apply(DataComponentType.builder()).build();
        REGISTER.register(name, () -> type);
        return type;
    }

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
