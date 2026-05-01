package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class AeroExtraBlocks {
    private static final CreateRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final BlockEntry<GPSSatelliteBlock> GPS_SATELLITE = REGISTRATE
            .block("gps_satellite", GPSSatelliteBlock::new)
            .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                    .getExistingFile(p.modLoc("block/gps_satellite/border"))))
            .initialProperties(SharedProperties::stone)
            .properties(properties -> properties.isRedstoneConductor(AeroExtraBlocks::never)
                    .isSuffocating(AeroExtraBlocks::never)
                    .noOcclusion())
            .transform(TagGen.pickaxeOnly())
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .tag(AeroExtraBlockTags.SUPER_HEAVY)
            .item().transform(ModelGen.customItemModel("gps_satellite", "item"))
            .lang("GPS Satellite")
            .register();

    private static boolean never(BlockState var1, BlockGetter var2, BlockPos var3) {
        return false;
    }

    public static void register() {}
}
