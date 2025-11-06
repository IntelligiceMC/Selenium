package com.itarqos.selenium.client.screen.tabs;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.client.screen.components.sliders.*;
import com.itarqos.selenium.config.SeleniumConfig;
import com.itarqos.selenium.util.SeleniumLog;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

public class AdvancedTab implements SettingsTab {
    
    @Override
    public void buildWidgets(TabWidgetBuilder builder) {
        SeleniumConfig cfg = builder.getConfig();
        int left = builder.getLeft();
        int width = builder.getWidth();
        
        // Predictive prefetch
        builder.addButton(ButtonWidget.builder(predictivePrefetchLabel(cfg), b -> {
            cfg.enablePredictivePrefetch = !cfg.enablePredictivePrefetch;
            b.setMessage(predictivePrefetchLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.predictive_prefetch")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // LOD throttling toggle
        builder.addButton(ButtonWidget.builder(lodThrottlingLabel(cfg), b -> {
            cfg.lodThrottlingEnabled = !cfg.lodThrottlingEnabled;
            b.setMessage(lodThrottlingLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.lod_throttling")))
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
        
        // Partial meshing master toggle
        builder.addButton(ButtonWidget.builder(partialMeshingLabel(cfg), b -> {
            cfg.enablePartialMeshing = !cfg.enablePartialMeshing;
            b.setMessage(partialMeshingLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.partial_meshing")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Render scheduler toggle
        builder.addButton(ButtonWidget.builder(renderSchedulerLabel(cfg), b -> {
            cfg.enableRenderScheduler = !cfg.enableRenderScheduler;
            b.setMessage(renderSchedulerLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.render_scheduler")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Verbose logging toggle
        builder.addButton(ButtonWidget.builder(verboseLoggingLabel(cfg), b -> {
            cfg.verboseLogging = !cfg.verboseLogging;
            b.setMessage(verboseLoggingLabel(cfg));
            SeleniumLog.setVerbose(cfg.verboseLogging);
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.verbose_logging")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Prediction everywhere toggle
        builder.addButton(ButtonWidget.builder(Text.translatable("selenium.settings.prediction_everywhere", 
            Text.translatable(cfg.enablePredictionEverywhere ? "selenium.common.on" : "selenium.common.off")), b -> {
            cfg.enablePredictionEverywhere = !cfg.enablePredictionEverywhere;
            b.setMessage(Text.translatable("selenium.settings.prediction_everywhere", 
                Text.translatable(cfg.enablePredictionEverywhere ? "selenium.common.on" : "selenium.common.off")));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.prediction_everywhere")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Prediction ahead ticks slider
        builder.addSlider(new PredictionAheadSlider(left, builder.getCurrentY(), width, 20, 
            cfg.predictionAheadTicks, cfg), 20);
        
        builder.addGap(8);
        
        // Visibility deprioritization toggle & sliders
        builder.addButton(ButtonWidget.builder(Text.translatable("selenium.settings.visibility_deprioritization", 
            Text.translatable(cfg.enableVisibilityDeprioritization ? "selenium.common.on" : "selenium.common.off")), b -> {
            cfg.enableVisibilityDeprioritization = !cfg.enableVisibilityDeprioritization;
            b.setMessage(Text.translatable("selenium.settings.visibility_deprioritization", 
                Text.translatable(cfg.enableVisibilityDeprioritization ? "selenium.common.on" : "selenium.common.off")));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.visibility_deprioritization")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        builder.addSlider(new HiddenFramesSlider(left, builder.getCurrentY(), width, 20, 
            cfg.hiddenDeprioritizeFrames, cfg), 20);
        
        builder.addGap(8);
        
        builder.addSlider(new UnhidePerTickSlider(left, builder.getCurrentY(), width, 20, 
            cfg.unhidePerTick, cfg), 20);
        
        builder.addGap(8);
        
        // Particle scaling toggle & sliders
        builder.addButton(ButtonWidget.builder(Text.translatable("selenium.settings.particle_distance_scaling", 
            Text.translatable(cfg.particleDistanceScaling ? "selenium.common.on" : "selenium.common.off")), b -> {
            cfg.particleDistanceScaling = !cfg.particleDistanceScaling;
            b.setMessage(Text.translatable("selenium.settings.particle_distance_scaling", 
                Text.translatable(cfg.particleDistanceScaling ? "selenium.common.on" : "selenium.common.off")));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.particle_distance_scaling")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        builder.addSlider(new ParticleNearSlider(left, builder.getCurrentY(), width, 20, 
            cfg.particleNear, cfg), 20);
        
        builder.addGap(8);
        
        builder.addSlider(new ParticleFarSlider(left, builder.getCurrentY(), width, 20, 
            cfg.particleFar, cfg), 20);
        
        builder.addGap(8);
        
        builder.addSlider(new ParticleMinDensitySlider(left, builder.getCurrentY(), width, 20, 
            cfg.particleMinDensity, cfg), 20);
        
        builder.addGap(8);
        
        builder.addSlider(new ParticleFarSizeScaleSlider(left, builder.getCurrentY(), width, 20, 
            cfg.particleFarSizeScale, cfg), 20);
        
        builder.addGap(8);
        
        // QoS controller: target FPS & aggressiveness
        builder.addSlider(new TargetFpsSlider(left, builder.getCurrentY(), width, 20, 
            cfg.targetFps, cfg), 20);
        
        builder.addGap(8);
        
        builder.addSlider(new QosAggressivenessSlider(left, builder.getCurrentY(), width, 20, 
            (float)cfg.qosAggressiveness, cfg), 20);
        
        builder.addGap(8);
        
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
        
        // Turn-bias prefetch toggle & strength
        builder.addButton(ButtonWidget.builder(turnBiasLabel(cfg), b -> {
            cfg.enableTurnBiasPrefetch = !cfg.enableTurnBiasPrefetch;
            b.setMessage(turnBiasLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.turn_bias_prefetch")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        builder.addSlider(new TurnBiasStrengthSlider(left, builder.getCurrentY(), width, 20, 
            (float)cfg.turnBiasStrength, cfg), 20);
        
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
        
        // Particle tile budgeting toggle & sliders
        builder.addButton(ButtonWidget.builder(particleTileBudgetLabel(cfg), b -> {
            cfg.enableParticleTileBudget = !cfg.enableParticleTileBudget;
            b.setMessage(particleTileBudgetLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.particle_tile_budget")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        builder.addSlider(new ParticleTileBudgetSlider(left, builder.getCurrentY(), width, 20, 
            cfg.particleTileBudget, cfg), 20);
        
        builder.addGap(8);
        
        builder.addSlider(new ParticleTileSizeSlider(left, builder.getCurrentY(), width, 20, 
            cfg.particleTileSize, cfg), 20);
        
        builder.addGap(8);
    }
    
    private Text predictivePrefetchLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.predictive_prefetch", 
            Text.translatable(cfg.enablePredictivePrefetch ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text lodThrottlingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.lod_throttling", 
            Text.translatable(cfg.lodThrottlingEnabled ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text partialMeshingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.partial_meshing", 
            Text.translatable(cfg.enablePartialMeshing ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text renderSchedulerLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.render_scheduler", 
            Text.translatable(cfg.enableRenderScheduler ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text verboseLoggingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.verbose_logging", 
            Text.translatable(cfg.verboseLogging ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text screenSpaceBudgeterLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.screen_space_budgeter", 
            Text.translatable(cfg.enableScreenSpaceBudgeter ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text turnBiasLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.turn_bias_prefetch", 
            Text.translatable(cfg.enableTurnBiasPrefetch ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text microStutterLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.micro_stutter_guard", 
            Text.translatable(cfg.enableMicroStutterGuard ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text particleTileBudgetLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.particle_tile_budget", 
            Text.translatable(cfg.enableParticleTileBudget ? "selenium.common.on" : "selenium.common.off"));
    }
}
