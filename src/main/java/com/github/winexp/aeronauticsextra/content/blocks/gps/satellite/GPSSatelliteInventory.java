package com.github.winexp.aeronauticsextra.content.blocks.gps.satellite;

import com.github.winexp.aeronauticsextra.registry.AeroExtraItemTags;
import com.simibubi.create.foundation.item.SmartInventory;

public class GPSSatelliteInventory extends SmartInventory {
    public GPSSatelliteInventory(GPSSatelliteBlockEntity be) {
        super(1, be, 1, false, (slot, stack) -> stack.is(AeroExtraItemTags.ANTENNA));
        this.whenContentsChanged(slot -> be.updateLazyTickRate());
    }
}
