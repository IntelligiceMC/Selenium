package com.itarqos.selenium.client.screen;

import com.itarqos.selenium.client.SeleniumClient;
import com.itarqos.selenium.client.screen.components.TabButton;
import com.itarqos.selenium.client.screen.tabs.*;
import com.itarqos.selenium.config.SeleniumConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class SeleniumSettingsScreen extends Screen implements TabButton.TabOwner {
    private final Screen parent;
    private SeleniumConfig cfg;
    private Category category = Category.ENTITIES;
    private final Map<Category, SettingsTab> tabs = new HashMap<>();

    // Scrolling support
    private double scrollOffset = 0.0;
    private double maxScroll = 0.0;
    private static final int SCROLL_SPEED = 10;
    // Scrollbar interaction state (computed each render)
    private boolean draggingScrollbar = false;
    private double dragStartY = 0.0;
    private double dragStartScroll = 0.0;
    // Cache last-drawn scrollbar geometry for hit-testing
    private int sbX, sbY, sbH;
    private double sbThumbY, sbThumbH;

    private enum Category {
        ENTITIES, WORLD, RENDER, PARTICLES, ADVANCED, EXTRA
    }

    // Responsive layout helpers
    private int getTabsY() {
        return 36;
    }

    private int getTabGap() {
        return 6;
    }

    private int getTabHeight() {
        return 20;
    }

    private int getContentWidth() {
        return Math.max(160, Math.min(420, this.width - 16));
    }

    private int getContentLeft() {
        return Math.max(8, (this.width - getContentWidth()) / 2);
    }

    private int getPanelLeft() {
        return getContentLeft() - 6;
    }

    private int getPanelRight() {
        return getContentLeft() + getContentWidth() + 6;
    }

    private int getPanelTop() {
        return getTabsY() + getTabHeight() + 8;
    }

    private int getPanelBottom() {
        return this.height - 36;
    }

    @Override
    public Object getCurrentCategory() {
        return category;
    }

    @Override
    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    public SeleniumSettingsScreen(Screen parent) {
        super(Text.translatable("selenium.settings.title"));
        this.parent = parent;

        // Initialize tabs
        tabs.put(Category.ENTITIES, new EntitiesTab());
        tabs.put(Category.WORLD, new WorldTab());
        tabs.put(Category.RENDER, new RenderTab());
        tabs.put(Category.PARTICLES, new ParticlesTab());
        tabs.put(Category.ADVANCED, new AdvancedTab());
        tabs.put(Category.EXTRA, new ExtraTab());
    }

    @Override
    protected void init() {
        this.cfg = SeleniumClient.CONFIG;

        // Tabs (horizontal strip near top, dynamic sizing)
        int tabsY = getTabsY();
        int gap = getTabGap();
        int count = Category.values().length;
        int sidePad = 16;
        int tabW = Math.max(68, Math.min(140, (this.width - (sidePad * 2) - (gap * (count - 1))) / count));
        int tabH = getTabHeight();
        int totalW = (tabW * count) + (gap * (count - 1));
        int baseX = (this.width - totalW) / 2;
        addTab(baseX + (tabW + gap) * 0, tabsY, tabW, tabH, Category.ENTITIES, Text.translatable("selenium.settings.tab.entities"));
        addTab(baseX + (tabW + gap) * 1, tabsY, tabW, tabH, Category.WORLD, Text.translatable("selenium.settings.tab.world"));
        addTab(baseX + (tabW + gap) * 2, tabsY, tabW, tabH, Category.RENDER, Text.translatable("selenium.settings.tab.render"));
        addTab(baseX + (tabW + gap) * 3, tabsY, tabW, tabH, Category.PARTICLES, Text.literal("Particles"));
        addTab(baseX + (tabW + gap) * 4, tabsY, tabW, tabH, Category.ADVANCED, Text.translatable("selenium.settings.tab.advanced"));
        addTab(baseX + (tabW + gap) * 5, tabsY, tabW, tabH, Category.EXTRA, Text.translatable("selenium.settings.tab.extra"));

        // Content layout
        int marginTop = getPanelTop();
        int contentWidth = getContentWidth();
        int left = getContentLeft();
        int visibleTop = marginTop - 4;
        int visibleBottom = getPanelBottom() - 8; // Leave space for Done button

        // Build widgets using the current tab
        SettingsTab currentTab = tabs.get(category);
        if (currentTab != null) {
            TabWidgetBuilderImpl builder = new TabWidgetBuilderImpl(left, contentWidth, marginTop, visibleTop, visibleBottom);
            currentTab.buildWidgets(builder);
            int contentBottom = builder.getCurrentY() + (int) scrollOffset;
            maxScroll = Math.max(0, contentBottom - visibleBottom);
            scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
        }

        // Done button (bottom) - fixed position, not affected by scroll
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("selenium.settings.done"), b -> {
            SeleniumClient.saveConfig();
            MinecraftClient.getInstance().setScreen(parent);
        }).dimensions(this.width / 2 - 100, this.height - 28, 200, 20).build());
    }

    private class TabWidgetBuilderImpl implements SettingsTab.TabWidgetBuilder {
        private final int left;
        private final int width;
        private int currentY;
        private final int visibleTop;
        private final int visibleBottom;

        public TabWidgetBuilderImpl(int left, int width, int startY, int visibleTop, int visibleBottom) {
            this.left = left;
            this.width = width;
            this.currentY = startY - (int) scrollOffset;
            this.visibleTop = visibleTop;
            this.visibleBottom = visibleBottom;
        }

        @Override
        public SeleniumConfig getConfig() {
            return cfg;
        }

        @Override
        public void addButton(ButtonWidget button, int height) {
            addIfVisible(button, currentY, height, visibleTop, visibleBottom);
            currentY += height;
        }

        @Override
        public void addSlider(SliderWidget slider, int height) {
            addIfVisible(slider, currentY, height, visibleTop, visibleBottom);
            currentY += height;
        }

        @Override
        public void addGap(int height) {
            currentY += height;
        }

        @Override
        public int getLeft() {
            return left;
        }

        @Override
        public int getWidth() {
            return width;
        }

        @Override
        public int getCurrentY() {
            return currentY;
        }

        @Override
        public void refreshScreen() {
            clearAndInit();
        }
    }

    private void addIfVisible(ButtonWidget widget, int y, int h, int visibleTop, int visibleBottom) {
        if (y + h >= visibleTop && y <= visibleBottom) {
            SeleniumSettingsScreen.this.addDrawableChild(widget);
        }
    }

    private void addIfVisible(SliderWidget widget, int y, int h, int visibleTop, int visibleBottom) {
        if (y + h >= visibleTop && y <= visibleBottom) {
            SeleniumSettingsScreen.this.addDrawableChild(widget);
        }
    }

    private void addTab(int x, int y, int w, int h, Category cat, Text label) {
        this.addDrawableChild(new TabButton(this, x, y, w, h, cat, label, b -> {
            this.category = cat;
            this.scrollOffset = 0.0; // Reset scroll when changing tabs
            this.clearAndInit();
        }));
    }

    @Override
    public void close() {
        SeleniumClient.saveConfig();
        MinecraftClient.getInstance().setScreen(parent);
    }

    @Override
    public void resize(MinecraftClient mc, int width, int height) {
        // Reset scroll when the window size changes to avoid widgets going out of bounds
        this.scrollOffset = 0.0;
        super.resize(mc, width, height);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        // Scroll the content
        double newOffset = scrollOffset - (verticalAmount * SCROLL_SPEED);
        scrollOffset = Math.max(0, Math.min(maxScroll, newOffset));

        // Rebuild widgets with new scroll offset
        this.clearAndInit();
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (maxScroll > 0) {
            // Hit-test scrollbar track
            if (mouseX >= sbX && mouseX <= sbX + 6 && mouseY >= sbY && mouseY <= sbY + sbH) {
                // If inside thumb, start dragging; else jump to position
                if (mouseY >= sbThumbY && mouseY <= sbThumbY + sbThumbH) {
                    draggingScrollbar = true;
                    dragStartY = mouseY;
                    dragStartScroll = scrollOffset;
                } else {
                    // Jump: center thumb around click
                    double range = sbH - sbThumbH;
                    double t = (mouseY - sbY - sbThumbH * 0.5);
                    t = Math.max(0, Math.min(range, t));
                    scrollOffset = Math.max(0, Math.min(maxScroll, (t / range) * maxScroll));
                    this.clearAndInit();
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (draggingScrollbar && maxScroll > 0) {
            // Map drag to scroll offset
            double range = sbH - sbThumbH;
            double initialThumb = (dragStartScroll / maxScroll) * range;
            double newThumb = Math.max(0, Math.min(range, initialThumb + (mouseY - dragStartY)));
            scrollOffset = Math.max(0, Math.min(maxScroll, (newThumb / range) * maxScroll));
            this.clearAndInit();
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingScrollbar) {
            draggingScrollbar = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render background
        this.renderBackground(context, mouseX, mouseY, delta);

        // Header title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);

        // Content panel background
        int panelTop = getPanelTop();
        int panelBottom = getPanelBottom();
        int panelLeft = getPanelLeft();
        int panelRight = getPanelRight();
        context.fill(panelLeft, panelTop, panelRight, panelBottom, 0x7F000000);
        context.fill(panelLeft, panelTop, panelRight, panelTop + 1, 0xFF000000);
        context.fill(panelLeft, panelBottom - 1, panelRight, panelBottom, 0xFF000000);
        context.fill(panelLeft, panelTop, panelLeft + 1, panelBottom, 0xFF000000);
        context.fill(panelRight - 1, panelTop, panelRight, panelBottom, 0xFF000000);

        // Render widgets
        super.render(context, mouseX, mouseY, delta);

        // Draw scrollbar if needed
        if (maxScroll > 0) {
            drawScrollbar(context);
        }
    }

    private void drawScrollbar(DrawContext context) {
        // Match content panel geometry from render()
        int panelTop = getPanelTop();
        int panelBottom = getPanelBottom();
        int panelLeft = getPanelLeft();
        int panelRight = getPanelRight();

        int scrollbarX = panelRight - 10; // inside panel
        int scrollbarY = panelTop;
        int scrollbarHeight = panelBottom - panelTop;

        // Background track
        context.fill(scrollbarX, scrollbarY, scrollbarX + 6, scrollbarY + scrollbarHeight, 0x80000000);

        // Calculate thumb position and size
        double contentHeight = maxScroll + scrollbarHeight;
        double thumbHeight = Math.max(20, scrollbarHeight * scrollbarHeight / contentHeight);
        double thumbY = scrollbarY + (scrollOffset / maxScroll) * (scrollbarHeight - thumbHeight);

        // Thumb
        context.fill(scrollbarX, (int) thumbY, scrollbarX + 6, (int) (thumbY + thumbHeight), 0xFFAAAAAA);
        // Cache geometry
        this.sbX = scrollbarX;
        this.sbY = scrollbarY;
        this.sbH = scrollbarHeight;
        this.sbThumbY = thumbY;
        this.sbThumbH = thumbHeight;
    }
}