package com.github.winexp.aeronauticsextra.content.blocks.kinetics;

import com.github.winexp.aeronauticsextra.mixin_interface.create.CogwheelPlacementExtensionExt;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.foundation.block.IBE;
import dev.simulated_team.simulated.util.extra_kinetics.ExtraKinetics;
import dev.simulated_team.simulated.util.placement_helpers.CogwheelPlacementExtension;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CVTGearshiftBlock extends DirectionalAxisKineticBlock implements IBE<CVTGearshiftBlockEntity>, ExtraKinetics.ExtraKineticsBlock {
    public static final int placementHelperId = PlacementHelpers.register(createPlacementHelper());
    public static final MapCodec<CVTGearshiftBlock> CODEC = simpleCodec(CVTGearshiftBlock::new);
    public static final BooleanProperty LEFT_POWERED = BooleanProperty.create("left_powered");
    public static final BooleanProperty RIGHT_POWERED = BooleanProperty.create("right_powered");

    public CVTGearshiftBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(LEFT_POWERED, false)
                .setValue(RIGHT_POWERED, false));
    }

    private static IPlacementHelper createPlacementHelper() {
        CogwheelPlacementExtension helper = new CogwheelPlacementExtension(stack -> stack.getItem() instanceof CogwheelBlockItem, AeroExtraBlocks.CVT_GEARSHIFT::has);
        ((CogwheelPlacementExtensionExt) helper).aero_extra$setAxisProvider((state) -> AeroExtraBlocks.CVT_GEARSHIFT.get().getRotationAxis(state));
        return helper;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        final ItemStack heldItem = player.getItemInHand(interactionHand);

        final IPlacementHelper helper = PlacementHelpers.get(placementHelperId);
        if (helper.matchesItem(heldItem)) {
            return helper
                    .getOffset(player, level, state, blockPos, blockHitResult)
                    .placeInWorld(level, (BlockItem) heldItem.getItem(), player, interactionHand, blockHitResult);
        }

        return super.useItemOn(stack, state, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected MapCodec<CVTGearshiftBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        return super.updateAfterWrenched(this.getPoweredState(context.getLevel(), newState, context.getClickedPos()), context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEFT_POWERED);
        builder.add(RIGHT_POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction lookingDirection = context.getNearestLookingDirection();
        boolean shiftKeyDown = context.getPlayer().isShiftKeyDown();

        Direction.Axis preferredAxis = RotatedPillarKineticBlock.getPreferredAxis(context);
        Direction darkDirection;
        boolean axisAlongFirst = false;

        if (preferredAxis != null && preferredAxis != lookingDirection.getAxis() && !shiftKeyDown) {
            darkDirection = lookingDirection;
            if (preferredAxis == Direction.Axis.X) {
                axisAlongFirst = true;
            } else if (preferredAxis == Direction.Axis.Y && lookingDirection.getAxis() == Direction.Axis.X) {
                axisAlongFirst = true;
            }
        } else {
            if (lookingDirection.getAxis().isHorizontal()) {
                darkDirection = lookingDirection.getCounterClockWise();
                if (lookingDirection.getAxis() == Direction.Axis.X) {
                    axisAlongFirst = true;
                }
            } else {
                darkDirection = context.getHorizontalDirection().getCounterClockWise();
                if (context.getHorizontalDirection().getAxis() == Direction.Axis.Z) {
                    axisAlongFirst = true;
                }
            }
        }

        if (shiftKeyDown) {
            darkDirection = darkDirection.getOpposite();
        }

        BlockState state = this.defaultBlockState()
                .setValue(FACING, darkDirection)
                .setValue(AXIS_ALONG_FIRST_COORDINATE, axisAlongFirst);

        return this.getPoweredState(context.getLevel(), state, context.getClickedPos());
    }

    public BlockState getPoweredState(Level level, BlockState state, BlockPos pos) {
        Direction left = getLeftDirection(state);
        Direction right = getRightDirection(state);

        return state.setValue(LEFT_POWERED, level.hasSignal(pos.relative(left), left))
                .setValue(RIGHT_POWERED, level.hasSignal(pos.relative(right), right));
    }

    public static Direction getLeftDirection(BlockState state) {
        return state.getValue(FACING);
    }

    public static Direction getRightDirection(BlockState state) {
        return getLeftDirection(state).getOpposite();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide)
            return;

        boolean previouslyLeftPowered = state.getValue(LEFT_POWERED);
        boolean previouslyRightPowered = state.getValue(RIGHT_POWERED);

        BlockState newState = this.getPoweredState(level, state, pos);

        if (previouslyLeftPowered != newState.getValue(LEFT_POWERED) || previouslyRightPowered != newState.getValue(RIGHT_POWERED)) {
            level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public Class<CVTGearshiftBlockEntity> getBlockEntityClass() {
        return CVTGearshiftBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CVTGearshiftBlockEntity> getBlockEntityType() {
        return AeroExtraBlockEntityTypes.CVT_GEARSHIFT.get();
    }

    @Override
    public IRotate getExtraKineticsRotationConfiguration() {
        return CVTGearshiftBlockEntity.CVTGearshiftCogwheel.EXTRA_COGWHEEL_CONFIG;
    }
}
