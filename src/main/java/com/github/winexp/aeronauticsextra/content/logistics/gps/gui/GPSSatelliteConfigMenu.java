package com.github.winexp.aeronauticsextra.content.logistics.gps.gui;

import com.github.winexp.aeronauticsextra.content.blocks.gps.satellite.GPSSatelliteBlockEntity;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import com.github.winexp.aeronauticsextra.registry.AeroExtraMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class GPSSatelliteConfigMenu extends MenuBase<GPSSatelliteBlockEntity> {
    public GPSSatelliteConfigMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    private GPSSatelliteConfigMenu(MenuType<?> type, int id, Inventory inv, GPSSatelliteBlockEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static GPSSatelliteConfigMenu create(int id, Inventory inv, GPSSatelliteBlockEntity be) {
        return new GPSSatelliteConfigMenu(AeroExtraMenuTypes.GPS_SATELLITE_CONFIG.get(), id, inv, be);
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
