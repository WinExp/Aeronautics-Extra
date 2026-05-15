package com.github.winexp.aeronauticsextra.content.blocks.gps.satellite;

import com.github.winexp.aeronauticsextra.client.block.renderer.BlockOutlineRenderer;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import com.github.winexp.aeronauticsextra.registry.AeroExtraTags;
import com.github.winexp.aeronauticsextra.utility.ShapeUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;

import java.util.Map;

public class GPSSatelliteBlock extends BaseEntityBlock implements IBE<GPSSatelliteBlockEntity>, IWrenchable, BlockOutlineRenderer {
    public static final MapCodec<GPSSatelliteBlock> CODEC = simpleCodec(GPSSatelliteBlock::new);
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.VERTICAL);
    public static final BooleanProperty ANTENNA = BooleanProperty.create("antenna");
    public static final Map<Direction, VoxelShape> BASE_SHAPES = Map.of(
            Direction.UP, Block.box(0, 0, 0, 16, 4, 16),
            Direction.DOWN, Block.box(0, 12, 0, 16, 16, 16)
    );
    public static final Map<Direction, VoxelShape> ANTENNA_SHAPES = Map.of(
            Direction.UP, Block.box(7, 4, 7, 9, 16, 9),
            Direction.DOWN, Block.box(7, 0, 7, 9, 12, 9)
    );

    public GPSSatelliteBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP).setValue(ANTENNA, false));
    }

    @Override
    public boolean renderHighlight(PoseStack poseStack, VertexConsumer vertexConsumer, Camera camera, Level level, BlockState blockState, BlockPos blockPos, Vec3 hitPos) {
        if (camera.getEntity() instanceof Player player && player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH)) {
            Vec3 pos = BlockOutlineRenderer.getRenderPos(blockPos, camera.getPosition());
            if (!isOnAntenna(hitPos, blockPos, blockState)) {
                LevelRenderer.renderShape(poseStack, vertexConsumer, getBaseShape(blockState), pos.x, pos.y, pos.z, 0, 0, 0, 0.4f);
            } else {
                LevelRenderer.renderShape(poseStack, vertexConsumer, getAntennaShape(blockState), pos.x, pos.y, pos.z, 0, 0, 0, 0.4f);
            }
            return true;
        }
        return false;
    }

    public static boolean isOnAntenna(Vec3 pos, BlockPos blockPos, BlockState blockState) {
        if (!blockState.is(AeroExtraBlocks.GPS_SATELLITE) || !blockState.getValue(ANTENNA)) return false;
        for (AABB aabb : ANTENNA_SHAPES.get(blockState.getValue(FACING)).toAabbs()) {
            if (aabb.move(blockPos).intersects(AABB.ofSize(pos, 1e-7, 1e-7, 1e-7))) return true;
        }
        return false;
    }

    public static VoxelShape getBaseShape(BlockState state) {
        if (!state.is(AeroExtraBlocks.GPS_SATELLITE)) return Shapes.empty();
        return BASE_SHAPES.get(state.getValue(FACING));
    }

    public static VoxelShape getAntennaShape(BlockState state) {
        if (!state.is(AeroExtraBlocks.GPS_SATELLITE)) return Shapes.empty();
        return ANTENNA_SHAPES.get(state.getValue(FACING));
    }

    public static Vec3 getAntennaTopPos(Level level, BlockState state, BlockPos blockPos) {
        if (!state.is(AeroExtraBlocks.GPS_SATELLITE)) return null;
        Direction direction = state.getValue(FACING);
        double y;
        if (direction == Direction.UP) {
            y = state.getShape(level, blockPos).move(blockPos.getX(), blockPos.getY(), blockPos.getZ()).max(Direction.Axis.Y);
        } else {
            y = state.getShape(level, blockPos).move(blockPos.getX(), blockPos.getY(), blockPos.getZ()).min(Direction.Axis.Y);
        }
        return blockPos.getCenter().with(Direction.Axis.Y, y);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        if (targetedFace.getAxis() == Direction.Axis.Y) return originalState;
        else return originalState.setValue(FACING, originalState.getValue(FACING).getOpposite());
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        BlockPos blockPos = context.getClickedPos();
        Vec3 clickPos = context.getClickLocation();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if (state.getValue(ANTENNA)) {
            if (isOnAntenna(clickPos, blockPos, state)) {
                this.withBlockEntityDo(level, blockPos, be -> {
                    if (!be.getAntenna().isEmpty()) {
                        if (!player.isCreative()) {
                            player.getInventory().placeItemBackInInventory(be.getAntenna().copyWithCount(1));
                        }
                        be.getAntenna().shrink(1);
                    }
                });
                level.setBlock(blockPos, state.setValue(ANTENNA, false), Block.UPDATE_ALL);
                IWrenchable.playRemoveSound(level, blockPos);
                return InteractionResult.SUCCESS;
            }
        }
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isShiftKeyDown() || !player.mayBuild() || !stack.is(AeroExtraTags.ItemTags.ANTENNAS.tag)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (state.getValue(ANTENNA)) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        if (ShapeUtil.isIntersect(player.getBoundingBox(), getAntennaShape(state), pos)) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        level.setBlock(pos, state.setValue(ANTENNA, true), Block.UPDATE_ALL);
        this.withBlockEntityDo(level, pos, be -> be.setAntenna(stack.copyWithCount(1)));
        stack.consume(1, player);
        level.playSound(null, pos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = getBaseShape(state);
        if (state.getValue(ANTENNA)) {
            return Shapes.or(shape, getAntennaShape(state));
        }
        return shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getNearestLookingVerticalDirection().getOpposite();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(ANTENNA);
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, level, pos, newState);
    }

    @Override
    protected MapCodec<GPSSatelliteBlock> codec() {
        return CODEC;
    }

    @Override
    public Class<GPSSatelliteBlockEntity> getBlockEntityClass() {
        return GPSSatelliteBlockEntity.class;
    }

    @Override
    public BlockEntityType<GPSSatelliteBlockEntity> getBlockEntityType() {
        return AeroExtraBlockEntityTypes.GPS_SATELLITE.get();
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
