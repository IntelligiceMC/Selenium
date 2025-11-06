package com.itarqos.selenium.client.screen.components;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TabButton extends ButtonWidget {
    private final Object category;
    private final TabOwner owner;

    public interface TabOwner {
        Object getCurrentCategory();
        TextRenderer getTextRenderer();
    }

    public TabButton(TabOwner owner, int x, int y, int width, int height, Object category, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.owner = owner;
        this.category = category;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        boolean selected = owner.getCurrentCategory().equals(this.category);
        int x = this.getX();
        int y = this.getY();
        int w = this.getWidth();
        int h = this.getHeight();

        int base = selected ? 0xFF2E2E2E : 0xFF1E1E1E;
        int hover = selected ? 0xFF3A3A3A : 0xFF2A2A2A;
        int border = selected ? 0xFF7ABFFF : 0xFF3A3A3A;
        int fill = this.isMouseOver(mouseX, mouseY) ? hover : base;

        // Button background
        context.fill(x, y, x + w, y + h, fill);
        // Top accent border for selected, subtle border otherwise
        context.fill(x, y, x + w, y + 1, border);
        context.fill(x, y + h - 1, x + w, y + h, 0xFF000000);
        context.fill(x, y, x + 1, y + h, 0xFF000000);
        context.fill(x + w - 1, y, x + w, y + h, 0xFF000000);

        // Text
        int color = this.active ? 0xFFFFFF : 0xFF888888;
        Text msg = this.getMessage();
        int tw = owner.getTextRenderer().getWidth(msg);
        context.drawText(owner.getTextRenderer(), msg, x + (w - tw) / 2, y + (h - 8) / 2, color, false);
    }
}
