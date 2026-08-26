package com.example.ourmod.gui;

import com.example.ourmod.module.Category;
import com.example.ourmod.module.Module;
import com.example.ourmod.module.ModuleManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ModMenuScreen extends Screen {
    private static final int BG = 0xCC11131A;
    private static final int PANEL = 0xF0181B24;
    private static final int PANEL_2 = 0xF01E212B;
    private static final int BORDER = 0xFF2B3040;
    private static final int TEXT = 0xFFF2F3F7;
    private static final int MUTED = 0xFF8D93A4;
    private static final int ACCENT = 0xFF8B7CFF;
    private static final int ACCENT_DARK = 0xFF3A345F;

    private Category selected = Category.COMBAT;
    private TextFieldWidget search;
    private long openedAt;

    public ModMenuScreen() {
        super(Text.literal("OurMod"));
    }

    @Override
    protected void init() {
        openedAt = System.currentTimeMillis();
        search = new TextFieldWidget(textRenderer, width - 210, 15, 175, 22, Text.literal("Search"));
        search.setMaxLength(40);
        search.setDrawsBackground(false);
        search.setEditableColor(TEXT);
        search.setTextColor(TEXT);
        addDrawableChild(search);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int left = Math.max(30, (width - 940) / 2);
        int top = Math.max(25, (height - 570) / 2);
        int right = Math.min(width - 30, left + 940);
        int bottom = Math.min(height - 25, top + 570);

        context.fill(0, 0, width, height, BG);
        drawPanel(context, left, top, right, bottom, PANEL);

        float open = MathHelper.clamp((System.currentTimeMillis() - openedAt) / 140f, 0f, 1f);
        int titleY = top + 18;
        context.drawText(textRenderer, Text.literal("OurMod"), left + 22, titleY, TEXT, true);
        context.drawText(textRenderer, Text.literal("1.21.4"), left + 88, titleY + 1, MUTED, false);

        int sideX = left + 12;
        int sideTop = top + 55;
        int sideW = 170;
        int contentX = sideX + sideW + 16;
        int contentTop = top + 55;

        drawSidebar(context, sideX, sideTop, sideW, bottom - 16, mouseX, mouseY);

        context.drawText(textRenderer, Text.literal(selected.title()), contentX, contentTop + 4, TEXT, true);
        context.drawText(textRenderer, Text.literal("Clean, fast and configurable"), contentX, contentTop + 20, MUTED, false);

        drawCards(context, contentX, contentTop + 48, right - 16, bottom - 16, mouseX, mouseY, open);

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawSidebar(DrawContext ctx, int x, int y, int w, int h, int mouseX, int mouseY) {
        drawPanel(ctx, x, y, x + w, y + h, 0xD8141720);
        int itemY = y + 12;
        for (Category category : Category.values()) {
            boolean active = category == selected;
            boolean hovered = inside(mouseX, mouseY, x + 8, itemY, x + w - 8, itemY + 34);
            if (active) {
                drawPanel(ctx, x + 8, itemY, x + w - 8, itemY + 34, 0xFF27233D);
                ctx.fill(x + 8, itemY, x + 10, itemY + 34, ACCENT);
            } else if (hovered) {
                drawPanel(ctx, x + 8, itemY, x + w - 8, itemY + 34, 0xFF20232D);
            }
            ctx.drawText(textRenderer, Text.literal(icon(category) + "  " + category.title()), x + 20, itemY + 10, active ? TEXT : 0xFFBEC2CF, false);
            itemY += 40;
        }

        int userY = y + h - 58;
        ctx.drawText(textRenderer, Text.literal("●"), x + 18, userY + 6, 0xFF7DFFB2, false);
        ctx.drawText(textRenderer, Text.literal("Developer"), x + 34, userY, TEXT, false);
        ctx.drawText(textRenderer, Text.literal("Local profile"), x + 34, userY + 14, MUTED, false);
    }

    private void drawCards(DrawContext ctx, int x, int y, int right, int bottom, int mouseX, int mouseY, float open) {
        List<Module> modules = new ArrayList<>();
        String query = search == null ? "" : search.getText().trim().toLowerCase();
        for (Module module : ModuleManager.modules()) {
            if (module.category() == selected && (query.isEmpty() || module.name().toLowerCase().contains(query))) {
                modules.add(module);
            }
        }

        int gap = 10;
        int cardW = (right - x - gap) / 2;
        int cardH = 78;
        int col = 0;
        int row = 0;
        for (Module module : modules) {
            int cx = x + col * (cardW + gap);
            int cy = y + row * (cardH + gap);
            if (cy + cardH > bottom) break;
            boolean hovered = inside(mouseX, mouseY, cx, cy, cx + cardW, cy + cardH);
            drawPanel(ctx, cx, cy, cx + cardW, cy + cardH, hovered ? 0xFF20242F : PANEL_2);
            drawOutline(ctx, cx, cy, cx + cardW, cy + cardH, hovered ? ACCENT_DARK : BORDER);

            ctx.drawText(textRenderer, Text.literal(module.name()), cx + 12, cy + 12, TEXT, false);
            ctx.drawText(textRenderer, Text.literal(module.enabled() ? "Enabled" : "Disabled"), cx + 12, cy + 31, module.enabled() ? 0xFF9E91FF : MUTED, false);
            ctx.drawText(textRenderer, Text.literal(module.description()), cx + 12, cy + 48, MUTED, false);
            drawToggle(ctx, cx + cardW - 34, cy + 29, module.enabled());

            col++;
            if (col == 2) { col = 0; row++; }
        }
    }

    private void drawToggle(DrawContext ctx, int x, int y, boolean enabled) {
        drawPanel(ctx, x - 2, y - 7, x + 27, y + 9, enabled ? 0xFF6256C6 : 0xFF3A3E49);
        int knobX = enabled ? x + 18 : x + 4;
        ctx.fill(knobX, y - 4, knobX + 8, y + 4, 0xFFF4F5F8);
    }

    private void drawPanel(DrawContext ctx, int x1, int y1, int x2, int y2, int color) {
        ctx.fill(x1, y1, x2, y2, color);
    }

    private void drawOutline(DrawContext ctx, int x1, int y1, int x2, int y2, int color) {
        ctx.fill(x1, y1, x2, y1 + 1, color);
        ctx.fill(x1, y2 - 1, x2, y2, color);
        ctx.fill(x1, y1, x1 + 1, y2, color);
        ctx.fill(x2 - 1, y1, x2, y2, color);
    }

    private String icon(Category category) {
        return switch (category) {
            case COMBAT -> "⚔";
            case MOVEMENT -> "✦";
            case RENDER -> "◈";
            case PLAYER -> "◉";
            case DISPLAY -> "▣";
        };
    }

    private boolean inside(double mouseX, double mouseY, int x1, int y1, int x2, int y2) {
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int left = Math.max(30, (width - 940) / 2);
        int top = Math.max(25, (height - 570) / 2);
        int sideX = left + 12;
        int itemY = top + 67;
        for (Category category : Category.values()) {
            if (inside(mouseX, mouseY, sideX + 8, itemY, sideX + 162, itemY + 34)) {
                selected = category;
                return true;
            }
            itemY += 40;
        }

        int contentX = sideX + 170 + 16;
        int y = top + 55 + 48;
        int right = Math.min(width - 30, left + 940) - 16;
        int gap = 10;
        int cardW = (right - contentX - gap) / 2;
        int cardH = 78;
        int col = 0;
        int row = 0;
        for (Module module : ModuleManager.modules()) {
            if (module.category() != selected) continue;
            int cx = contentX + col * (cardW + gap);
            int cy = y + row * (cardH + gap);
            if (inside(mouseX, mouseY, cx, cy, cx + cardW, cy + cardH)) {
                module.toggle();
                return true;
            }
            col++;
            if (col == 2) { col = 0; row++; }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
