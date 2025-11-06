package com.itarqos.selenium.client.screen.tabs;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.client.screen.components.sliders.VerticalBandSlider;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.text.Text;

public class WorldTab implements SettingsTab {
    
    @Override
    public void buildWidgets(TabWidgetBuilder builder) {
        SeleniumConfig cfg = builder.getConfig();
        int left = builder.getLeft();
        int width = builder.getWidth();
        
        // Chunk culling master
        builder.addButton(ButtonWidget.builder(chunkCullingLabel(cfg), b -> {
            cfg.enableChunkCulling = !cfg.enableChunkCulling;
            b.setMessage(chunkCullingLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.chunk_culling")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Chunk vertical band
        builder.addButton(ButtonWidget.builder(chunkBandLabel(cfg), b -> {
            cfg.enableChunkVerticalBandCulling = !cfg.enableChunkVerticalBandCulling;
            b.setMessage(chunkBandLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.chunk_y_band")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Block entity culling
        builder.addButton(ButtonWidget.builder(blockEntityCullingLabel(cfg), b -> {
            cfg.enableBlockEntityCulling = !cfg.enableBlockEntityCulling;
            b.setMessage(blockEntityCullingLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.blockentity_culling")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Shared vertical band half-height slider
        builder.addSlider(new VerticalBandSlider(left, builder.getCurrentY(), width, 20, 
            cfg.verticalBandHalfHeight, cfg), 20);
        
        builder.addGap(8);
        
        // Dynamic vertical band toggle
        builder.addButton(ButtonWidget.builder(dynamicBandLabel(cfg), b -> {
            cfg.enableDynamicVerticalBand = !cfg.enableDynamicVerticalBand;
            b.setMessage(dynamicBandLabel(cfg));
            SeleniumClient.saveConfig();
        }).tooltip(Tooltip.of(Text.translatable("selenium.tooltip.dynamic_y_band")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
    }
    
    private Text chunkCullingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.chunk_culling", 
            Text.translatable(cfg.enableChunkCulling ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text chunkBandLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.chunk_y_band", 
            Text.translatable(cfg.enableChunkVerticalBandCulling ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text blockEntityCullingLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.blockentity_culling", 
            Text.translatable(cfg.enableBlockEntityCulling ? "selenium.common.on" : "selenium.common.off"));
    }
    
    private Text dynamicBandLabel(SeleniumConfig cfg) {
        return Text.translatable("selenium.settings.dynamic_y_band", 
            Text.translatable(cfg.enableDynamicVerticalBand ? "selenium.common.on" : "selenium.common.off"));
    }
}
