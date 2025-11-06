package com.itarqos.selenium.client.screen.components;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ToggleButton {
    
    public static ButtonWidget.Builder create(Text label, BooleanSupplier getter, Consumer<Boolean> setter) {
        return ButtonWidget.builder(label, button -> {
            boolean newValue = !getter.getAsBoolean();
            setter.accept(newValue);
            button.setMessage(label);
        });
    }
}
