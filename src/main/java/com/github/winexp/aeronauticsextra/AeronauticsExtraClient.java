package com.github.winexp.aeronauticsextra;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = AeronauticsExtra.MOD_ID, dist = Dist.CLIENT)
public class AeronauticsExtraClient {
    public AeronauticsExtraClient(IEventBus modEventBus) {
        modEventBus.addListener(this::init);
    }

    private void init(FMLClientSetupEvent event) {
        AeroExtraPartialModels.init();
    }
}
