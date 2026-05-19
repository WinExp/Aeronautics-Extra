package com.github.winexp.aeronauticsextra.client.gui.widgets;

import net.createmod.catnip.platform.CatnipClientServices;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.DoubleRange;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleEditBox extends EditBoxBase<Double> {
    private static final NumberFormat FORMAT = DecimalFormat.getInstance(CatnipClientServices.CLIENT_HOOKS.getCurrentLocale());

    public boolean positionComplement;
    @NotNull
    public DoubleRange range = DoubleRange.of(-Double.MAX_VALUE, Double.MAX_VALUE);

    static {
        FORMAT.setMaximumFractionDigits(2);
        FORMAT.setMinimumFractionDigits(1);
    }

    public DoubleEditBox(int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
        super(x, y, width, height, editBox, message);
        this.setMaxLength(32);
    }

    @Override
    public boolean filterInput(String text) {
        return StringUtils.containsOnly(text, "-.0123456789")
                && StringUtils.countMatches(text, '.') <= 1
                && (!text.contains("-") || (StringUtils.countMatches(text, "-") == 1 && text.startsWith("-")));
    }

    @Override
    public boolean validateInput(String text) {
        try {
            return this.range.contains(Double.valueOf(text));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public int getValidColor() {
        return EditBox.DEFAULT_TEXT_COLOR;
    }

    @Override
    public int getInvalidColor() {
        return 0xff0000;
    }

    @Override
    public Double getBoxValue() {
        if (this.validInput) {
            double val = Double.parseDouble(this.getValue());
            if (this.positionComplement && !this.getValue().contains(".")) {
                return val + 0.5;
            } else {
                return val;
            }
        }
        else return 0.0;
    }

    @Override
    public void setBoxValue(Double value) {
        this.setValue(FORMAT.format(value));
    }
}
