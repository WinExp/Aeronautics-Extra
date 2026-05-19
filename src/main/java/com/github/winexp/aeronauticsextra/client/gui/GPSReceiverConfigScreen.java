package com.github.winexp.aeronauticsextra.client.gui;

import com.github.winexp.aeronauticsextra.client.gui.widgets.DoubleEditBox;
import com.github.winexp.aeronauticsextra.client.gui.widgets.IntegerEditBox;
import com.github.winexp.aeronauticsextra.content.blocks.geomatics.gps.receiver.GPSReceiverBlockEntity;
import com.github.winexp.aeronauticsextra.content.logistics.gps.gui.ReceiverConfigMenu;
import com.github.winexp.aeronauticsextra.content.logistics.gps.networking.ServerBoundReceiverConfigRequest;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec3;

public class GPSReceiverConfigScreen extends AbstractSimiContainerScreen<ReceiverConfigMenu> {
    private final ReceiverConfigMenu menu;
    private DoubleEditBox xEditBox;
    private DoubleEditBox yEditBox;
    private DoubleEditBox zEditBox;
    private IntegerEditBox maxDistanceEditBox;

    public GPSReceiverConfigScreen(ReceiverConfigMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.menu = menu;
    }

    private boolean canSave() {
        return this.xEditBox.isValidInput() && this.yEditBox.isValidInput() && this.zEditBox.isValidInput()
                && this.maxDistanceEditBox.isValidInput();
    }

    private void save() {
        if (this.canSave()) {
            Vec3 targetPos = new Vec3(
                    this.xEditBox.getBoxValue(),
                    this.yEditBox.getBoxValue(),
                    this.zEditBox.getBoxValue()
            );
            int maxDistance = this.maxDistanceEditBox.getBoxValue();
            CatnipServices.NETWORK.sendToServer(new ServerBoundReceiverConfigRequest(this.menu.contentHolder.getBlockPos(), targetPos, maxDistance));
        }
    }

    @Override
    public void onClose() {
        this.save();
        super.onClose();
    }

    private DoubleEditBox createPositionEditBox(int x, int y, int width, int height, EditBox parent, double initValue) {
        DoubleEditBox editBox = new DoubleEditBox(x, y, width, height, parent, Component.empty());
        editBox.positionComplement = true;
        if (editBox.getValue().isEmpty()) editBox.setBoxValue(initValue);
        return editBox;
    }

    @Override
    protected void init() {
        super.init();
        this.xEditBox = this.addRenderableWidget(this.createPositionEditBox((this.width - 140) / 2, (this.height - 60) / 2, 40, 20,
                this.xEditBox, this.menu.contentHolder.getTargetPos().x));
        this.yEditBox = this.addRenderableWidget(this.createPositionEditBox((this.width - 40) / 2, (this.height - 60) / 2, 40, 20,
                this.yEditBox, this.menu.contentHolder.getTargetPos().y));
        this.zEditBox = this.addRenderableWidget(this.createPositionEditBox((this.width + 60) / 2, (this.height - 60) / 2, 40, 20,
                this.zEditBox, this.menu.contentHolder.getTargetPos().z));
        this.maxDistanceEditBox = this.addRenderableWidget(new IntegerEditBox((this.width - 40) / 2, (this.height + 20) / 2, 40, 20,
                this.maxDistanceEditBox, Component.empty()));
        this.maxDistanceEditBox.range = GPSReceiverBlockEntity.MAX_DISTANCE_RANGE;
        this.maxDistanceEditBox.setBoxValue(this.menu.contentHolder.getMaxDistance());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
    }

    @Override
    public ReceiverConfigMenu getMenu() {
        return this.menu;
    }
}
