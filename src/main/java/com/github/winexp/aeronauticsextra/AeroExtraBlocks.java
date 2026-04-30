package com.github.winexp.aeronauticsextra;

import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntry;

public class AeroExtraBlocks {
    private static final CreateRegistrate REGISTRATE = AeronauticsExtra.getRegistrate();

    public static final BlockEntry<GPSSatelliteBlock> GPS_SATELLITE = REGISTRATE
            .block("gps_satellite", GPSSatelliteBlock::new)
            .blockstate((c, p) -> p.simpleBlock(c.get(), p.models()
                    .getExistingFile(p.modLoc("block/gps_satellite/block"))))
            .initialProperties(SharedProperties::stone)
            .properties(properties -> properties.noOcclusion().isRedstoneConductor((_1, _2, _3) -> false))
            .transform(TagGen.pickaxeOnly())
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .tag(AeroExtraBlockTags.SUPER_HEAVY)
            .item().transform(ModelGen.customItemModel("gps_satellite", "block"))
            .lang("GPS Satellite")
            .register();

    public static void register() {}
}
