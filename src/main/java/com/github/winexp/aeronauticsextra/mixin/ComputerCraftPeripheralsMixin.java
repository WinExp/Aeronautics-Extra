package com.github.winexp.aeronauticsextra.mixin;

import com.github.winexp.aeronauticsextra.impl.CCTPeripherals;
import com.llamalad7.mixinextras.sugar.Local;
import dev.simulated_team.simulated.compat.computercraft.ComputerCraftPeripherals;
import dev.simulated_team.simulated.service.compat.SimPeripheralService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComputerCraftPeripherals.class)
public class ComputerCraftPeripheralsMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci, @Local(name = "service") SimPeripheralService service) {
        CCTPeripherals.init(service);
    }
}
