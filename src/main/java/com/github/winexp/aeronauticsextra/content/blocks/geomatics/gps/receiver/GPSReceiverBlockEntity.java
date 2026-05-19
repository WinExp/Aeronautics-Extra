package com.github.winexp.aeronauticsextra.content.blocks.geomatics.gps.receiver;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSBroadcastReceiver;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSSampleData;
import com.github.winexp.aeronauticsextra.content.logistics.gps.TrilaterationResolver;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ReceiverConfigMenu;
import com.github.winexp.aeronauticsextra.data.AeroExtraLang;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.ryanhcode.sable.Sable;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.IntegerRange;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GPSReceiverBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation, MenuProvider {
    public static final IntegerRange MAX_DISTANCE_RANGE = IntegerRange.of(-2048, 2048);
    private static final Component SCROLL_OPTION_TITLE = AeroExtraLang.translate("scroll_option.sampling_time").component();

    private int satelliteCount = 0;
    private int sampleCount = 0;
    @Nullable
    private Vec3 currentPos;
    private Vec3 targetPos = Vec3.ZERO;
    private int maxDistance = 64;

    private final Map<Direction, Integer> signalMap = new EnumMap<>(Direction.class) {
        {
            for (Direction direction : Direction.values()) {
                put(direction, 0);
            }
        }
    };
    private boolean updated = false;
    private final ArrayList<GPSSampleData> sampleDataList =  new ArrayList<>();
    private int sendDataCounter;

    private ScrollValueBehaviour scrollValueBehaviour;

    public GPSReceiverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int getSignal(Direction direction) {
        return this.signalMap.get(direction);
    }

    public Vec3 getTargetPos() {
        return this.targetPos;
    }

    public void setTargetPos(Vec3 targetPos) {
        this.targetPos = targetPos;
        this.notifyUpdate();
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = MAX_DISTANCE_RANGE.fit(maxDistance);
        this.notifyUpdate();
    }

    @Nullable
    public Vec3 getCurrentPos() {
        return this.currentPos;
    }

    private void setCurrentPos(Vec3 currentPos) {
        this.currentPos = currentPos;
        this.notifyUpdate();
    }

    public boolean isUpdated() {
        boolean result = this.updated;
        this.updated = false;
        return result;
    }

    @Override
    public void notifyUpdate() {
        super.notifyUpdate();
        this.updateSignal(this.getBlockState());
    }

    public void updateSignal(BlockState state) {
        if (this.currentPos == null) return;

        Direction front = state.getValue(GPSReceiverBlock.FACING);
        Vec3 targetPos = this.targetPos;
        Vec3 currentPos = this.currentPos;
        double dx = targetPos.x - currentPos.x;
        double dy = targetPos.y - currentPos.y;
        double dz = targetPos.z - currentPos.z;
        int signalX = (int) Math.floor(dx / this.maxDistance * 15);
        int signalY = (int) Math.floor(dy / this.maxDistance * 15);
        int signalZ = (int) Math.floor(dz / this.maxDistance * 15);
        if (signalX > 0) {
            this.signalMap.put(front.getCounterClockWise(), 0);
            this.signalMap.put(front.getClockWise(), signalX);
        } else {
            this.signalMap.put(front.getCounterClockWise(), -signalX);
            this.signalMap.put(front.getClockWise(), 0);
        }
        if (signalY > 0) {
            this.signalMap.put(Direction.DOWN, 0);
            this.signalMap.put(Direction.UP, signalY);
        } else {
            this.signalMap.put(Direction.DOWN, -signalY);
            this.signalMap.put(Direction.UP, 0);
        }
        if (signalZ > 0) {
            this.signalMap.put(front, 0);
            this.signalMap.put(front.getOpposite(), signalZ);
        } else {
            this.signalMap.put(front, -signalZ);
            this.signalMap.put(front.getOpposite(), 0);
        }

        Level level = this.getLevel();
        level.setBlockAndUpdate(this.getBlockPos(), state);
        level.updateNeighborsAt(this.getBlockPos(), state.getBlock());
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.scrollValueBehaviour = new GPSReceiverValueBehaviour(SCROLL_OPTION_TITLE, this, new GPSReceiverValueBoxTransform());
        behaviours.add(this.scrollValueBehaviour);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        CompoundTag targetPosTag = tag.getCompound("target_pos");
        this.targetPos = new Vec3(
                targetPosTag.getDouble("x"),
                targetPosTag.getDouble("y"),
                targetPosTag.getDouble("z")
        );
        this.maxDistance = tag.getInt("max_distance");
        CompoundTag currentPosTag = tag.getCompound("current_pos");
        if (!currentPosTag.isEmpty()) {
            this.currentPos = new Vec3(
                    currentPosTag.getDouble("x"),
                    currentPosTag.getDouble("y"),
                    currentPosTag.getDouble("z")
            );
        }
        if (clientPacket) {
            this.satelliteCount = tag.getInt("satellite_count");
            this.sampleCount = tag.getInt("sample_count");
        }
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        CompoundTag targetPosTag = new CompoundTag();
        targetPosTag.putDouble("x", this.targetPos.x);
        targetPosTag.putDouble("y", this.targetPos.y);
        targetPosTag.putDouble("z", this.targetPos.z);
        tag.put("target_pos", targetPosTag);
        tag.putInt("max_distance", this.maxDistance);
        if (this.currentPos != null) {
            CompoundTag currentPosTag = new CompoundTag();
            currentPosTag.putDouble("x", this.currentPos.x);
            currentPosTag.putDouble("y", this.currentPos.y);
            currentPosTag.putDouble("z", this.currentPos.z);
            tag.put("current_pos", currentPosTag);
        }
        if (clientPacket) {
            tag.putInt("satellite_count", this.satelliteCount);
            tag.putInt("sample_count", this.sampleCount);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (--this.sendDataCounter <= 0) {
            this.sendData();
            this.sendDataCounter = 20;
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.level.isClientSide) {
            BlockPos blockPos = this.getBlockPos();
            BlockState blockState = this.level.getBlockState(blockPos);
            Vec3 pos = Sable.HELPER.projectOutOfSubLevel(this.level, blockPos.getCenter());
            Vec3 antennaPos = Sable.HELPER.projectOutOfSubLevel(this.level, GPSReceiverBlock.getAntennaTopPos(this.level, blockState, blockPos));
            GPSBroadcastReceiver receiver = new GPSBroadcastReceiver(this.level, pos, antennaPos, 0.07f, this::receiveBroadcast, this::onSamplingComplete, this.scrollValueBehaviour.value);
            GPSManager.registerReceiver(receiver);
        }
    }

    private void receiveBroadcast(GPSSampleData sampleData) {
        this.sampleDataList.add(sampleData);
    }

    private void onSamplingComplete() {
        if (this.isRemoved()) return;
        TrilaterationResolver.LocateResult result = TrilaterationResolver.locate(this.sampleDataList);
        if (!result.isEmpty()) {
            this.setCurrentPos(result.position());
            this.updated = true;
        }
        HashSet<UUID> uuids = new HashSet<>();
        for (GPSSampleData data : this.sampleDataList) {
            uuids.add(data.satelliteUUID());
        }
        this.satelliteCount = uuids.size();
        this.sampleCount = this.sampleDataList.size();
        this.sampleDataList.clear();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.aeronauticsextra.gps_receiver");
    }

    @Override
    public ReceiverConfigMenu createMenu(int containerId, Inventory inventory, Player player) {
        return ReceiverConfigMenu.create(containerId, inventory, this);
    }

    private static class GPSReceiverValueBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 16);
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        AeroExtraLang.translate("gui.gps_receiver.info_header").forGoggles(tooltip);
        ChatFormatting satelliteCountColor;
        if (this.satelliteCount < 4) satelliteCountColor = ChatFormatting.RED;
        else if (this.satelliteCount < 8) satelliteCountColor = ChatFormatting.YELLOW;
        else satelliteCountColor = ChatFormatting.GREEN;

        ChatFormatting sampleCountColor;
        if (this.sampleCount < 4) sampleCountColor = ChatFormatting.RED;
        else sampleCountColor = ChatFormatting.GREEN;
        AeroExtraLang.translate("gui.gps_receiver.satellite_count")
                .style(ChatFormatting.GRAY)
                .add(AeroExtraLang.number(this.satelliteCount).style(satelliteCountColor)).forGoggles(tooltip);
        AeroExtraLang.translate("gui.gps_receiver.sample_count")
                .style(ChatFormatting.GRAY)
                .add(AeroExtraLang.number(this.sampleCount).style(sampleCountColor)).forGoggles(tooltip);
        return true;
    }
}
