package com.github.winexp.aeronauticsextra.content.blocks.kinetics;

import com.github.winexp.aeronauticsextra.registry.AeroExtraBlockEntityTypes;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class CVTGearshiftBlock extends RotatedPillarKineticBlock implements IBE<CVTGearshiftBlockEntity>, ExtraKinetics.ExtraKineticsBlock {
    public static final int placementHelperId = PlacementHelpers.register(new CogwheelPlacementExtension(stack -> stack.getItem() instanceof CogwheelBlockItem, AeroExtraBlocks.CVT_GEARSHIFT::has));
    public static final MapCodec<CVTGearshiftBlock> CODEC = simpleCodec(CVTGearshiftBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CVTGearshiftBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
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
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public IRotate getExtraKineticsRotationConfiguration() {
        return CVTGearshiftBlockEntity.CVTGearshiftCogwheel.EXTRA_COGWHEEL_CONFIG;
    }
}
