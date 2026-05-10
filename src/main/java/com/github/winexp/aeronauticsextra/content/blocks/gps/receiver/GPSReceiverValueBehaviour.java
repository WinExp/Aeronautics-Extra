package com.github.winexp.aeronauticsextra.content.blocks.gps.receiver;

import com.github.winexp.aeronauticsextra.data.AeroExtraLang;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class GPSReceiverValueBehaviour extends ScrollValueBehaviour {
    private static final Component TITLE = AeroExtraLang.translate("generic.duration").component();

    public GPSReceiverValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
        super(label, be, slot);
        this.between(1, 200);
        this.value = 5;
    }

    private void updateBlockEntity() {
        this.blockEntity.setLazyTickRate(this.value - 1);
    }

    @Override
    public void initialize() {
        this.updateBlockEntity();
    }

    @Override
    public void setValue(int value) {
        super.setValue(value);
        this.updateBlockEntity();
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        return new ValueSettingsBoard(this.label, this.max, 10, List.of(TITLE), new ValueSettingsFormatter(this::format));
    }

    private MutableComponent format(ValueSettings valueSettings) {
        int value = Math.clamp(valueSettings.value(), 1, this.max);
        return AeroExtraLang.number(value).text(" t").component();
    }

    @Override
    public String getClipboardKey() {
        return "Sampling Time";
    }
}
