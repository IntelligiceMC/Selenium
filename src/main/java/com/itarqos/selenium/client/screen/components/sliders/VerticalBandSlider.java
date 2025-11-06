package com.itarqos.selenium.client.screen.components.sliders;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class VerticalBandSlider extends SliderWidget {
    private static final int MIN = 5;
    private static final int MAX = 50;
    private final SeleniumConfig cfg;

    public VerticalBandSlider(int x, int y, int width, int height, int initial, SeleniumConfig cfg) {
        super(x, y, width, height, Text.of(bandLabel(initial)), normalize(initial));
        this.cfg = cfg;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(bandLabel(current())));
    }

    @Override
    protected void applyValue() {
        cfg.verticalBandHalfHeight = current();
        SeleniumClient.saveConfig();
    }

    private static double normalize(int v) {
        return (clamp(v) - (double) MIN) / (double) (MAX - MIN);
    }

    private int current() {
        int v = (int) Math.round(MIN + this.value * (MAX - MIN));
        return clamp(v);
    }

    private static int clamp(int v) {
        if (v < MIN) return MIN;
        if (v > MAX) return MAX;
        return v;
    }

    private static String bandLabel(int v) {
        return Text.translatable("selenium.settings.vertical_band_half", clamp(v)).getString();
    }
}
