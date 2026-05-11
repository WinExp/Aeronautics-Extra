package com.github.winexp.aeronauticsextra.content.item;

import com.github.winexp.aeronauticsextra.content.entity.SmallBalloonEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SmallBalloonItem extends Item {
    public SmallBalloonItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (!level.isClientSide) {
            SmallBalloonEntity entity = getEntity(level, player.position(), itemStack.get(DataComponents.BASE_COLOR));
            entity.setLeashedTo(player, true);
            level.addFreshEntity(entity);
            itemStack.consume(1, player);
        }
        return InteractionResultHolder.success(itemStack);
    }

    private static SmallBalloonEntity getEntity(Level level, Vec3 pos, DyeColor color) {
        SmallBalloonEntity entity = SmallBalloonEntity.create(level, pos.x, pos.y, pos.z, color);
        return entity;
    }
}
