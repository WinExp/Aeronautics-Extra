package com.github.winexp.aeronauticsextra.content.blocks.gps.satellite;

import com.github.winexp.aeronauticsextra.registry.AeroExtraTags;
import com.simibubi.create.foundation.item.SmartInventory;

public class GPSSatelliteInventory extends SmartInventory {
    public GPSSatelliteInventory(GPSSatelliteBlockEntity be) {
        super(1, be, 1, false, (slot, stack) -> stack.is(AeroExtraTags.ItemTags.ANTENNAS.tag));
        this.whenContentsChanged(slot -> be.updateLazyTickRate());
    }
}
