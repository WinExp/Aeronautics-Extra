package com.github.winexp.aeronauticsextra.content.logistics.gps.gui;

import com.github.winexp.aeronauticsextra.content.blocks.gps.satellite.GPSSatelliteBlockEntity;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import com.github.winexp.aeronauticsextra.registry.AeroExtraMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ConfigMenu extends MenuBase<GPSSatelliteBlockEntity> {
    public ConfigMenu(int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(AeroExtraMenuTypes.GPS_SATELLITE_CONFIG.get(), id, inv, extraData);
    }

    public ConfigMenu(int id, Inventory inv, GPSSatelliteBlockEntity contentHolder) {
        super(AeroExtraMenuTypes.GPS_SATELLITE_CONFIG.get(), id, inv, contentHolder);
    }

    @Override
    protected GPSSatelliteBlockEntity createOnClient(RegistryFriendlyByteBuf extraData) {
        return AeroExtraBlocks.GPS_SATELLITE.get().getBlockEntity(Minecraft.getInstance().level, extraData.readBlockPos());
    }

    @Override
    protected void initAndReadInventory(GPSSatelliteBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
    }

    @Override
    protected void saveData(GPSSatelliteBlockEntity contentHolder) {
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }
}
