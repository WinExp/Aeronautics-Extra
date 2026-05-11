package com.github.winexp.aeronauticsextra.content.blocks.gps.receiver;

import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class GPSReceiverBlock extends BaseEntityBlock implements IBE<GPSReceiverBlockEntity> {
    public static final MapCodec<GPSReceiverBlock> CODEC = simpleCodec(GPSReceiverBlock::new);

    public GPSReceiverBlock(Properties properties) {
        super(properties);
    }

    public static Vec3 getAntennaTopPos(Level level, BlockState blockState, BlockPos blockPos) {
        return blockPos.getCenter().add(0, 0.5, 0);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public Class<GPSReceiverBlockEntity> getBlockEntityClass() {
        return GPSReceiverBlockEntity.class;
    }

    @Override
    public BlockEntityType<GPSReceiverBlockEntity> getBlockEntityType() {
        return AeroExtraBlockEntityTypes.GPS_RECEIVER.get();
    }
}
