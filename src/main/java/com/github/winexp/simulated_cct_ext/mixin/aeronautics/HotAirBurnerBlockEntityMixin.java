package com.github.winexp.simulated_cct_ext.mixin.aeronautics;

import com.github.winexp.simulated_cct_ext.mixin_interface.aeronautics.HotAirBurnerBlockEntityExtension;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.eriksonn.aeronautics.content.blocks.hot_air.hot_air_burner.HotAirBurnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HotAirBurnerBlockEntity.class)
public class HotAirBurnerBlockEntityMixin implements HotAirBurnerBlockEntityExtension {
    @Shadow
    protected ScrollValueBehaviour hotAirAmountBehaviour;

    @Override
    public int sce$getHotAirAmount() {
        return this.hotAirAmountBehaviour.getValue();
    }

    @Override
    public void sce$setHotAirAmount(int amount) {
        this.hotAirAmountBehaviour.setValue(amount);
    }
}
