package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.entity.SmallBalloonEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AeroExtraEntityTypes {
    private static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, AeronauticsExtra.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SmallBalloonEntity>> SMALL_BALLOON = register("small_balloon",
            () -> EntityType.Builder.of(SmallBalloonEntity::new, MobCategory.MISC)
                    .sized(0.7f, 0.7f)
                    .canSpawnFarFromPlayer()
                    .clientTrackingRange(8)
                    .updateInterval(3));

    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String key, Supplier<EntityType.Builder<T>> supplier) {
        Supplier<EntityType<T>> supplier1 = () -> supplier.get().build(key);
        return REGISTER.register(key, supplier1);
    }

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
