package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.AeroExtraMenuTypes;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ConfigMenu;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSSatelliteBlockEntity extends SmartBlockEntity implements MenuProvider {
    private Vec3 location = Vec3.ZERO;

    public GPSSatelliteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    public Vec3 getLocation() {
        return this.location;
    }

    public void setLocation(Vec3 location) {
        this.location = location;
        this.notifyUpdate();
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
        this.location = new Vec3(tag.getDouble("location_x"), tag.getDouble("location_y"), tag.getDouble("location_z"));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putDouble("location_x", this.location.x);
        tag.putDouble("location_y", this.location.y);
        tag.putDouble("location_z", this.location.z);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.aeronauticsextra.gps_satellite");
    }

    @Override
    public ConfigMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ConfigMenu(AeroExtraMenuTypes.GPS_SATELLITE_CONFIG.get(), containerId, inventory, this);
    }
}
