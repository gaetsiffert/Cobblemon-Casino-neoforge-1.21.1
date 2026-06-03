package net.andrespr.casinorocket.screen.custom.common;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.network.c2s.common.DoWithdrawC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.custom.CasinoMachineScreen;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class WithdrawScreen extends CasinoMachineScreen<WithdrawScreenHandler> {

    private CommonButton withdrawButton;
    private long balance = 0L;

    public WithdrawScreen(WithdrawScreenHandler handler, Inventory inv, Component title) {
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

        this.withdrawButton = ModButtons.doWithdraw(baseX, baseY, 46, 2, b -> onDoWithdrawPressed());
        this.addRenderableWidget(this.withdrawButton);
        updateWithdrawButtonState();
    }

    private void onDoWithdrawPressed() {
        if (minecraft != null && minecraft.player != null) {
            if (this.menu instanceof IMachineBoundHandler m) {
                CasinoRocketPackets.sendToServer(new DoWithdrawC2SPayload(m.getMachineKey(), m.getMachinePos()));
            }
        }
    }

    // === BACKGROUND ===
    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.WITHDRAW_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) /2;

        context.blit(ModGuiTextures.WITHDRAW_GUI, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics ctx, int mouseX, int mouseY) { }

    @Override
    public void containerTick() {
        super.containerTick();
        updateWithdrawButtonState();
    }

    // === GETTERS / UPDATERS ===

    public void updateBalance(long amount) {
        this.balance = amount;
    }

    private void updateWithdrawButtonState() {
        if (this.withdrawButton != null) {
            this.withdrawButton.active = balance > 0;
        }
    }

}

