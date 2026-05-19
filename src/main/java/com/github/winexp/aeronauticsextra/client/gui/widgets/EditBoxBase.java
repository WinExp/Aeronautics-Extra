package com.github.winexp.aeronauticsextra.client.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class EditBoxBase<T> extends EditBox {
    protected boolean validInput;

    public EditBoxBase(int x, int y, int width, int height, @Nullable EditBox editBox, Component message) {
        super(Minecraft.getInstance().font, x, y, width, height, editBox, message);
        super.setFilter(this::filterInput);
        super.setResponder(this::checkInputNumber);
    }

    public abstract boolean filterInput(String text);

    public abstract boolean validateInput(String text);

    public abstract int getValidColor();

    public abstract int getInvalidColor();

    public void checkInputNumber(String text) {
        if (this.validateInput(text)) {
            this.validInput = true;
            this.setTextColor(this.getValidColor());
        } else {
            this.validInput = false;
            this.setTextColor(this.getInvalidColor());
        }
    }

    public boolean isValidInput() {
        return this.validInput;
    }

    @Override
    public void setFilter(Predicate<String> filter) {
        super.setFilter(filter.and(this::filterInput));
    }

    @Override
    public void setResponder(Consumer<String> responder) {
        super.setResponder(responder.andThen(this::checkInputNumber));
    }

    public abstract T getBoxValue();

    public abstract void setBoxValue(T value);

    public final String getValue() {
        return super.getValue();
    }

    public final void setValue(String value) {
        super.setValue(value);
    }
}
