package com.itarqos.selenium.client.screen.components.sliders;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class ParticleFarSizeScaleSlider extends SliderWidget {
    private static final double MIN = 0.2;
    private static final double MAX = 1.0;
    private final SeleniumConfig cfg;

    public ParticleFarSizeScaleSlider(int x, int y, int width, int height, float initial, SeleniumConfig cfg) {
        super(x, y, width, height, Text.of(label(initial)), normalize(initial));
        this.cfg = cfg;
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.of(label((float) current())));
    }

    @Override
    protected void applyValue() {
        cfg.particleFarSizeScale = (float) current();
        SeleniumClient.saveConfig();
    }

    private static double normalize(float v) {
        return (clamp(v) - MIN) / (MAX - MIN);
    }

    private double current() {
        return MIN + this.value * (MAX - MIN);
    }

    private static float clamp(float v) {
        return (float) Math.max(MIN, Math.min(MAX, v));
    }

    private static String label(float v) {
        return Text.translatable("selenium.settings.particle_far_size_scale", String.format("%.2f", clamp(v))).getString();
    }
}
