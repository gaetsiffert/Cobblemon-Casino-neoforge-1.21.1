package net.andrespr.casinorocket.screen.custom.chip_table;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.games.chip_table.ChipTableConversionMode;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.andrespr.casinorocket.network.c2s.chip_table.ChipTableConvertC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ChipTableScreen extends AbstractContainerScreen<ChipTableScreenHandler> {

    private ChipTableConversionMode selectedMode = ChipTableConversionMode.CURRENCY_TO_CHIPS;
    private CommonButton modeButton;

    public ChipTableScreen(ChipTableScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = 174;
        this.imageHeight = handler.isWalletMode() ? 171 : 199;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = 7;
        titleLabelY = 3;
        inventoryLabelX = 7;
        inventoryLabelY = this.menu.isWalletMode() ? 79 : 102;

        if (!this.menu.isWalletMode()) {
            int baseX = (width - imageWidth) / 2;
            int baseY = (height - imageHeight) / 2;
            this.modeButton = new CommonButton(baseX + 7, baseY + 86, 82, 12, ModGuiTextures.BTN_LARGE, b -> cycleMode(), modeText());
            this.addRenderableWidget(this.modeButton);
            this.addRenderableWidget(new CommonButton(baseX + 91, baseY + 86, 82, 12, ModGuiTextures.BTN_LARGE,
                    b -> CasinoRocketPackets.sendToServer(new ChipTableConvertC2SPayload(this.menu.getTablePos(), this.selectedMode)),
                    Component.translatable("button.casinorocket.convert")));
        }
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
    }

    private void cycleMode() {
        int next = (this.selectedMode.ordinal() + 1) % ChipTableConversionMode.values().length;
        this.selectedMode = ChipTableConversionMode.byOrdinal(next);
        if (this.modeButton != null) {
            this.modeButton.setMessage(modeText());
        }
    }

    private Component modeText() {
        return Component.translatable("gui.casinorocket.chip_table.mode." + this.selectedMode.name().toLowerCase());
    }

}
