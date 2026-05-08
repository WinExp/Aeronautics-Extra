package com.github.winexp.aeronauticsextra.mixin.simulated;

import com.github.winexp.aeronauticsextra.data.AeroExtraLang;
import com.github.winexp.aeronauticsextra.mixin_interface.simulated.GimbalSensorBlockEntityExtension;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import dev.simulated_team.simulated.content.display_sources.GimbalSensorDisplaySource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(GimbalSensorDisplaySource.class)
public abstract class GimbalSensorDisplaySourceMixin {
    @Shadow
    abstract String getSelectionKey();

    @WrapMethod(method = "getOptions")
    private List<Component> addExtraOptions(Operation<List<Component>> original) {
        ArrayList<Component> options = new ArrayList<>(original.call());
        options.addAll(AeroExtraLang.translatedOptions("display_source.gimbal_sensor", "y_angle"));
        return options;
    }

    @Inject(method = "provideLine", at = @At("TAIL"), cancellable = true)
    private void provideExtraLine(DisplayLinkContext displayLinkContext, DisplayTargetStats displayTargetStats, CallbackInfoReturnable<MutableComponent> cir) {
        if (displayLinkContext.sourceConfig().getInt(this.getSelectionKey()) == 2) {
            GimbalSensorBlockEntityExtension extension = (GimbalSensorBlockEntityExtension) displayLinkContext.getSourceBlockEntity();
            cir.setReturnValue(AeroExtraLang.number(Math.toDegrees(extension.aero_extra$getYAngle())).component());
        }
    }
}
