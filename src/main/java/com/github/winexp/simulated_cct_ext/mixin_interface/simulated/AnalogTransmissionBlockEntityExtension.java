package com.github.winexp.simulated_cct_ext.mixin_interface.simulated;

public interface AnalogTransmissionBlockEntityExtension {
    float sce$getOverrideSignal();

    void sce$setOverrideSignal(final float power);
}
