package com.itarqos.selenium.client.screen.tabs;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

public class ExtraTab implements SettingsTab {
    
    @Override
    public void buildWidgets(TabWidgetBuilder builder) {
        SeleniumConfig cfg = builder.getConfig();
        int left = builder.getLeft();
        int width = builder.getWidth();
        
        // Overlay toggle
        builder.addButton(ButtonWidget.builder(overlayLabel(cfg), b -> {
            cfg.showCullingOverlay = !cfg.showCullingOverlay;
            b.setMessage(overlayLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.overlay")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Overlay position cycle
        builder.addButton(ButtonWidget.builder(overlayPositionLabel(cfg), b -> {
            cfg.overlayPosition = nextOverlayPosition(cfg.overlayPosition);
            b.setMessage(overlayPositionLabel(cfg));
            SeleniumClient.saveConfig();
        }).dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Debug mode toggle (forces counters even without F3)
        builder.addButton(ButtonWidget.builder(debugModeLabel(cfg), b -> {
            cfg.enableDebugMode = !cfg.enableDebugMode;
            b.setMessage(debugModeLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.debug_mode")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
    }
    
    private Text overlayLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.overlay", 
            Text.translatable(cfg.showCullingOverlay ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text debugModeLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.debug_mode", 
            Text.translatable(cfg.enableDebugMode ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text overlayPositionLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.overlay_position", overlayPositionValue(cfg));
    }
    
    private Text overlayPositionValue(SeleniumConfig cfg) {
        return switch (cfg.overlayPosition) {
            case TOP_LEFT -> Text.translatable("selenium.overlay.top_left");
            case TOP_RIGHT -> Text.translatable("selenium.overlay.top_right");
            case BOTTOM_LEFT -> Text.translatable("selenium.overlay.bottom_left");
            case BOTTOM_RIGHT -> Text.translatable("selenium.overlay.bottom_right");
            case CENTER -> Text.translatable("selenium.overlay.center");
        };
    }
    
    private static SeleniumConfig.OverlayPosition nextOverlayPosition(SeleniumConfig.OverlayPosition p) {
        SeleniumConfig.OverlayPosition[] vals = SeleniumConfig.OverlayPosition.values();
        int i = (p.ordinal() + 1) % vals.length;
        return vals[i];
    }
}
