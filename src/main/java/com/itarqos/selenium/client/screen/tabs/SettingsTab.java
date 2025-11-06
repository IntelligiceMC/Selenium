package com.itarqos.selenium.client.screen.tabs;

import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;

public interface SettingsTab {
    
    /**
     * Build the widgets for this tab
     * @param builder The builder to add widgets to
     */
    void buildWidgets(TabWidgetBuilder builder);
    
    /**
     * Helper interface for building tab widgets
     */
    interface TabWidgetBuilder {
        SeleniumConfig getConfig();
        void addButton(ButtonWidget button, int height);
        void addSlider(SliderWidget slider, int height);
        void addGap(int height);
        int getLeft();
        int getWidth();
        int getCurrentY();
        void refreshScreen();
    }
}
