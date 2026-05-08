package com.github.winexp.aeronauticsextra.content.blocks.gps.receiver;

import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GPSReceiverBlock extends BaseEntityBlock implements IBE<GPSReceiverBlockEntity> {
    public static final MapCodec<GPSReceiverBlock> CODEC = simpleCodec(GPSReceiverBlock::new);

    public GPSReceiverBlock(Properties properties) {
        super(properties);
    }

    public static VoxelShape getAntennaShape(BlockState state) {
        if (!state.is(AeroExtraBlocks.GPS_RECEIVER.get())) return Shapes.empty();
        return Shapes.block();
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
