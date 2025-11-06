package com.itarqos.selenium.client.screen.tabs;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.client.screen.components.sliders.DistanceSlider;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

public class EntitiesTab implements SettingsTab {
    
    @Override
    public void buildWidgets(TabWidgetBuilder builder) {
        SeleniumConfig cfg = builder.getConfig();
        int left = builder.getLeft();
        int width = builder.getWidth();
        
        // Entity culling master
        builder.addButton(ButtonWidget.builder(entityCullingLabel(cfg), b -> {
            cfg.enableEntityCulling = !cfg.enableEntityCulling;
            b.setMessage(entityCullingLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.entity_culling")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Behind culling
        builder.addButton(ButtonWidget.builder(entityBehindLabel(cfg), b -> {
            cfg.enableEntityBehindCulling = !cfg.enableEntityBehindCulling;
            b.setMessage(entityBehindLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.behind_culling")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Vertical band culling for entities
        builder.addButton(ButtonWidget.builder(entityBandLabel(cfg), b -> {
            cfg.enableEntityVerticalBandCulling = !cfg.enableEntityVerticalBandCulling;
            b.setMessage(entityBandLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.entity_y_band")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Distance slider for entity culling
        builder.addSlider(new DistanceSlider(left, builder.getCurrentY(), width, 20, 
            cfg.entityCullingDistance, cfg), 20);
        
        builder.addGap(8);
    }
    
    private Text entityCullingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.entity_culling", 
            Text.translatable(cfg.enableEntityCulling ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text entityBehindLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.behind_culling", 
            Text.translatable(cfg.enableEntityBehindCulling ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text entityBandLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.entity_y_band", 
            Text.translatable(cfg.enableEntityVerticalBandCulling ? "selenium.common.on" : "selenium.common.off"));
    }
}
