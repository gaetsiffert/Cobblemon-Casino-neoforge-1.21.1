package net.narrnouille.cobblemoncasino.screen.custom.common;

import com.mojang.blaze3d.systems.RenderSystem;
import net.narrnouille.cobblemoncasino.network.c2s.common.DoBetC2SPayload;
import net.narrnouille.cobblemoncasino.screen.ModGuiTextures;
import net.narrnouille.cobblemoncasino.screen.custom.CasinoMachineScreen;
import net.narrnouille.cobblemoncasino.screen.widget.CommonButton;
import net.narrnouille.cobblemoncasino.screen.widget.ModButtons;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
import net.narrnouille.cobblemoncasino.util.TextUtils;
import net.narrnouille.cobblemoncasino.network.CobblemonCasinoPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class BetScreen extends CasinoMachineScreen<BetScreenHandler> {

    private CommonButton betButton;
    private long currentTotal = 0L;

    public BetScreen(BetScreenHandler handler, Inventory inv, Component title) {
        super(handler, inv, title);
        this.imageWidth = 174;
        this.imageHeight = 166;
    }

    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();
        int baseX = (this.width - this.imageWidth) / 2;
        int baseY = (this.height - this.imageHeight) / 2;

        this.betButton = ModButtons.doBet(baseX, baseY, 46, 2, b -> onDoBetPressed());
        this.addRenderableWidget(ModButtons.back(baseX, baseY, 6, 2, b -> onBackPressed()));
        this.addRenderableWidget(this.betButton);
        updateBetButtonState();
    }

    private void onDoBetPressed() {
        if (minecraft != null && minecraft.player != null) {
            if (this.menu instanceof IMachineBoundHandler m) {
                CobblemonCasinoPackets.sendToServer(new DoBetC2SPayload(m.getMachineKey(), m.getMachinePos()));
            }
        }
    }

    // === BACKGROUND ===
    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.BET_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) /2;

        context.blit(ModGuiTextures.BET_GUI, x, y, 0, 0, imageWidth, imageHeight);

        context.blit(ModGuiTextures.CHIP, x + 112, y + 2, 0, 0, 12, 12, 12, 12);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        String formatted = TextUtils.formatCompact(currentTotal);

        int width = font.width(formatted);
        int drawX = Math.max(105 - width, 79);

        context.drawString(font, formatted, drawX, 4, 0x00AA00, true);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        updateBetButtonState();
    }

    // === GETTERS ===

    public void updateTotalAmount(long amount) {
        this.currentTotal = amount;
    }

    private void updateBetButtonState() {
        if (this.betButton != null) {
            this.betButton.active = currentTotal > 0;
        }
    }

}

