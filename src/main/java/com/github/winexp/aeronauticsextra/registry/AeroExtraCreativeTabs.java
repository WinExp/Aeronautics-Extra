package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.blocks.AeroExtraBlocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class AeroExtraCreativeTabs {
    private static final Registrate REGISTRATE = AeronauticsExtra.REGISTRATE.get();

    public static final RegistryEntry<CreativeModeTab, CreativeModeTab> AERO_EXTRA = REGISTRATE.defaultCreativeTab("base", builder -> builder
            .title(Component.translatable("itemGroup." + AeronauticsExtra.MOD_ID))
            .icon(() -> new ItemStack(AeroExtraBlocks.GPS_SATELLITE))
            .displayItems((params, output) -> {
                output.accept(AeroExtraBlocks.GPS_SATELLITE, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
            }).build())
            .register();

    public static void register() {}
}
