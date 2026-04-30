package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.AeroExtraBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GPSSatelliteBlock extends BaseEntityBlock implements IBE<GPSSatelliteBlockEntity> {
    public static final MapCodec<GPSSatelliteBlock> CODEC = simpleCodec(GPSSatelliteBlock::new);

    public GPSSatelliteBlock(Properties properties) {
        super(properties);
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
        if (player.isCrouching()) return InteractionResult.PASS;

        return onBlockEntityUse(level, pos, be -> {
            if (!level.isClientSide) {
                player.openMenu(this.getMenuProvider(state, level, pos), be::sendToMenu);
            }

            return InteractionResult.SUCCESS;
        });
    }
}
