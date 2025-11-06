package com.itarqos.selenium.client.screen.components.sliders;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ParticleNearSlider extends SliderWidget {
    private static final double MIN = 4.0;
    private static final double MAX = 64.0;
    private final SeleniumConfig cfg;

    public ParticleNearSlider(int x, int y, int width, int height, double initial, SeleniumConfig cfg) {
        super(x, y, width, height, Text.of(label(initial)), normalize(initial));
        this.cfg = cfg;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(label(current())));
    }

    @Override
    protected void applyValue() {
        cfg.particleNear = current();
        // keep far >= near + 1
        if (cfg.particleFar < cfg.particleNear + 1.0) cfg.particleFar = cfg.particleNear + 1.0;
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
        return Text.translatable("selenium.settings.particle_near", String.format("%.1f", clamp(v))).getString();
    }
}
