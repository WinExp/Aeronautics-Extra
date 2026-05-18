package com.github.winexp.aeronauticsextra.content.blocks.kinetics;

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

public class CVTGearshiftValueBehaviour extends ScrollValueBehaviour {
    private static final String VALUE_FORMAT = "%s%%";
    private static final Component TITLE = AeroExtraLang.translate("generic.speed").component();

    public CVTGearshiftValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
        super(label, be, slot);
        this.between(1, 100);
        this.value = 50;
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        return new ValueSettingsBoard(this.label, this.max, 10, List.of(TITLE), new ValueSettingsFormatter(this::format));
    }

    private MutableComponent format(ValueSettings valueSettings) {
        int value = Math.clamp(valueSettings.value(), 1, this.max);
        return AeroExtraLang.text(VALUE_FORMAT.formatted(value)).component();
    }

    @Override
    public String getClipboardKey() {
        return "Shift Speed";
    }
}
