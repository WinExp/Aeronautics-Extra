package com.github.winexp.aeronauticsextra.registry;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSReceiverBlock;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlock;
import com.simibubi.create.foundation.data.SharedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AeroExtraBlocks {
    private static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(AeronauticsExtra.MOD_ID);

    public static final DeferredBlock<GPSSatelliteBlock> GPS_SATELLITE = REGISTER
            .register("gps_satellite", () -> new GPSSatelliteBlock(BlockBehaviour.Properties
                    .ofFullCopy(SharedProperties.stone())
                    .isRedstoneConductor(AeroExtraBlocks::never)
                    .isSuffocating(AeroExtraBlocks::never)
                    .noOcclusion()));
    public static final DeferredBlock<GPSReceiverBlock> GPS_RECEIVER = REGISTER
            .register("gps_receiver", () -> new GPSReceiverBlock(BlockBehaviour.Properties
                    .ofFullCopy(SharedProperties.stone())
                    .isRedstoneConductor(AeroExtraBlocks::never)));

    private static boolean never(BlockState var1, BlockGetter var2, BlockPos var3) {
        return false;
    }

    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
