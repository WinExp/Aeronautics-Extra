package com.github.winexp.simulated_cct_ext.mixin.aeronautics;

import com.github.winexp.simulated_cct_ext.mixin_interface.aeronautics.HotAirBurnerBlockEntityExtension;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.eriksonn.aeronautics.content.blocks.hot_air.steam_vent.SteamVentBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SteamVentBlockEntity.class)
public class SteamVentBlockEntityMixin implements HotAirBurnerBlockEntityExtension {
    @Shadow
    protected ScrollValueBehaviour steamAmountBehaviour;

    @Override
    public int sce$getHotAirAmount() {
        return this.steamAmountBehaviour.getValue();
    }

    @Override
    public void sce$setHotAirAmount(int amount) {
        this.steamAmountBehaviour.setValue(amount);
    }
}
