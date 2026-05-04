package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.content.logistics.gps.*;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class GPSReceiverBlockEntity extends SmartBlockEntity {
    private Vec3 currentPos;
    private final ArrayList<SampleData> sampleDataList =  new ArrayList<>();
    private boolean isSampling = false;

    public GPSReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(AeroExtraBlockEntityTypes.GPS_RECEIVER.get(), pos, state);
        this.setLazyTickRate(4);
    }

    public Vec3 getCurrentPos() {
        return this.currentPos;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if (!this.level.isClientSide && !this.isSampling) {
            GPSBroadcastReceiver receiver = new GPSBroadcastReceiver(this.level, this.getBlockPos().getCenter(), 0.07f, this::receiveBroadcast, this::onSamplingComplete, 6);
            GPSManager.registerReceiver(receiver);
            this.isSampling = true;
        }
    }

    private void receiveBroadcast(SampleData sampleData) {
        this.sampleDataList.add(sampleData);
    }

    private void onSamplingComplete() {
        TrilaterationResolver.LocateResult result = TrilaterationResolver.locate(this.sampleDataList);
        if (!result.isEmpty()) {
            this.currentPos = result.position();
        }
        this.sampleDataList.clear();
        this.isSampling = false;
    }
}
