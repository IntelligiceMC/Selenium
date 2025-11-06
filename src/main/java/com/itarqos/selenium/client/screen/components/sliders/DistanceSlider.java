package com.itarqos.selenium.client.screen.components.sliders;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class DistanceSlider extends SliderWidget {
    private static final double MIN = 32.0;
    private static final double MAX = 256.0;
    private final SeleniumConfig cfg;

    public DistanceSlider(int x, int y, int width, int height, double initial, SeleniumConfig cfg) {
        super(x, y, width, height, Text.of(label(initial)), normalize(initial));
        this.cfg = cfg;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(label(current())));
    }

    @Override
    protected void applyValue() {
        cfg.entityCullingDistance = current();
        SeleniumClient.saveConfig();
    }

    private static double normalize(double v) {
        return (clamp(v) - MIN) / (MAX - MIN);
    }

    private double current() {
        return MIN + this.value * (MAX - MIN);
    }

    private static double clamp(double v) {
        if (v < MIN) return MIN;
        if (v > MAX) return MAX;
        return v;
    }

    private static String label(double v) {
        return Text.translatable("selenium.settings.entity_distance", String.format("%.0f", clamp(v))).getString();
    }
}
