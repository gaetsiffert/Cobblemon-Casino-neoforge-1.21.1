package net.andrespr.casinorocket.screen.custom.chip_table;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.games.chip_table.ChipTableConversionMode;
import net.andrespr.casinorocket.games.chip_table.ChipTableExchange;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.andrespr.casinorocket.network.c2s.chip_table.ChipTableConvertC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class ChipTableScreen extends AbstractContainerScreen<ChipTableScreenHandler> {

    private static final int VALUE_PANEL_WIDTH = 122;
    private static final int VALUE_PANEL_ROW_HEIGHT = 18;
    private static final int BUTTON_ROW_X = 7;
    private static final int BUTTON_ROW_Y = 86;
    private static final int MODE_BUTTON_WIDTH = 99;
    private static final int CONVERT_BUTTON_WIDTH = 59;
    private static final int BUTTON_GAP = 2;
    private static final int LARGE_BUTTON_SOURCE_WIDTH = 82;

    private ChipTableConversionMode selectedMode = ChipTableConversionMode.CURRENCY_TO_CHIPS;
    private CommonButton modeButton;
    private EditBox cobbledollarAmountBox;

    public ChipTableScreen(ChipTableScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = 174;
        this.imageHeight = handler.isWalletMode() ? 171 : 199;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = 7;
        titleLabelY = this.menu.isWalletMode() ? 3 : 7;
        inventoryLabelX = 7;
        inventoryLabelY = this.menu.isWalletMode() ? 79 : 102;

        if (!this.menu.isWalletMode()) {
            int baseX = (width - imageWidth) / 2;
            int baseY = (height - imageHeight) / 2;
            this.modeButton = new CommonButton(baseX + BUTTON_ROW_X, baseY + BUTTON_ROW_Y,
                    MODE_BUTTON_WIDTH, 12, LARGE_BUTTON_SOURCE_WIDTH, ModGuiTextures.BTN_LARGE, b -> cycleMode(), modeText());
            this.addRenderableWidget(this.modeButton);
            this.addRenderableWidget(new CommonButton(baseX + BUTTON_ROW_X + MODE_BUTTON_WIDTH + BUTTON_GAP, baseY + BUTTON_ROW_Y,
                    CONVERT_BUTTON_WIDTH, 12, ModGuiTextures.BTN_MEDIUM,
                    b -> CasinoRocketPackets.sendToServer(new ChipTableConvertC2SPayload(this.menu.getTablePos(), this.selectedMode,
                            parseCobbledollarAmount())),
                    Component.translatable("button.casinorocket.convert")));

            this.cobbledollarAmountBox = new EditBox(this.font, baseX + 68, baseY + 34, 99, 12,
                    Component.translatable("gui.casinorocket.chip_table.cobbledollar_amount"));
            this.cobbledollarAmountBox.setMaxLength(18);
            this.cobbledollarAmountBox.setFilter(value -> value.matches("\\d{0,18}"));
            this.addRenderableWidget(this.cobbledollarAmountBox);
            updateCobbledollarAmountBox();
        }
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, this.menu.isWalletMode() ? ModGuiTextures.CHIP_TABLE_GUI : ModGuiTextures.CHIP_TABLE_EXCHANGE_GUI);

        context.blit(this.menu.isWalletMode() ? ModGuiTextures.CHIP_TABLE_GUI : ModGuiTextures.CHIP_TABLE_EXCHANGE_GUI, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        context.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFFFFF, false);
        if (!this.menu.isWalletMode()) {
            if (this.selectedMode == ChipTableConversionMode.FROM_COBBLEDOLLAR) {
                context.drawString(this.font, Component.translatable("gui.casinorocket.chip_table.cobbledollar_amount"),
                        7, 37, 0xFFFFFF, false);
            }
            renderCurrencyValuePanel(context);
        }
    }

    public List<Rect2i> getCurrencyValuePanelArea() {
        List<ChipTableExchange.ValueEntry> entries = this.menu.getCurrencyValueEntries();
        if (this.menu.isWalletMode() || entries.isEmpty()) {
            return List.of();
        }

        int panelX = getCurrencyValuePanelX();
        int panelY = getCurrencyValuePanelY();
        int panelHeight = getCurrencyValuePanelHeight(entries.size());
        return List.of(new Rect2i(this.leftPos + panelX - 2, this.topPos + panelY - 2,
                VALUE_PANEL_WIDTH + 2, panelHeight + 2));
    }

    private void cycleMode() {
        ChipTableConversionMode[] modes = ChipTableConversionMode.values();
        int next = this.selectedMode.ordinal();
        do {
            next = (next + 1) % modes.length;
            this.selectedMode = ChipTableConversionMode.byOrdinal(next);
        } while (!isModeVisible(this.selectedMode));

        if (this.modeButton != null) {
            this.modeButton.setMessage(modeText());
        }
        updateCobbledollarAmountBox();
    }

    private Component modeText() {
        return Component.translatable("gui.casinorocket.chip_table.mode." + this.selectedMode.name().toLowerCase());
    }

    private boolean isModeVisible(ChipTableConversionMode mode) {
        return this.menu.isCobbledollarsAvailable()
                || (mode != ChipTableConversionMode.TO_COBBLEDOLLAR && mode != ChipTableConversionMode.FROM_COBBLEDOLLAR);
    }

    private long parseCobbledollarAmount() {
        if (this.selectedMode != ChipTableConversionMode.FROM_COBBLEDOLLAR || this.cobbledollarAmountBox == null) {
            return 0;
        }

        String value = this.cobbledollarAmountBox.getValue();
        if (value.isBlank()) return 0;

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }

    private void updateCobbledollarAmountBox() {
        if (this.cobbledollarAmountBox == null) return;

        boolean visible = this.selectedMode == ChipTableConversionMode.FROM_COBBLEDOLLAR;
        this.cobbledollarAmountBox.visible = visible;
        this.cobbledollarAmountBox.active = visible;
        if (!visible) {
            this.cobbledollarAmountBox.setFocused(false);
        }
    }

    private void renderCurrencyValuePanel(GuiGraphics context) {
        List<ChipTableExchange.ValueEntry> entries = this.menu.getCurrencyValueEntries();
        if (entries.isEmpty()) return;

        int panelHeight = getCurrencyValuePanelHeight(entries.size());
        int panelX = getCurrencyValuePanelX();
        int panelY = getCurrencyValuePanelY();
        context.fill(panelX - 2, panelY - 2, panelX + VALUE_PANEL_WIDTH, panelY + panelHeight, 0xDD101018);
        context.fill(panelX - 2, panelY - 2, panelX + VALUE_PANEL_WIDTH, panelY - 1, 0xAAFFD96A);
        context.fill(panelX - 2, panelY + panelHeight - 1, panelX + VALUE_PANEL_WIDTH, panelY + panelHeight, 0xAAFFD96A);
        context.drawString(this.font, Component.translatable("gui.casinorocket.chip_table.currency_values"),
                panelX + 5, panelY + 4, 0xFFD96A, false);

        int rowY = panelY + 17;
        for (ChipTableExchange.ValueEntry entry : entries) {
            context.renderItem(entry.stack(), panelX + 5, rowY);
            context.drawString(this.font, Component.translatable("gui.casinorocket.chip_amount",
                            TextUtils.formatWithCommas(entry.value())),
                    panelX + 26, rowY + 4, 0xFFFFFF, false);
            rowY += VALUE_PANEL_ROW_HEIGHT;
        }
    }

    private int getCurrencyValuePanelX() {
        int panelX = imageWidth + 8;
        if (this.leftPos + panelX + VALUE_PANEL_WIDTH > this.width) {
            panelX = -VALUE_PANEL_WIDTH - 8;
        }
        if (this.leftPos + panelX < 0) {
            panelX = imageWidth - VALUE_PANEL_WIDTH - 6;
        }
        return panelX;
    }

    private int getCurrencyValuePanelY() {
        return 13;
    }

    private int getCurrencyValuePanelHeight(int entryCount) {
        return 17 + entryCount * VALUE_PANEL_ROW_HEIGHT + 4;
    }

}
