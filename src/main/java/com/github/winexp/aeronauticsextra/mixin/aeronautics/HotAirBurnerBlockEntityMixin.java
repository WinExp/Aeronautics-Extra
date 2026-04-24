package com.github.winexp.aeronauticsextra.mixin.aeronautics;

import com.github.winexp.aeronauticsextra.mixin_interface.aeronautics.HotAirBurnerBlockEntityExtension;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HotAirBurnerBlockEntity.class)
public class HotAirBurnerBlockEntityMixin implements HotAirBurnerBlockEntityExtension {
    @Shadow
    protected ScrollValueBehaviour hotAirAmountBehaviour;

    @Override
    public int aero_extra$getHotAirAmount() {
        return this.hotAirAmountBehaviour.getValue();
    }

    @Override
    public void aero_extra$setHotAirAmount(int amount) {
        this.hotAirAmountBehaviour.setValue(amount);
    }
}
