package net.narrnouille.cobblemoncasino.screen.widget;

import net.narrnouille.cobblemoncasino.screen.ModGuiTextures;
import net.narrnouille.cobblemoncasino.sound.ModSounds;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class BackButton extends Button {

    private static final int WIDTH = 25;
    private static final int HEIGHT = 12;

    public BackButton(int x, int y, OnPress onPress) {
        super(x, y, WIDTH, HEIGHT, Component.translatable("button.cobblemoncasino.back"), onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        int vOffset = this.active && this.isHovered() ? HEIGHT : 0;
        context.blit(ModGuiTextures.BTN_BACK, this.getX(), this.getY(), 0, vOffset,
                WIDTH, HEIGHT, WIDTH, HEIGHT * 2);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(ModSounds.BUTTON, 1.0F));
    }
}
