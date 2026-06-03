package net.andrespr.casinorocket.screen.custom.chip_table;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ChipTableScreen extends AbstractContainerScreen<ChipTableScreenHandler> {

    public ChipTableScreen(ChipTableScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = 174;
        this.imageHeight = 171;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = 7;
        titleLabelY = 3;
        inventoryLabelX = 7;
        inventoryLabelY = 79;
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.CHIP_TABLE_GUI);

        context.blit(ModGuiTextures.CHIP_TABLE_GUI, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFFFFF, false);
        context.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFFFFFF, false);
    }

}

