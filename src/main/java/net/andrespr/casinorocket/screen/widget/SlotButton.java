package net.andrespr.casinorocket.screen.widget;

import net.andrespr.casinorocket.sound.ModSounds;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SlotButton extends Button {

    private final ResourceLocation texture;
    private final int texWidth;
    private final int texHeight;
    private final int stateHeight;

    private boolean pressed = false;
    private boolean forcedPressed = false;

    private boolean fakePressed = false;

    public SlotButton(int x, int y, int width, int height, ResourceLocation texture, OnPress onPress, Component text) {
        super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.texWidth = width;
        this.stateHeight = height;
        this.texHeight = height * 3;
    }

    public void setForcedPressed(boolean forced) {
        this.forcedPressed = forced;
    }

    public void setFakePressed(boolean fake) {
        this.fakePressed = fake;
        this.active = !fake;
        if (fake) {
            this.pressed = false;
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
        this.pressed = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        this.pressed = false;
    }

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
        int vOffset = 0;

        // Fake pressed
        if (this.fakePressed) {
            vOffset = 0;
        }
        // Disabled
        else if (!this.active) {
            vOffset = this.stateHeight * 2;
        }
        // Forced pressed
        else if (this.forcedPressed) {
            vOffset = this.stateHeight * 2;
        }
        // Real click
        else if (this.pressed) {
            vOffset = this.stateHeight * 2;
        }
        // Hover
        else if (this.isHovered()) {
            vOffset = this.stateHeight;
        }

        context.blit(this.texture, this.getX(), this.getY(), 0, vOffset,
                this.getWidth(), this.stateHeight, this.texWidth, this.texHeight);
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(ModSounds.BUTTON, 1.0F));
    }

}

