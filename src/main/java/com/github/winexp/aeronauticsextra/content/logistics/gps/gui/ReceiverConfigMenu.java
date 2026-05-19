package com.github.winexp.aeronauticsextra.content.logistics.gps.gui;

import com.github.winexp.aeronauticsextra.content.blocks.geomatics.gps.receiver.GPSReceiverBlockEntity;
import com.github.winexp.aeronauticsextra.registry.AeroExtraBlocks;
import com.github.winexp.aeronauticsextra.registry.AeroExtraMenuTypes;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ReceiverConfigMenu extends MenuBase<GPSReceiverBlockEntity> {
    public ReceiverConfigMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    private ReceiverConfigMenu(MenuType<?> type, int id, Inventory inv, GPSReceiverBlockEntity contentHolder) {
        super(type, id, inv, contentHolder);
    }

    public static ReceiverConfigMenu create(int id, Inventory inv, GPSReceiverBlockEntity be) {
        return new ReceiverConfigMenu(AeroExtraMenuTypes.GPS_RECEIVER_CONFIG.get(), id, inv, be);
    }

    @Override
    protected GPSReceiverBlockEntity createOnClient(RegistryFriendlyByteBuf extraData) {
        return AeroExtraBlocks.GPS_RECEIVER.get().getBlockEntity(Minecraft.getInstance().level, extraData.readBlockPos());
    }

    @Override
    protected void initAndReadInventory(GPSReceiverBlockEntity contentHolder) {
    }

    @Override
    protected void addSlots() {
    }

    @Override
    protected void saveData(GPSReceiverBlockEntity contentHolder) {
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }
}
