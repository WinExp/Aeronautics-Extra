package com.github.winexp.aeronauticsextra.client.gui.widgets;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.IntegerRange;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class IntegerEditBox extends EditBoxBase<Integer> {
    @NotNull
    public IntegerRange range = IntegerRange.of(Integer.MIN_VALUE, Integer.MAX_VALUE);

    public IntegerEditBox(int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
        super(x, y, width, height, editBox, message);
        this.setMaxLength(11);
    }

    @Override
    public boolean filterInput(String text) {
        return StringUtils.containsOnly(text, "-0123456789")
                && (!text.contains("-") || (StringUtils.countMatches(text, "-") == 1 && text.startsWith("-")));
    }

    @Override
    public boolean validateInput(String text) {
        try {
            return this.range.contains(Integer.valueOf(text));
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
    public Integer getBoxValue() {
        if (this.validInput) {
            return Integer.valueOf(this.getValue());
        }
        else return 0;
    }

    @Override
    public void setBoxValue(Integer value) {
        this.setValue(String.valueOf(value));
    }
}
