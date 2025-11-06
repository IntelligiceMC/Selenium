package com.itarqos.selenium.client.screen.tabs;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ParticlesTab implements SettingsTab {
    
    @Override
    public void buildWidgets(TabWidgetBuilder builder) {
        SeleniumConfig cfg = builder.getConfig();
        int left = builder.getLeft();
        int width = builder.getWidth();
        
        // Global disable all particles
        builder.addButton(ButtonWidget.builder(Text.of("Disable All Particles: " + (cfg.disableAllParticles ? "On" : "Off")), b -> {
            cfg.disableAllParticles = !cfg.disableAllParticles;
            b.setMessage(Text.of("Disable All Particles: " + (cfg.disableAllParticles ? "On" : "Off")));
            SeleniumClient.saveConfig();
            builder.refreshScreen();
        }).tooltip(Tooltip.of(Text.of("Blocks all particle spawns when On")))
          .dimensions(left, builder.getCurrentY(), width, 20).build(), 20);
        
        builder.addGap(4);
        
        // Individual particle toggles
        for (Identifier id : Registries.PARTICLE_TYPE.getIds()) {
            String idStr = id.toString();
            String name = id.getPath().replace('_', ' ');
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            boolean disabled = cfg.disabledParticleIds.contains(idStr);
            Text label = Text.of(name + ": " + (disabled ? "Off" : "On"));
            String finalName = name;
            
            ButtonWidget.Builder btnBuilder = ButtonWidget.builder(label, b -> {
                if (cfg.disabledParticleIds.contains(idStr)) {
                    cfg.disabledParticleIds.remove(idStr);
                } else {
                    cfg.disabledParticleIds.add(idStr);
                }
                b.setMessage(Text.of(finalName + ": " + (cfg.disabledParticleIds.contains(idStr) ? "Off" : "On")));
                SeleniumClient.saveConfig();
            }).tooltip(Tooltip.of(Text.of(idStr)));
            
            ButtonWidget btn = btnBuilder.dimensions(left, builder.getCurrentY(), width, 20).build();
            if (cfg.disableAllParticles) btn.active = false; // disabled by global
            
            builder.addButton(btn, 20);
            builder.addGap(4);
        }
    }
}
