package com.github.winexp.aeronauticsextra.content.blocks.geomatics.gps.receiver;

import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

public class GPSReceiverBlock extends BaseEntityBlock implements IBE<GPSReceiverBlockEntity>, IWrenchable {
    public static final MapCodec<GPSReceiverBlock> CODEC = simpleCodec(GPSReceiverBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public GPSReceiverBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public static Vec3 getAntennaTopPos(Level level, BlockState blockState, BlockPos blockPos) {
        return blockPos.getCenter().add(0, 0.5, 0);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        GPSReceiverBlockEntity be = this.getBlockEntity(level, pos);
        return be.getSignal(direction.getOpposite());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        this.withBlockEntityDo(context.getLevel(), context.getClickedPos(), be -> be.updateSignal(newState));
        return IWrenchable.super.updateAfterWrenched(newState, context);
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

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH)) return InteractionResult.PASS;

        return this.onBlockEntityUse(level, pos, be -> {
            if (!level.isClientSide) {
                player.openMenu(this.getMenuProvider(state, level, pos), be::sendToMenu);
            }

            return InteractionResult.SUCCESS;
        });
    }
}
