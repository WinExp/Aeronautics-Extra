package com.github.winexp.aeronauticsextra.content.logistics.gps.gui;

import com.github.winexp.aeronauticsextra.content.logistics.gps.networking.ServerBoundConfigRequest;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.phys.Vec3;

public class ConfigScreen extends AbstractSimiContainerScreen<ConfigMenu> {
    private final ConfigMenu menu;
    private PositionEditBox xEditBox;
    private PositionEditBox yEditBox;
    private PositionEditBox zEditBox;
    private PositionEditBox targetXEditBox;
    private PositionEditBox targetYEditBox;
    private PositionEditBox targetZEditBox;

    public ConfigScreen(ConfigMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.menu = menu;
    }

    private boolean canSave() {
        return this.xEditBox.isValidInput() && this.yEditBox.isValidInput() && this.zEditBox.isValidInput()
                && this.targetXEditBox.isValidInput() && this.targetYEditBox.isValidInput() && this.targetZEditBox.isValidInput();
    }

    private void save() {
        if (this.canSave()) {
            double x = this.xEditBox.getDoubleValue(true);
            double y = this.yEditBox.getDoubleValue(true);
            double z = this.zEditBox.getDoubleValue(true);
            double targetX = this.targetXEditBox.getDoubleValue();
            double targetY = this.targetYEditBox.getDoubleValue();
            double targetZ = this.targetZEditBox.getDoubleValue();
            CatnipServices.NETWORK.sendToServer(new ServerBoundConfigRequest(this.menu.contentHolder.getBlockPos(), new Vec3(x, y, z), new Vec3(targetX, targetY, targetZ)));
        }
    }

    @Override
    public void onClose() {
        this.save();
        super.onClose();
    }

    private PositionEditBox createLocationEditBox(int x, int y, int width, int height, EditBox parent, double initValue) {
        PositionEditBox editBox = new PositionEditBox(this.minecraft.font, x, y, width, height, parent, Component.empty());
        if (editBox.getValue().isEmpty()) editBox.setDoubleValue(initValue);
        return editBox;
    }

    @Override
    protected void init() {
        super.init();
        this.xEditBox = this.addRenderableWidget(this.createLocationEditBox((this.width - 140) / 2, (this.height - 60) / 2, 40, 20,
                this.xEditBox, this.menu.contentHolder.getPosition().x));
        this.yEditBox = this.addRenderableWidget(this.createLocationEditBox((this.width - 40) / 2, (this.height - 60) / 2, 40, 20,
                this.yEditBox, this.menu.contentHolder.getPosition().y));
        this.zEditBox = this.addRenderableWidget(this.createLocationEditBox((this.width + 60) / 2, (this.height - 60) / 2, 40, 20,
                this.zEditBox, this.menu.contentHolder.getPosition().z));
        this.targetXEditBox = this.addRenderableWidget(this.createLocationEditBox((this.width - 140) / 2, (this.height + 20) / 2, 40, 20,
                this.targetXEditBox, this.menu.contentHolder.getTargetPosition().x));
        this.targetYEditBox = this.addRenderableWidget(this.createLocationEditBox((this.width - 40) / 2, (this.height + 20) / 2, 40, 20,
                this.targetYEditBox, this.menu.contentHolder.getTargetPosition().y));
        this.targetZEditBox = this.addRenderableWidget(this.createLocationEditBox((this.width + 60) / 2, (this.height + 20) / 2, 40, 20,
                this.targetZEditBox, this.menu.contentHolder.getTargetPosition().z));
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
    public ConfigMenu getMenu() {
        return this.menu;
    }
}
