package com.github.winexp.aeronauticsextra.client;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = AeronauticsExtra.MOD_ID, dist = Dist.CLIENT)
public class AeronauticsExtraClient {
    public AeronauticsExtraClient(IEventBus modEventBus, ModContainer modContainer) {
        AeroExtraPartialModels.init();
    }
}
