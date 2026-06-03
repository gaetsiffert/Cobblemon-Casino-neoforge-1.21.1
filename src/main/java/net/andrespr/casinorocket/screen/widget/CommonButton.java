package net.andrespr.casinorocket.screen.widget;

import net.andrespr.casinorocket.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CommonButton extends Button {

    private final ResourceLocation texture;
    private final int texWidth;
    private final int texHeight;
    private final int stateHeight;

    public CommonButton(int x, int y, int width, int height, ResourceLocation texture, OnPress onPress, Component text) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.texWidth = width;
        this.stateHeight = height;
        this.texHeight = height * 3;
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        int vOffset;

        if (!this.active) {
            vOffset = 0;
        } else if (this.isHovered()) {
            vOffset = this.stateHeight * 2;
        } else {
            vOffset = this.stateHeight;
        }

        context.blit(this.texture, this.getX(), this.getY(), 0, vOffset,
                this.getWidth(), this.stateHeight, this.texWidth, this.texHeight);

        int color = this.active ? 0xFFFFFF : 0xA0A0A0;

        int centerX = this.getX() + this.getWidth() / 2;
        int centerY = this.getY() + (this.stateHeight - 8) / 2;

        context.drawCenteredString(Minecraft.getInstance().font,
                this.getMessage(), centerX, centerY, color);

    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(ModSounds.BUTTON, 1.0F));
    }

}

