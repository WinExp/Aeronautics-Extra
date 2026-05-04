package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSBroadcast;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraDataComponents;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ConfigMenu;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Clearable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSSatelliteBlockEntity extends SmartBlockEntity implements MenuProvider, Clearable {
    public final GPSSatelliteInventory inventory = new GPSSatelliteInventory(this);
    private Vec3 virtualPos = Vec3.ZERO;

    private float coreScale = 0;
    public final LerpedFloat coreScaler = LerpedFloat.linear();
    private float coreAngle = 0;
    public final LerpedFloat coreRotation = LerpedFloat.angular();

    public GPSSatelliteBlockEntity(BlockPos pos, BlockState blockState) {
        super(AeroExtraBlockEntityTypes.GPS_SATELLITE.get(), pos, blockState);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (!this.getCore().isEmpty()) {
                this.coreScale = Math.min(1, this.coreScale + .15f);
            } else {
                this.coreScale = Math.max(0, this.coreScale - .15f);
            }
            this.coreAngle = (this.coreAngle + 4) % 360;
            this.coreScaler.chase(this.coreScale, .2f, LerpedFloat.Chaser.EXP);
            this.coreScaler.tickChaser();
            this.coreRotation.chase(this.coreAngle, .2f, LerpedFloat.Chaser.EXP);
            this.coreRotation.tickChaser();
        }
    }

    public void updateLazyTickRate() {
        Integer broadcastInterval = this.getCore().get(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL);
        this.setLazyTickRate(broadcastInterval == null ? 0 : broadcastInterval - 1);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        GPSManager.broadcast(new GPSBroadcast(this.level, this.virtualPos, this.getBlockPos().getCenter(), 128));
    }

    @Override
    public void destroy() {
        ItemHelper.dropContents(this.level, this.getBlockPos(), this.inventory);
        super.destroy();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public Vec3 getVirtualPos() {
        return this.virtualPos;
    }

    public void setVirtualPos(Vec3 virtualPos) {
        this.virtualPos = virtualPos;
        this.notifyUpdate();
    }

    public ItemStack getCore() {
        return this.inventory.getItem(0);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.virtualPos = new Vec3(tag.getDouble("position_x"), tag.getDouble("position_y"), tag.getDouble("position_z"));
        this.inventory.setItem(0, ItemStack.parseOptional(registries, tag.getCompound("core")));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putDouble("position_x", this.virtualPos.x);
        tag.putDouble("position_y", this.virtualPos.y);
        tag.putDouble("position_z", this.virtualPos.z);
        tag.put("core", this.getCore().saveOptional(registries));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.aeronauticsextra.gps_satellite");
    }

    @Override
    public ConfigMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ConfigMenu(containerId, inventory, this);
    }

    @Override
    public void clearContent() {
        this.inventory.clearContent();
    }
}
