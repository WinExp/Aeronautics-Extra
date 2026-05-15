package com.github.winexp.aeronauticsextra.content.blocks.gps.receiver;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSBroadcastReceiver;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSSampleData;
import com.github.winexp.aeronauticsextra.content.logistics.gps.TrilaterationResolver;
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
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GPSReceiverBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
    private static final Component SCROLL_OPTION_TITLE = AeroExtraLang.translate("scroll_option.sampling_time").component();
    private static final String VALUE_FORMAT = "%s t";

    private int satelliteCount = 0;
    private int sampleCount = 0;
    private boolean updated = false;
    @Nullable
    private Vec3 currentPos;
    private final ArrayList<GPSSampleData> sampleDataList =  new ArrayList<>();
    private int sendDataCounter;

    private ScrollValueBehaviour samplingTimeBehaviour;

    public GPSReceiverBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    public Vec3 getCurrentPos() {
        return this.currentPos;
    }

    private void setCurrentPos(Vec3 currentPos) {
        this.currentPos = currentPos;
        this.setChanged();
    }

    public boolean isUpdated() {
        boolean result = this.updated;
        this.updated = false;
        return result;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        this.samplingTimeBehaviour = new GPSReceiverValueBehaviour(SCROLL_OPTION_TITLE, this,
                new GPSReceiverValueBoxTransform())
                .withFormatter(VALUE_FORMAT::formatted);
        behaviours.add(this.samplingTimeBehaviour);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        CompoundTag currentPosTag = tag.getCompound("current_pos");
        if (!currentPosTag.isEmpty()) {
            double x = currentPosTag.getDouble("x");
            double y = currentPosTag.getDouble("y");
            double z = currentPosTag.getDouble("z");
            this.currentPos = new Vec3(x, y, z);
        }
        if (clientPacket) {
            this.satelliteCount = tag.getInt("satellite_count");
            this.sampleCount = tag.getInt("sample_count");
        }
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
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
            GPSBroadcastReceiver receiver = new GPSBroadcastReceiver(this.level, pos, antennaPos, 0.07f, this::receiveBroadcast, this::onSamplingComplete, this.samplingTimeBehaviour.value);
            GPSManager.registerReceiver(receiver);
        }
    }

    private void receiveBroadcast(GPSSampleData sampleData) {
        this.sampleDataList.add(sampleData);
    }

    private void onSamplingComplete() {
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
