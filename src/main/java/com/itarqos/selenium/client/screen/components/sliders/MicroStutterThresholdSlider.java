package com.itarqos.selenium.client.screen.components.sliders;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class MicroStutterThresholdSlider extends SliderWidget {
    private static final int MIN = 10;
    private static final int MAX = 50;
    private final SeleniumConfig cfg;

    public MicroStutterThresholdSlider(int x, int y, int width, int height, int initial, SeleniumConfig cfg) {
        super(x, y, width, height, Text.of(label(initial)), normalize(initial));
        this.cfg = cfg;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(label(current())));
    }

    @Override
    protected void applyValue() {
        cfg.microStutterThresholdMs = current();
        SeleniumClient.saveConfig();
    }

    private static double normalize(int v) {
        return (clamp(v) - (double) MIN) / (double) (MAX - MIN);
    }

    private int current() {
        return clamp((int) Math.round(MIN + this.value * (MAX - MIN)));
    }

    private static int clamp(int v) {
        return Math.max(MIN, Math.min(MAX, v));
    }

    private static String label(int v) {
        return Text.translatable("selenium.settings.micro_stutter_threshold", clamp(v)).getString();
    }
}
