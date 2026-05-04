package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AeroExtraCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, AeronauticsExtra.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AERO_EXTRA = REGISTER.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + AeronauticsExtra.MOD_ID))
            .icon(() -> new ItemStack(AeroExtraBlocks.GPS_SATELLITE))
            .displayItems((params, output) -> {
                output.accept(AeroExtraBlocks.GPS_SATELLITE);
                output.accept(AeroExtraBlocks.GPS_RECEIVER);
                output.accept(AeroExtraItems.ANDESITE_GPS_CORE);
                output.accept(AeroExtraItems.BRASS_GPS_CORE);
            })
            .build());

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
