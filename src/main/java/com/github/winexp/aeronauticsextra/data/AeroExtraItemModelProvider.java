package com.github.winexp.aeronauticsextra.data;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.registry.AeroExtraItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class AeroExtraItemModelProvider extends ItemModelProvider {
    public AeroExtraItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AeronauticsExtra.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(AeroExtraItems.ANDESITE_GPS_CORE.get());
        this.basicItem(AeroExtraItems.BRASS_GPS_CORE.get());
    }
}
