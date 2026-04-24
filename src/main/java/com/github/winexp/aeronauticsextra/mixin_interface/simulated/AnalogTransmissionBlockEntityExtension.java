package com.github.winexp.aeronauticsextra.mixin_interface.simulated;

public interface AnalogTransmissionBlockEntityExtension {
    float aero_extra$getOverrideSignal();

    void aero_extra$setOverrideSignal(final float power);
}
