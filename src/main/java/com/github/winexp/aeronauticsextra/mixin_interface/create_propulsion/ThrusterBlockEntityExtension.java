package com.github.winexp.aeronauticsextra.mixin_interface.create_propulsion;

public interface ThrusterBlockEntityExtension {
    double aero_extra$getOverrideThrottle();
    void aero_extra$setOverrideThrottle(double throttle);
}
