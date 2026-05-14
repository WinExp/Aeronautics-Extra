package com.github.winexp.aeronauticsextra.content.blocks.kinetics;

import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class CVTGearshiftBlock extends BaseEntityBlock implements IBE<CVTGearshiftBlockEntity> {
    public static final MapCodec<CVTGearshiftBlock> CODEC = simpleCodec(CVTGearshiftBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public CVTGearshiftBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<CVTGearshiftBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        builder.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(AXIS, context.getNearestLookingDirection().getAxis());
    }

    @Override
    public Class<CVTGearshiftBlockEntity> getBlockEntityClass() {
        return CVTGearshiftBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CVTGearshiftBlockEntity> getBlockEntityType() {
        return AeroExtraBlockEntityTypes.CVT_GEARSHIFT.get();
    }
}
