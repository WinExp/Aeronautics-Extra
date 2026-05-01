package com.github.winexp.aeronauticsextra.content.logistics.gps.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PositionEditBox extends EditBox {
    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    private boolean validInput;

    public PositionEditBox(Font font, int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
        super(font, x, y, width, height, editBox, message);
        super.setFilter(this::validateInput);
        super.setResponder(this::checkInputNumber);
        this.setMaxLength(11);
    }

    private boolean validateInput(String text) {
        return StringUtils.containsOnly(text, "-.0123456789") && StringUtils.countMatches(text, '.') <= 1;
    }

    private void checkInputNumber(String text) {
        try {
            Double.parseDouble(text);
            this.validInput = true;
            this.setTextColor(EditBox.DEFAULT_TEXT_COLOR);
        } catch (NumberFormatException ignored) {
            this.validInput = false;
            this.setTextColor(0xff0000);
        }
    }

    public boolean isValidInput() {
        return this.validInput;
    }

    @Override
    public void setFilter(Predicate<String> filter) {
        super.setFilter(filter.and(this::validateInput));
    }

    @Override
    public void setResponder(Consumer<String> responder) {
        super.setResponder(responder.andThen(this::checkInputNumber));
    }

    public double getDoubleValue() {
        return this.getDoubleValue(false);
    }

    public double getDoubleValue(boolean positionComplement) {
        if (this.validInput) {
            double val = Double.parseDouble(this.getValue());
            if (positionComplement && !this.getValue().contains(".")) {
                return val + 0.5;
            } else {
                return val;
            }
        }
        else return Double.NaN;
    }

    public void setDoubleValue(double value) {
        this.setValue(FORMAT.format(value));
    }
}
