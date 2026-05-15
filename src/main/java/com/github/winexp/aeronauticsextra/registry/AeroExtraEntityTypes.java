package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.client.entity.renderer.SmallBalloonEntityRenderer;
import com.github.winexp.aeronauticsextra.content.entity.SmallBalloonEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.EntityBuilder;
import com.tterrag.registrate.providers.ProviderType;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;

public class AeroExtraEntityTypes {
    private static final SimulatedRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final DeferredHolder<EntityType<?>, EntityType<SmallBalloonEntity>> SMALL_BALLOON = builder("small_balloon", SmallBalloonEntity::new, MobCategory.MISC)
            .properties(builder -> builder
                    .sized(0.7f, 0.7f)
                    .canSpawnFarFromPlayer()
                    .clientTrackingRange(8))
            .renderer(() -> SmallBalloonEntityRenderer::new)
            .register();

    private static <T extends Entity> EntityBuilder<T, CreateRegistrate> builder(String id, EntityType.EntityFactory<T> factory, MobCategory category) {
        return REGISTRATE.entity(id, factory, category).setData(ProviderType.LANG, (ctx, prov) -> {});
    }

    public static void register() {}
}
