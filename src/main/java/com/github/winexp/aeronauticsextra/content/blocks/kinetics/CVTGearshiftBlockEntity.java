package com.github.winexp.aeronauticsextra.content.blocks.kinetics;

import com.github.winexp.aeronauticsextra.data.AeroExtraLang;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.simulated_team.simulated.mixin_interface.extra_kinetics.KineticBlockEntityExtension;
import dev.simulated_team.simulated.util.extra_kinetics.ExtraBlockPos;
import dev.simulated_team.simulated.util.extra_kinetics.ExtraKinetics;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CVTGearshiftBlockEntity extends KineticBlockEntity implements ExtraKinetics {
    private static final Component SCROLL_OPTION_TITLE = AeroExtraLang.translate("scroll_option.shift_speed").component();

    private final CVTGearshiftCogwheel extraWheel;

    private float ratio = 1;
    private boolean oversaturated;

    private boolean alreadySentEffects;
    private boolean needsUpdate;
    private ScrollValueBehaviour scrollValueBehaviour;

    public CVTGearshiftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.extraWheel = new CVTGearshiftCogwheel(type, new ExtraBlockPos(pos), state, this);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.scrollValueBehaviour = new CVTGearshiftValueBehaviour(SCROLL_OPTION_TITLE, this, new CVTGearshiftValueBoxTransform());
        behaviours.add(this.scrollValueBehaviour);
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            BlockPos pos = this.getBlockPos();
            BlockState state = this.getBlockState();
            Direction left = CVTGearshiftBlock.getLeftDirection(state);
            Direction right = CVTGearshiftBlock.getRightDirection(state);
            int leftSignal = this.level.getSignal(pos.relative(left), left);
            int rightSignal = this.level.getSignal(pos.relative(right), right);

            int signalDiff = leftSignal - rightSignal;
            if (signalDiff != 0) {
                this.setRatio(this.getRatio() + (signalDiff / 15f * 0.33f * this.scrollValueBehaviour.getValue() / 100));
            }

            if (this.needsUpdate) {
                this.detachKinetics();
                this.extraWheel.detachKinetics();
                this.removeSource();
                this.extraWheel.removeSource();

                if (((KineticBlockEntityExtension) this).simulated$getConnectedToExtraKinetics()) {
                    this.attachKinetics();
                    this.extraWheel.attachKinetics();
                } else {
                    this.extraWheel.attachKinetics();
                    this.attachKinetics();
                }
                this.needsUpdate = false;
            }

        } else if (this.oversaturated) {
            if (!this.alreadySentEffects) {
                this.alreadySentEffects = true;
                this.effects.triggerOverStressedEffect();
            }
        } else {
            this.alreadySentEffects = false;
        }

        this.extraWheel.tick();
        super.tick();
    }

    public float getRatio() {
        return this.ratio;
    }

    public void setRatio(float ratio) {
        if (this.ratio == ratio) return;
        ratio = Math.clamp(ratio, 0.1f, 10);
        this.ratio = ratio;
        this.needsUpdate = true;
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff,
                                     boolean connectedViaAxes, boolean connectedViaCogs) {
        float modifier = 0;

        if (target == this.extraWheel) {
            modifier = this.ratio;
            if (this.oversaturated) {
                return 0;
            } else if (Math.abs(this.getTheoreticalSpeed() * modifier) > AllConfigs.server().kinetics.maxRotationSpeed.get()) {
                this.oversaturated = true;
                return 0;
            } else {
                this.oversaturated = false;
            }
        } else if (target == this) {
            modifier = 1 / this.ratio;
            if (this.oversaturated) {
                return 0;
            } else if (Math.abs(this.extraWheel.getTheoreticalSpeed() * modifier) > AllConfigs.server().kinetics.maxRotationSpeed.get()) {
                this.oversaturated = true;
                return 0;
            } else {
                this.oversaturated = false;
            }
        } else {
            this.oversaturated = false;
        }

        return modifier;
    }

    @Override
    public boolean isOverStressed() {
        if (this.level.isClientSide) {
            return this.oversaturated || this.overStressed;
        }

        return super.isOverStressed();
    }

    @Override
    protected void read(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.read(compound, registries, clientPacket);

        this.oversaturated = compound.getBoolean("oversaturated");
        if (compound.contains("ratio")) {
            this.ratio = compound.getFloat("ratio");
        }
    }

    @Override
    protected void write(final CompoundTag compound, final HolderLookup.Provider registries, final boolean clientPacket) {
        super.write(compound, registries, clientPacket);

        compound.putBoolean("oversaturated", this.oversaturated);
        compound.putFloat("ratio", this.ratio);
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

    private static class CVTGearshiftValueBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(12, 8, 16);
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Vec3 location = getSouthLocation();
            if (((IRotate) state.getBlock()).getRotationAxis(state) == Direction.Axis.Y
            || (CVTGearshiftBlock.getLeftDirection(state).getAxis() == Direction.Axis.Y
                    && !state.getValue(CVTGearshiftBlock.AXIS_ALONG_FIRST_COORDINATE))) {
                location = location.add(VecHelper.voxelSpace(-4, -4, 0));
            }
            location = VecHelper.rotateCentered(location, AngleHelper.horizontalAngle(getSide()), Direction.Axis.Y);
            location = VecHelper.rotateCentered(location, AngleHelper.verticalAngle(getSide()), Direction.Axis.X);
            return location;
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return direction == CVTGearshiftBlock.getLeftDirection(state) || direction == CVTGearshiftBlock.getRightDirection(state);
        }

        @Override
        public float getScale() {
            return 0.33f;
        }
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
