package com.itarqos.selenium.client.screen.tabs;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.client.screen.components.sliders.*;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

public class RenderTab implements SettingsTab {
    
    @Override
    public void buildWidgets(TabWidgetBuilder builder) {
        SeleniumConfig cfg = builder.getConfig();
        int left = builder.getLeft();
        int width = builder.getWidth();
        
        // Render scheduler toggle
        builder.addButton(ButtonWidget.builder(renderSchedulerLabel(cfg), b -> {
            cfg.enableRenderScheduler = !cfg.enableRenderScheduler;
            b.setMessage(renderSchedulerLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.render_scheduler")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Screen-space budgeter toggle & slice budget per tick
        builder.addButton(ButtonWidget.builder(screenSpaceBudgeterLabel(cfg), b -> {
            cfg.enableScreenSpaceBudgeter = !cfg.enableScreenSpaceBudgeter;
            b.setMessage(screenSpaceBudgeterLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.screen_space_budgeter")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        builder.addSlider(new SliceBudgetSlider(left, builder.getCurrentY(), width, 20, 
            cfg.sliceBudgetPerTick, cfg), 20);
        
        builder.addGap(8);
        
        // LOD throttling toggle
        builder.addButton(ButtonWidget.builder(lodThrottlingLabel(cfg), b -> {
            cfg.lodThrottlingEnabled = !cfg.lodThrottlingEnabled;
            b.setMessage(lodThrottlingLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.lod_throttling")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Partial meshing master toggle
        builder.addButton(ButtonWidget.builder(partialMeshingLabel(cfg), b -> {
            cfg.enablePartialMeshing = !cfg.enablePartialMeshing;
            b.setMessage(partialMeshingLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.partial_meshing")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Frustum hysteresis slider (0..6 ticks)
        builder.addSlider(new HysteresisSlider(left, builder.getCurrentY(), width, 20, 
            cfg.frustumHysteresisTicks, cfg), 20);
        
        builder.addGap(8);
        
        // Slice debounce slider (50..600 ms)
        builder.addSlider(new DebounceSlider(left, builder.getCurrentY(), width, 20, 
            cfg.sliceDebounceMillis, cfg), 20);
        
        builder.addGap(8);
        
        // Micro-stutter guard toggle & threshold
        builder.addButton(ButtonWidget.builder(microStutterLabel(cfg), b -> {
            cfg.enableMicroStutterGuard = !cfg.enableMicroStutterGuard;
            b.setMessage(microStutterLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.micro_stutter_guard")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        builder.addSlider(new MicroStutterThresholdSlider(left, builder.getCurrentY(), width, 20, 
            cfg.microStutterThresholdMs, cfg), 20);
        
        builder.addGap(8);
        
        // QoS controller: target FPS & aggressiveness
        builder.addSlider(new TargetFpsSlider(left, builder.getCurrentY(), width, 20, 
            cfg.targetFps, cfg), 20);
        
        builder.addGap(8);
        
        builder.addSlider(new QosAggressivenessSlider(left, builder.getCurrentY(), width, 20, 
            (float)cfg.qosAggressiveness, cfg), 20);
        
        builder.addGap(8);
    }
    
    private Text renderSchedulerLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.render_scheduler", 
            Text.translatable(cfg.enableRenderScheduler ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text screenSpaceBudgeterLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.screen_space_budgeter", 
            Text.translatable(cfg.enableScreenSpaceBudgeter ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text lodThrottlingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.lod_throttling", 
            Text.translatable(cfg.lodThrottlingEnabled ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text partialMeshingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.partial_meshing", 
            Text.translatable(cfg.enablePartialMeshing ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text microStutterLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.micro_stutter_guard", 
            Text.translatable(cfg.enableMicroStutterGuard ? "selenium.common.on" : "selenium.common.off"));
    }
}
