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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class SmallBalloonEntity extends Entity implements Leashable, EntitySubLevelActor {
    public static final double ENTITY_MAX_LIFT = 0.3;
    public static final double LEASH_LENGTH = 4;

    private static final EntityDataAccessor<Byte> COLOR = SynchedEntityData.defineId(SmallBalloonEntity.class, EntityDataSerializers.BYTE);
    private LeashData leashData;
    public float zRot;
    public float zRotO;
    private float prevMotionYaw;

    public SmallBalloonEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
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

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.yRotO = packet.getYRot();
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.getEntityData().get(COLOR));
    }

    public void setColor(DyeColor color) {
        this.getEntityData().set(COLOR, (byte) color.getId());
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    public boolean isVerticalCollided() {
        return this.verticalCollision || !this.level().noCollision(this.getBoundingBox().expandTowards(0, 1e-5, 0));
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide && reason.shouldDestroy() && this.isLeashed()) {
            this.dropLeash(true, true);
        }
        super.remove(reason);
    }

    protected void tickLeashMovement(Entity leashHolder, float distance) {
        if (distance > LEASH_LENGTH) {
            Vec3 holderPos = Sable.HELPER.projectOutOfSubLevel(leashHolder.level(), leashHolder.position());
            Vec3 direction = holderPos.subtract(this.position()).normalize().scale(distance - LEASH_LENGTH);
            Vec3 motion = this.getDeltaMovement();
            if (motion.dot(direction) > 0) {
                this.setDeltaMovement(motion.add(direction.scale(0.15)));
            } else {
                this.setDeltaMovement(motion.add(direction.scale(0.2)));
            }
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

    protected void tickLeashOnClient() {
        Entity leashHolder = this.getLeashHolder();
        if (leashHolder != null && leashHolder.level() == this.level()) {
            float distance = this.distanceTo(leashHolder);
            if (distance <= 10) {
                this.tickLeashMovement(leashHolder, distance);
            }
        }
    }

    @Override
    public float getViewXRot(float partialTick) {
        if (partialTick == 1) return this.getXRot();
        float delta = Mth.wrapDegrees(this.getXRot() - this.xRotO);
        return Mth.wrapDegrees(this.xRotO + delta * partialTick);
    }

    @Override
    public float getViewYRot(float partialTick) {
        if (partialTick == 1) return this.getYRot();
        float delta = Mth.wrapDegrees(this.getYRot() - this.yRotO);
        return Mth.wrapDegrees(this.yRotO + delta * partialTick);
    }

    public float getViewZRot(float partialTick) {
        if (partialTick == 1) return this.zRot;
        float delta = Mth.wrapDegrees(this.zRot - this.zRotO);
        return Mth.wrapDegrees(this.zRotO + delta * partialTick);
    }

    protected void liftEntity(Entity entity) {
        if (!(entity instanceof Player player) || !player.getAbilities().flying) {
            float distance = this.distanceTo(entity);
            if (distance > LEASH_LENGTH && this.getY() > entity.getY()) {
                Vec3 direction = this.position().subtract(entity.position()).normalize();
                double lift = direction.y;
                double entityYVel = entity.getDeltaMovement().y;
                if (entityYVel > 0) {
                    lift *= 0.023;
                } else {
                    lift *= 0.036;
                }
                if (entityYVel >= ENTITY_MAX_LIFT) lift = 0;
                else if (entityYVel + lift >= ENTITY_MAX_LIFT) lift = entityYVel + lift - ENTITY_MAX_LIFT;
                entity.addDeltaMovement(new Vec3(0, lift, 0));
                entity.checkSlowFallDistance();
            }
        }
    }

    protected void pushEntities() {
        if (this.level().isClientSide) {
            this.level().getEntities(EntityTypeTest.forClass(Player.class), this.getBoundingBox(), EntitySelector.pushableBy(this))
                    .forEach(this::push);
        } else {
            List<Entity> entities = this.level().getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
            for (Entity entity : entities) {
                this.push(entity);
            }
        }
    }

    @Override
    public void push(double x, double y, double z) {
        super.push(x * 0.2, y * 0.2, z * 0.2);
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
    public void tick() {
        this.addDeltaMovement(new Vec3(0, 0.072, 0));
        this.setDeltaMovement(this.getDeltaMovement().multiply(0.9, 0.9, 0.9));
        if (this.isVerticalCollided()) this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0, 1));
        if (this.level().isClientSide) {
            this.tickLeashOnClient();
        }
        super.tick();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.pushEntities();
        if (this.getLeashHolder() != null)  {
            this.liftEntity(this.getLeashHolder());
        }
        if (!this.level().isClientSide) {
            if (this.isInWall() || this.position().y > this.level().getMaxBuildHeight() + 64) {
                this.kill();
            }
        }
        if (this.level().isClientSide) {
            this.tickRotation();
        }
        EntityMovementExtension extension = (EntityMovementExtension) this;
        extension.sable$setTrackingSubLevel(null);
    }

    protected void tickRotation() {
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        this.zRotO = this.zRot;
        Vec3 motion = this.getDeltaMovement();
        float zSpeed = (float) Mth.clamp(motion.z, -0.25, 0.25) / 0.25f;
        this.setXRot(this.getXRot() + zSpeed * 30);
        float horizontalSpeed = (float) Mth.clamp(motion.horizontalDistance() - 0.075, 0, 0.15) / 0.15f;
        float motionYaw = (float) Math.toDegrees(Mth.atan2(motion.z, motion.x)) - 90f;
        float deltaRotY = Mth.wrapDegrees((motionYaw - this.prevMotionYaw) * 5);
        this.setYRot(this.getYRot() + deltaRotY * horizontalSpeed * 0.15f);
        float xSpeed = (float) Mth.clamp(motion.x, -0.25, 0.25) / 0.25f;
        this.zRot = this.zRot + xSpeed * 30;
        this.setXRot(this.getXRot() * 0.5f);
        this.setYRot(Mth.wrapDegrees(this.getYRot()));
        this.zRot = this.zRot * 0.5f;
        this.prevMotionYaw = motionYaw;
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
                this.kill();
                return true;
            }
        }
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(COLOR, (byte) DyeColor.WHITE.getId());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.leashData = this.readLeashData(tag);
        this.getEntityData().set(COLOR, tag.getByte("Color"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
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
