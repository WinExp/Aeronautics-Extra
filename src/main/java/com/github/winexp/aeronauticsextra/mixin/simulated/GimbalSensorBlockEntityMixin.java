package com.github.winexp.aeronauticsextra.mixin.simulated;

import com.github.winexp.aeronauticsextra.AeroExtraLang;
import com.github.winexp.aeronauticsextra.mixin_interface.simulated.GimbalSensorBlockEntityExtension;
import com.llamalad7.mixinextras.sugar.Local;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.simulated_team.simulated.content.blocks.gimbal_sensor.GimbalSensorBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.joml.Quaterniond;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GimbalSensorBlockEntity.class)
public class GimbalSensorBlockEntityMixin implements GimbalSensorBlockEntityExtension {
    @Unique
    private double aero_extra$YAngle;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Ldev/simulated_team/simulated/content/blocks/gimbal_sensor/GimbalSensorBlockEntity;setPower(DLnet/minecraft/core/Direction;)V", ordinal = 0))
    private void getYAngle(CallbackInfo ci, @Local(name = "subLevel") SubLevel subLevel) {
        Quaterniond q = subLevel.logicalPose().orientation();

        double siny_cosp = 2.0 * (q.w() * q.y() + q.z() * q.x());
        double cosy_cosp = 1.0 - 2.0 * (q.y() * q.y() + q.x() * q.x());
        this.aero_extra$YAngle = -Math.atan2(siny_cosp, cosy_cosp);
    }

    @Inject(method = "addToGoggleTooltip", at = @At(value = "INVOKE", target = "Ldev/simulated_team/simulated/data/SimLang;translate(Ljava/lang/String;[Ljava/lang/Object;)Lnet/createmod/catnip/lang/LangBuilder;", ordinal = 1))
    private void addGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        Component y = Component.literal("%.2f".formatted(Math.toDegrees(this.aero_extra$YAngle)))
                .withStyle(ChatFormatting.GREEN);
        AeroExtraLang.translate("gimbal_sensor.y_angle", y)
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 2);
    }

    @Override
    public double aero_extra$getYAngle() {
        return this.aero_extra$YAngle;
    }
}
