package com.github.winexp.aeronauticsextra.content.blocks.gps;

import com.github.winexp.aeronauticsextra.registry.AeroExtraItemTags;
import com.simibubi.create.foundation.item.SmartInventory;

public class GPSSatelliteInventory extends SmartInventory {
    public GPSSatelliteInventory(GPSSatelliteBlockEntity be) {
        super(1, be, (slot, stack) -> stack.is(AeroExtraItemTags.GPS_CORE));
        this.withMaxStackSize(1);
        this.whenContentsChanged(slot -> be.updateLazyTickRate());
    }
}
