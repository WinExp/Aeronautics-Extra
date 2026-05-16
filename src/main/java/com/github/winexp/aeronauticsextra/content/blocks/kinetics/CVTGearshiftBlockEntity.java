package com.github.winexp.aeronauticsextra.content.blocks.kinetics;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import dev.simulated_team.simulated.util.extra_kinetics.ExtraBlockPos;
import dev.simulated_team.simulated.util.extra_kinetics.ExtraKinetics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CVTGearshiftBlockEntity extends KineticBlockEntity implements ExtraKinetics {
    private final CVTGearshiftCogwheel extraWheel;

    private boolean oversaturated = false;
    boolean alreadySentEffects = false;

    public CVTGearshiftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.extraWheel = new CVTGearshiftCogwheel(type, new ExtraBlockPos(pos), state, this);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
        } else if (this.oversaturated) {
            if (!this.alreadySentEffects) {
                this.effects.triggerOverStressedEffect();
                this.alreadySentEffects = true;
            }
        } else {
            this.alreadySentEffects = false;
        }

        this.extraWheel.tick();
        super.tick();
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff,
                                     boolean connectedViaAxes, boolean connectedViaCogs) {
        float factor = 0;

        if (target == this.extraWheel) {
            factor = 1;
        } else if (target == this) {
            factor = 1;
        }

        return factor;
    }

    @Override
    public boolean isOverStressed() {
        if (this.level.isClientSide) {
            return this.oversaturated || this.overStressed;
        }

        return super.isOverStressed();
    }

    @Override
    protected void write(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        compound.putBoolean("oversaturated", this.oversaturated);
    }

    @Override
    protected void read(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        this.oversaturated = compound.getBoolean("oversaturated");
    }

    @Override
    public KineticBlockEntity getExtraKinetics() {
        return this.extraWheel;
    }

    @Override
    public boolean shouldConnectExtraKinetics() {
        return true;
    }

    @Override
    public String getExtraKineticsSaveName() {
        return "ExtraCogwheel";
    }

    public static class CVTGearshiftCogwheel extends KineticBlockEntity implements ExtraKineticsBlockEntity {
        public static final ICogWheel EXTRA_COGWHEEL_CONFIG = new ICogWheel() {
            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                return false;
            }

            @Override
            public Direction.Axis getRotationAxis(BlockState state) {
                return ((IRotate) state.getBlock()).getRotationAxis(state);
            }
        };

        private final CVTGearshiftBlockEntity parent;

        public CVTGearshiftCogwheel(BlockEntityType<?> typeIn, BlockPos pos, BlockState state, CVTGearshiftBlockEntity parent) {
            super(typeIn, pos, state);
            this.parent = parent;
        }

        @Override
        public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
            return this.parent.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
        }

        @Override
        protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
            return true;
        }

        @Override
        public KineticBlockEntity getParentBlockEntity() {
            return this.parent;
        }
    }
}
