package com.github.winexp.aeronauticsextra.content.display_source;

import com.github.winexp.aeronauticsextra.content.blocks.geomatics.gps.receiver.GPSReceiverBlockEntity;
import com.github.winexp.aeronauticsextra.data.AeroExtraLang;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.NumericSingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.Vec3;

public class GPSReceiverDisplaySource extends NumericSingleLineDisplaySource {
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        if (!(context.getSourceBlockEntity() instanceof GPSReceiverBlockEntity be)) return ZERO.copy();

        Vec3 pos = be.getCurrentPos();
        if (pos == null) return ZERO.copy();
        return switch (context.sourceConfig().getInt("GPSReceiverSelection")) {
            case 0 -> Component.literal(String.format("%.2f", pos.x));
            case 1 -> Component.literal(String.format("%.2f", pos.y));
            case 2 -> Component.literal(String.format("%.2f", pos.z));
            default -> EMPTY_LINE.copy();
        };
    }

    @Override
    public void initConfigurationWidgets(DisplayLinkContext context, ModularGuiLineBuilder builder, boolean isFirstLine) {
        super.initConfigurationWidgets(context, builder, isFirstLine);
        if (isFirstLine) return;

        var options = AeroExtraLang.translatedOptions("display_source.gps_receiver", "pos_x", "pos_y", "pos_z");
        builder.addSelectionScrollInput(0, 60, (input, label) ->
                input.forOptions(options), "GPSReceiverSelection");
    }

    @Override
    protected String getTranslationKey() {
        return "gps_receiver.data";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
