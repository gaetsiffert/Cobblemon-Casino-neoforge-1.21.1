package net.andrespr.casinorocket.screen.custom.slot;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.network.c2s.slots.ChangeBetBaseC2SPayload;
import net.andrespr.casinorocket.network.c2s.slots.ChangeLinesModeC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.custom.CasinoMachineScreen;
import net.andrespr.casinorocket.screen.opening.MouseRestore;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.screen.widget.SlotButton;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.andrespr.casinorocket.util.TextUtils;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SlotMachineMenuScreen extends CasinoMachineScreen<SlotMachineMenuScreenHandler> {

    private long balance = 0L;
    private int betBase = 10;
    private int linesMode = 1;

    private SlotButton plusButton;
    private SlotButton subtractButton;
    private SlotButton mode1Button;
    private SlotButton mode2Button;
    private SlotButton mode3Button;

    public SlotMachineMenuScreen(SlotMachineMenuScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = 200;
        this.imageHeight = 236;
    }

    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();

        int baseX = (this.width - this.imageWidth) / 2;
        int baseY = (this.height - this.imageHeight) / 2;

        this.plusButton = ModButtons.plus(baseX, baseY, 67, 36, b -> onPlusPressed());
        this.subtractButton = ModButtons.subtract(baseX, baseY, 6, 36, b -> onSubtractPressed());
        this.mode1Button = ModButtons.mode1(baseX, baseY, 6, 67, b -> onMode1Pressed());
        this.mode2Button = ModButtons.mode2(baseX, baseY, 6, 82, b -> onMode2Pressed());
        this.mode3Button = ModButtons.mode3(baseX, baseY, 6, 97, b -> onMode3Pressed());

        this.addRenderableWidget(this.plusButton);
        this.addRenderableWidget(this.subtractButton);
        this.addRenderableWidget(this.mode1Button);
        this.addRenderableWidget(this.mode2Button);
        this.addRenderableWidget(this.mode3Button);

        updateSettings(
                this.menu.getInitialBalance(),
                this.menu.getInitialBetBase(),
                this.menu.getInitialLinesMode()
        );

    }

    // === BUTTONS ===
    private void onPlusPressed() {
        if (minecraft != null && minecraft.player != null) {
            CasinoRocketPackets.sendToServer(new ChangeBetBaseC2SPayload(true));
        }
    }

    private void onSubtractPressed() {
        if (minecraft != null && minecraft.player != null) {
            CasinoRocketPackets.sendToServer(new ChangeBetBaseC2SPayload(false));
        }
    }

    private void onMode1Pressed() {
        sendMode(1);
    }

    private void onMode2Pressed() {
        sendMode(2);
    }

    private void onMode3Pressed() {
        sendMode(3);
    }

    private void sendMode(int mode) {
        if (minecraft != null && minecraft.player != null) {
            CasinoRocketPackets.sendToServer(new ChangeLinesModeC2SPayload(mode));
        }
    }

    // === SCREEN TICK ===
    @Override
    protected void containerTick() {
        super.containerTick();
        MouseRestore.applyIfPending(minecraft);
    }

    // === BACKGROUND ===
    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.SLOT_MACHINE_MENU_GUI);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) /2;

        context.blit(ModGuiTextures.SLOT_MACHINE_MENU_GUI, x, y, 0, 0, imageWidth, imageHeight);

        if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive())
        { context.blit(ModGuiTextures.COBBLEDOLLARS, x + 53, y + 36, 0, 0, 12, 12, 12,12); }
        if (CasinoRocket.CONFIG.generalConfig.isRelicCoinActive())
        { context.blit(ModGuiTextures.RELIC_COIN, x + 53, y + 36, 0, 0, 12, 12, 12,12); }
        if (CasinoRocket.CONFIG.generalConfig.isDiamondActive())
        { context.blit(ModGuiTextures.DIAMOND, x + 53, y + 36, 0, 0, 12, 12, 12, 12); }
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        context.drawString(font, Component.translatable("gui.casinorocket.slot_machine.settings_bet_amount"),
                7, 25, 0xFFFFFF, true);
        context.drawString(font, Component.translatable("gui.casinorocket.slot_machine.settings_lines"),
                7, 56, 0xFFFFFF, true);
        context.drawString(font, Component.translatable("gui.casinorocket.slot_machine.settings_one_line"),
                23, 70, 0xFFFFFF, true);
        context.drawString(font, Component.translatable("gui.casinorocket.slot_machine.settings_three_lines"),
                23, 85, 0xFFFFFF, true);
        context.drawString(font, Component.translatable("gui.casinorocket.slot_machine.settings_five_lines"),
                23, 100, 0xFFFFFF, true);

        int mult = SlotMachineConstants.getBetMultiplierForMode(linesMode);
        int finalBet = betBase * mult;
        String formatted = TextUtils.formatCompact(finalBet);
        context.drawString(font, formatted, 23, 38, 0x00AA00, true);

        int srcY = (linesMode - 1) * 30;
        context.blit(ModGuiTextures.LINES_LAYOUT, 21,112, 0, srcY,
                43, 30, 43, 30 * 3);

    }

    // === UPDATERS ===
    public void updateSettings(long balance, int base, int lines) {
        this.balance = balance;
        this.betBase = base;
        this.linesMode = lines;

        updateButtonStates();
        updateModeButtons();
    }

    private void updateButtonStates() {
        int index = SlotMachineConstants.betValues().indexOf(betBase);

        subtractButton.active = index > 0;
        plusButton.active = index < SlotMachineConstants.betValues().size() - 1;

        subtractButton.setForcedPressed(index == 0);
        plusButton.setForcedPressed(index == SlotMachineConstants.betValues().size() - 1);
    }

    private void updateModeButtons() {
        mode1Button.setForcedPressed(linesMode == 1);
        mode2Button.setForcedPressed(linesMode == 2);
        mode3Button.setForcedPressed(linesMode == 3);

        mode1Button.active = linesMode != 1;
        mode2Button.active = linesMode != 2;
        mode3Button.active = linesMode != 3;
    }

}

