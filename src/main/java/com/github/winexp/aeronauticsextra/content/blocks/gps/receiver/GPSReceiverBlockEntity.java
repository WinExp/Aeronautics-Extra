package com.github.winexp.aeronauticsextra.content.blocks.gps.receiver;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSBroadcastReceiver;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSSampleData;
import com.github.winexp.aeronauticsextra.content.logistics.gps.TrilaterationResolver;
import com.github.winexp.aeronauticsextra.data.AeroExtraLang;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.ryanhcode.sable.Sable;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GPSReceiverBlockEntity extends SmartBlockEntity {
    private static final Component SCROLL_OPTION_TITLE = AeroExtraLang.translate("scroll_option.sampling_time").component();
    private static final String VALUE_FORMAT = "%s t";

    @Nullable
    private Vec3 currentPos;
    private final ArrayList<GPSSampleData> sampleDataList =  new ArrayList<>();

    private ScrollValueBehaviour samplingTimeBehaviour;

    public GPSReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(AeroExtraBlockEntityTypes.GPS_RECEIVER.get(), pos, state);
    }

    @Nullable
    public Vec3 getCurrentPos() {
        return this.currentPos;
    }

    private void setCurrentPos(Vec3 currentPos) {
        this.currentPos = currentPos;
        this.notifyUpdate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.setLazyTickRate(this.samplingTimeBehaviour.value - 1);
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
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.level.isClientSide) {
            Vec3 pos = Sable.HELPER.projectOutOfSubLevel(level, this.getBlockPos().getCenter());
            GPSBroadcastReceiver receiver = new GPSBroadcastReceiver(this.level, pos, 0.07f, this::receiveBroadcast, this::onSamplingComplete, this.samplingTimeBehaviour.value);
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
        }
        this.sampleDataList.clear();
    }

    private static class GPSReceiverValueBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 16);
        }
    }
}
