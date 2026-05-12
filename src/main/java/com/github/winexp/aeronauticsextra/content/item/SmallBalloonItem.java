package com.github.winexp.aeronauticsextra.content.item;

import com.github.winexp.aeronauticsextra.content.entity.SmallBalloonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SmallBalloonItem extends Item {
    public SmallBalloonItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            RandomSource r = level.random;
            double xOffset = (r.nextBoolean() ? 1 : -1) * r.nextDouble() * 0.5;
            double yOffset = (r.nextBoolean() ? 1 : -1) * r.nextDouble() * 0.5;
            double zOffset = (r.nextBoolean() ? 1 : -1) * r.nextDouble() * 0.5;
            Vec3 spawnPos = player.position().add(0, 3, 0).add(xOffset, yOffset, zOffset);
            SmallBalloonEntity entity = getEntity(level, spawnPos, itemStack.get(DataComponents.BASE_COLOR));
            float yRot = (r.nextBoolean() ? 1 : -1) * r.nextFloat() * 180;
            entity.setYRot(yRot);
            entity.yRotO = yRot;
            entity.setLeashedTo(player, true);
            level.addFreshEntity(entity);
            itemStack.consume(1, player);
        }
        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemStack = context.getItemInHand();
        BlockPos blockPos = context.getClickedPos();
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(BlockTags.FENCES)) {
            if (!level.isClientSide) {
                RandomSource r = level.random;
                double xOffset = (r.nextBoolean() ? 1 : -1) * r.nextDouble() * 0.5;
                double yOffset = (r.nextBoolean() ? 1 : -1) * r.nextDouble() * 0.5;
                double zOffset = (r.nextBoolean() ? 1 : -1) * r.nextDouble() * 0.5;
                Vec3 spawnPos = blockPos.above(3).getCenter().add(xOffset, yOffset, zOffset);
                SmallBalloonEntity entity = getEntity(level, spawnPos, itemStack.get(DataComponents.BASE_COLOR));
                float yRot = (r.nextBoolean() ? 1 : -1) * r.nextFloat() * 180;
                entity.setYRot(yRot);
                entity.yRotO = yRot;
                LeashFenceKnotEntity knot = LeashFenceKnotEntity.getOrCreateKnot(level, blockPos);
                knot.playPlacementSound();
                entity.setLeashedTo(knot, true);
                level.addFreshEntity(entity);
                itemStack.consume(1, player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static SmallBalloonEntity getEntity(Level level, Vec3 pos, DyeColor color) {
        SmallBalloonEntity entity = SmallBalloonEntity.create(level, pos.x, pos.y, pos.z, color);
        return entity;
    }
}
