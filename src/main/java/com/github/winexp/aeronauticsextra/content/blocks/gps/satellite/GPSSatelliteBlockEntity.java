package com.github.winexp.aeronauticsextra.content.blocks.gps.satellite;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSBroadcast;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ConfigMenu;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraDataComponents;
import com.github.winexp.aeronauticsextra.registry.AeroExtraItemTags;
import com.github.winexp.aeronauticsextra.utility.ShapeUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import dev.ryanhcode.sable.Sable;
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
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class GPSSatelliteBlockEntity extends SmartBlockEntity implements MenuProvider, Clearable {
    private final GPSSatelliteInventory inventory = new GPSSatelliteInventory(this);
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
            if (!this.getAntenna().isEmpty()) {
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
        Integer broadcastInterval = this.getAntenna().get(AeroExtraDataComponents.GPS_BROADCAST_INTERVAL);
        this.setLazyTickRate(broadcastInterval == null ? 0 : broadcastInterval - 1);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.level.isClientSide) {
            if (this.getAntenna().isEmpty()) return;
            Float signalStrength = this.getAntenna().get(AeroExtraDataComponents.GPS_BROADCAST_STRENGTH);
            if (signalStrength == null) signalStrength = 1.0f;
            BlockPos blockPos = this.getBlockPos();
            BlockState state = this.level.getBlockState(blockPos);
            Vec3 pos = Sable.HELPER.projectOutOfSubLevel(this.level, blockPos.getCenter());
            VoxelShape antennaShape = ShapeUtil.setOriginAsCenter(GPSSatelliteBlock.getAntennaShape(state));
            GPSManager.broadcast(new GPSBroadcast(this.level, this.virtualPos, pos, antennaShape, signalStrength, 256));
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

    public Vec3 getVirtualPos() {
        return this.virtualPos;
    }

    public void setVirtualPos(Vec3 virtualPos) {
        this.virtualPos = virtualPos;
        this.notifyUpdate();
    }

    public ItemStack getAntenna() {
        return this.inventory.getItem(0);
    }

    public void setAntenna(ItemStack stack) {
        if (stack.isEmpty() || (stack.is(AeroExtraItemTags.ANTENNA) && this.inventory.getSlotLimit(0) >= stack.getCount())) {
            this.inventory.setItem(0, stack);
            this.notifyUpdate();
        }
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        CompoundTag virtualPosTag = tag.getCompound("virtual_pos");
        if (!virtualPosTag.isEmpty()) {
            this.virtualPos = new Vec3(virtualPosTag.getDouble("x"), virtualPosTag.getDouble("y"), virtualPosTag.getDouble("z"));
        }
        this.inventory.setItem(0, ItemStack.parseOptional(registries, tag.getCompound("antenna")));
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        CompoundTag virtualPosTag = new CompoundTag();
        virtualPosTag.putDouble("x", this.virtualPos.x);
        virtualPosTag.putDouble("y", this.virtualPos.y);
        virtualPosTag.putDouble("z", this.virtualPos.z);
        tag.put("virtual_pos", virtualPosTag);
        tag.put("antenna", this.getAntenna().saveOptional(registries));
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
