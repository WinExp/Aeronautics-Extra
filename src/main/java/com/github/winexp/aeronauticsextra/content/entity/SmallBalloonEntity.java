package com.github.winexp.aeronauticsextra.content.entity;

import com.github.winexp.aeronauticsextra.content.entity.physics.EntitySubLevelActor;
import com.github.winexp.aeronauticsextra.registry.AeroExtraEntityTypes;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.force.ForceGroups;
import dev.ryanhcode.sable.api.physics.force.QueuedForceGroup;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.mixinterface.entity.entity_sublevel_collision.EntityMovementExtension;
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SmallBalloonEntity extends LivingEntity implements Leashable, EntitySubLevelActor {
    public static final double MAX_ENTITY_LIFT = 0.2;
    public static final double LEASH_LENGTH = 4;
    public static final float MAX_ANGLE = 45;

    private static final EntityDataAccessor<Byte> COLOR = SynchedEntityData.defineId(SmallBalloonEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Float> ENTITY_LIFT = SynchedEntityData.defineId(SmallBalloonEntity.class, EntityDataSerializers.FLOAT);
    private LeashData leashData;
    private float prevMotionYaw;
    private float yawRate;
    private final AxisAngle4f rotation = new AxisAngle4f();
    private final AxisAngle4f prevRotation = new AxisAngle4f();

    public SmallBalloonEntity(EntityType<SmallBalloonEntity> entityType, Level level) {
        super(entityType, level);
        this.setDiscardFriction(true);
    }

    public static SmallBalloonEntity create(Level level, double x, double y, double z, DyeColor color) {
        SmallBalloonEntity entity = new SmallBalloonEntity(AeroExtraEntityTypes.SMALL_BALLOON.get(), level);
        entity.setPos(x, y, z);
        entity.xo = x;
        entity.yo = y;
        entity.zo = z;
        entity.setColor(color);
        return entity;
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(COLOR));
    }

    public void setColor(DyeColor color) {
        this.entityData.set(COLOR, (byte) color.getId());
    }

    public float getEntityLift() {
        return this.entityData.get(ENTITY_LIFT);
    }

    public void setEntityLift(float lift) {
        this.entityData.set(ENTITY_LIFT, lift);
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide && reason.shouldDestroy() && this.isLeashed()) {
            this.dropLeash(true, true);
        }
        super.remove(reason);
    }

    @Override
    public float getViewYRot(float partialTicks)  {
        return Mth.rotLerp(partialTicks, this.yRotO, this.getYRot());
    }

    protected void tickLeashMovement(Entity leashHolder, float distance) {
        if (distance > LEASH_LENGTH) {
            Vec3 holderPos = Sable.HELPER.projectOutOfSubLevel(leashHolder.level(), leashHolder.position());
            Vec3 direction = holderPos.subtract(this.position()).normalize().scale(distance - LEASH_LENGTH);
            Vec3 motion = this.getDeltaMovement();
            if (motion.dot(direction) > 0) {
                motion = motion.add(direction.scale(0.15));
            } else {
                motion = motion.add(direction.scale(0.20));
            }
            this.setDeltaMovement(motion);
        }
    }

    @Override
    public void elasticRangeLeashBehaviour(Entity leashHolder, float distance) {
        this.tickLeashMovement(leashHolder, distance);
    }

    @Override
    public void closeRangeLeashBehaviour(Entity leashHolder) {
        this.tickLeashMovement(leashHolder, this.distanceTo(leashHolder));
    }

    @Override
    protected Vec3 getLeashOffset() {
        return Vec3.ZERO;
    }

    protected void liftEntity(Entity entity) {
        double entityYVel = entity.getDeltaMovement().y;
        if (!this.level().isClientSide) {
            float distance = this.distanceTo(entity);
            if (distance > LEASH_LENGTH && this.getY() > entity.getY() && (!(entity instanceof Player player) || !player.getAbilities().flying)) {
                Vec3 direction = this.position().subtract(entity.position()).normalize();
                double lift = direction.y;
                if (entityYVel > 0) {
                    lift *= 0.024;
                } else {
                    lift *= 0.036;
                }
                if (entityYVel >= MAX_ENTITY_LIFT) lift = 0;
                else if (entityYVel + lift >= MAX_ENTITY_LIFT) lift = entityYVel + lift - MAX_ENTITY_LIFT;
                this.setEntityLift((float) lift);
                entity.checkSlowFallDistance();
            } else this.setEntityLift(0);
        }
        entity.addDeltaMovement(new Vec3(0, this.getEntityLift(), 0));
    }

    @Override
    public void subLevelPhysicsTick(ServerSubLevel subLevel, RigidBodyHandle handle, double timeStep) {
        if (this.getLeashHolder() instanceof LeashFenceKnotEntity entity && Sable.HELPER.getContaining(entity) == subLevel) {
            float distance = this.distanceTo(entity);
            Vec3 holderPos = Sable.HELPER.projectOutOfSubLevel(entity.level(), entity.position());
            if (distance > LEASH_LENGTH && this.getY() > holderPos.y) {
                Vector3d direction = JOMLConversion.toJOML(this.position().subtract(holderPos).normalize());
                Vector3d forcePoint = JOMLConversion.atCenterOf(entity.blockPosition());
                Vector3d lift = subLevel.logicalPose().transformNormalInverse(new Vector3d(0, 0.7, 0).mul(direction));
                double airPressure = DimensionPhysicsData.getAirPressure(entity.level(), JOMLConversion.toJOML(holderPos));
                lift.mul(airPressure);
                QueuedForceGroup queuedForceGroup = subLevel.getOrCreateQueuedForceGroup(ForceGroups.BALLOON_LIFT.get());
                queuedForceGroup.applyAndRecordPointForce(forcePoint, lift);
            }
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.87, 0.91, 0.87));
        super.travel(travelVector);
    }

    protected void tickRotation(Vec3 motion) {
        float horizontalSpeed = (float) Mth.clamp(motion.horizontalDistance() - 0.075, 0, 0.20) / 0.20f;
        float motionYaw = (float) Mth.atan2(motion.z, motion.x) * Mth.RAD_TO_DEG - 90f;
        float deltaRotY = Mth.wrapDegrees((motionYaw - this.prevMotionYaw) * 5);
        this.yawRate *= 0.95f;
        this.yawRate += (deltaRotY * horizontalSpeed * 0.2f - this.yawRate) * horizontalSpeed * 0.15f;
        this.setYRot(Mth.wrapDegrees(this.getYRot() + this.yawRate));
        this.prevMotionYaw = motionYaw;
        if (!this.level().isClientSide) {
        } else {
            this.prevRotation.set(this.rotation);
            Vec3 axis = motion.multiply(1, 0, 1).cross(new Vec3(0, 1, 0)).normalize();

            if (axis != Vec3.ZERO) {
                this.rotation.x = (float) axis.x;
                this.rotation.y = (float) axis.y;
                this.rotation.z = (float) axis.z;
            }
            this.rotation.angle = (float) Math.min(motion.horizontalDistance(), 0.50) / 0.50f * MAX_ANGLE * Mth.DEG_TO_RAD;

            Quaternionf result = new Quaternionf();
            Quaternionf prevRotQ = this.prevRotation.get(new Quaternionf());
            Quaternionf rotQ = this.rotation.get(new Quaternionf());
            prevRotQ.slerp(rotQ, 0.2f, result);
            result.w = Math.clamp(result.w, -1, 1); // w may exceed [-1, 1] after slerp, clamp it
            this.rotation.set(result);
        }
    }

    public Quaternionf getRotation(float partialTicks) {
        Quaternionf rotation = new Quaternionf();
        this.prevRotation.get(new Quaternionf()).slerp(this.rotation.get(new Quaternionf()), partialTicks, rotation);
        rotation.w = Math.clamp(rotation.w, -1, 1); // w may exceed [-1, 1] after slerp, clamp it
        return rotation;
    }

    @Override
    public void tick() {
        Vec3 prevPos = this.position();
        super.tick();
        this.tickRotation(this.position().subtract(prevPos));
        if (this.getLeashHolder() != null)  {
            this.liftEntity(this.getLeashHolder());
        }
        if (this.isAlive() && !this.level().isClientSide) {
            if (this.position().y > this.level().getMaxBuildHeight() + 64) {
                this.hurt(this.damageSources().outOfBorder(), 1);
            }
        }
        EntityMovementExtension extension = (EntityMovementExtension) this;
        extension.sable$setTrackingSubLevel(null);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) && source.is(DamageTypes.MAGIC);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && !this.isRemoved()) {
            if (this.isInvulnerableTo(source)) {
                return false;
            } else {
                this.remove(RemovalReason.KILLED);
                return false;
            }
        }
        return false;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, (byte) DyeColor.WHITE.getId());
        builder.define(ENTITY_LIFT, 0f);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.leashData = this.readLeashData(tag);
        this.getEntityData().set(COLOR, tag.getByte("Color"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        this.writeLeashData(tag, this.leashData);
        tag.putByte("Color", this.getEntityData().get(COLOR));
    }

    @Override
    public @Nullable LeashData getLeashData() {
        return this.leashData;
    }

    @Override
    public void setLeashData(Leashable.@Nullable LeashData leashData) {
        this.leashData = leashData;
    }
}
