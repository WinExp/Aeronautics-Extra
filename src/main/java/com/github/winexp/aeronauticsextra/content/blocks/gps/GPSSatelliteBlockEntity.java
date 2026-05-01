package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.AeroExtraMenuTypes;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSSatelliteBlockEntity extends SmartBlockEntity implements MenuProvider, Clearable {
    public final GPSSatelliteInventory inventory = new GPSSatelliteInventory(this);
    private Vec3 position = Vec3.ZERO;

    private float coreScale = 0;
    public final LerpedFloat coreScaler = LerpedFloat.linear();
    private float coreAngle = 0;
    public final LerpedFloat coreRotation = LerpedFloat.angular();

    public GPSSatelliteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
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

    @Override
    public void destroy() {
        ItemHelper.dropContents(this.level, this.getBlockPos(), this.inventory);
        super.destroy();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
        this.notifyUpdate();
    }

    public ItemStack getCore() {
        return this.inventory.getItem(0);
    }

    @Override
    public void initialize() {
        super.onLoad();
        GPSManager.registerSatellite(this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        GPSManager.unregisterSatellite(this);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.position = new Vec3(tag.getDouble("position_x"), tag.getDouble("position_y"), tag.getDouble("position_z"));
        this.inventory.setItem(0, ItemStack.parseOptional(registries, tag.getCompound("core")));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putDouble("position_x", this.position.x);
        tag.putDouble("position_y", this.position.y);
        tag.putDouble("position_z", this.position.z);
        tag.put("core", this.getCore().saveOptional(registries));
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.aeronauticsextra.gps_satellite");
    }

    @Override
    public ConfigMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ConfigMenu(AeroExtraMenuTypes.GPS_SATELLITE_CONFIG.get(), containerId, inventory, this);
    }

    @Override
    public void clearContent() {
        this.inventory.clearContent();
    }
}
