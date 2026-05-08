package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSBroadcastReceiver;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSSampleData;
import com.github.winexp.aeronauticsextra.content.logistics.gps.TrilaterationResolver;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.ryanhcode.sable.Sable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class GPSReceiverBlockEntity extends SmartBlockEntity {
    private Vec3 currentPos;
    private final ArrayList<GPSSampleData> sampleDataList =  new ArrayList<>();
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
            Vec3 pos = Sable.HELPER.projectOutOfSubLevel(level, this.getBlockPos().getCenter());
            GPSBroadcastReceiver receiver = new GPSBroadcastReceiver(this.level, pos, 0.07f, this::receiveBroadcast, this::onSamplingComplete, 6);
            GPSManager.registerReceiver(receiver);
            this.isSampling = true;
        }
    }

    private void receiveBroadcast(GPSSampleData sampleData) {
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
