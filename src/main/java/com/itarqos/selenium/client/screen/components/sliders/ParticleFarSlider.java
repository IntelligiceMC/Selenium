package com.itarqos.selenium.client.screen.components.sliders;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ParticleFarSlider extends SliderWidget {
    private static final double MIN = 16.0;
    private static final double MAX = 128.0;
    private final SeleniumConfig cfg;

    public ParticleFarSlider(int x, int y, int width, int height, double initial, SeleniumConfig cfg) {
        super(x, y, width, height, Text.of(label(initial)), normalize(initial));
        this.cfg = cfg;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(label(current())));
    }

    @Override
    protected void applyValue() {
        cfg.particleFar = Math.max(current(), cfg.particleNear + 1.0);
        SeleniumClient.saveConfig();
    }

    private static double normalize(double v) {
        return (clamp(v) - MIN) / (MAX - MIN);
    }

    private double current() {
        return MIN + this.value * (MAX - MIN);
    }

    private static double clamp(double v) {
        return Math.max(MIN, Math.min(MAX, v));
    }

    private static String label(double v) {
        return Text.translatable("selenium.settings.particle_far", String.format("%.1f", clamp(v))).getString();
    }
}
