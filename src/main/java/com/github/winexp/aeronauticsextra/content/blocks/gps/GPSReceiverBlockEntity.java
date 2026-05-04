package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSManager;
import com.github.winexp.aeronauticsextra.content.logistics.gps.GPSRequest;
import com.github.winexp.aeronauticsextra.content.logistics.gps.TrilaterationResolver;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GPSReceiverBlockEntity extends SmartBlockEntity {
    private Vec3 currentPos;

    public GPSReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(AeroExtraBlockEntityTypes.GPS_RECEIVER.get(), pos, state);
        this.setLazyTickRate(5);
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

        if (!this.level.isClientSide) {
            GPSManager.request(new GPSRequest(this.level, this.getBlockPos().getCenter(), responses -> {
                TrilaterationResolver.LocateResult result = TrilaterationResolver.locate(responses);
                if (!result.isEmpty()) {
                    this.currentPos = result.position();
                }
            }));
        }
    }
}
