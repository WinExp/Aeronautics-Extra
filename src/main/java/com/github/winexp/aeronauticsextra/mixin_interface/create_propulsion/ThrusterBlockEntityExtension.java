package com.github.winexp.aeronauticsextra.mixin_interface.create_propulsion;

public interface ThrusterBlockEntityExtension {
    double aero_extra$getOverrideThrust();
    void aero_extra$setOverrideThrust(double power);
}
