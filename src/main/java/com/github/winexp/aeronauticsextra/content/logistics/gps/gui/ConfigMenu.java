package com.github.winexp.aeronauticsextra.content.logistics.gps.gui;

import com.github.winexp.aeronauticsextra.AeroExtraBlocks;
import com.github.winexp.aeronauticsextra.content.blocks.gps.GPSSatelliteBlockEntity;
import com.simibubi.create.foundation.gui.menu.MenuBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ConfigMenu extends MenuBase<GPSSatelliteBlockEntity> {
    public ConfigMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ConfigMenu(MenuType<?> type, int id, Inventory inv, GPSSatelliteBlockEntity contentHolder) {
        super(type, id, inv, contentHolder);
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
        this.addSlot(new SlotItemHandler(contentHolder.inventory, 0, 66, 122));
        this.addPlayerSlots(8, 165);
    }

    @Override
    protected void saveData(GPSSatelliteBlockEntity contentHolder) {
    }

    // 0 -> core slot
    // 1-27 -> player inventory
    // 28-36 -> player hotbar
    @Override
    public ItemStack quickMoveStack(Player player, int idx) {
        ItemStack quickMovedStack = ItemStack.EMPTY;
        Slot quickMovedSlot = this.slots.get(idx);
        if (quickMovedSlot.hasItem()) {
            ItemStack rawStack = quickMovedSlot.getItem();
            quickMovedStack = rawStack.copy();

            if (idx == 0) {
                if (!this.moveItemStackTo(rawStack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (idx >= 1 && idx <= 36) {
                if (!this.moveItemStackTo(rawStack, 0, 1, false)) {
                    if (idx <= 27) {
                        if (!this.moveItemStackTo(rawStack, 28, 37, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        if (!this.moveItemStackTo(rawStack, 1, 28, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }

            if (rawStack.isEmpty()) {
                quickMovedSlot.set(ItemStack.EMPTY);
            } else {
                quickMovedSlot.setChanged();
            }

            if (rawStack.getCount() == quickMovedStack.getCount()) {
                return ItemStack.EMPTY;
            }
            quickMovedSlot.onTake(player, rawStack);
        }

        return quickMovedStack;
    }
}
