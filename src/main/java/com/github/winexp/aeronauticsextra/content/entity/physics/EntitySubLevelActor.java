package com.github.winexp.aeronauticsextra.content.entity.physics;

import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;

public interface EntitySubLevelActor {
    default void subLevelPhysicsTick(ServerSubLevel subLevel, RigidBodyHandle handle, double timeStep) {}
}
