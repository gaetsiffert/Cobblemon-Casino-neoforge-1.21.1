package net.andrespr.casinorocket.screen.widget;

import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public final class ModButtons {

    private ModButtons() {}

    // BET MENU
    public static CommonButton doBet(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 29, 12, ModGuiTextures.BTN_SMALL, onPress, Component.translatable("button.casinorocket.bet"));
    }

    // WITHDRAW MENU
    public static CommonButton doWithdraw(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 82, 12, ModGuiTextures.BTN_LARGE, onPress, Component.translatable("button.casinorocket.withdraw"));
    }

    // COMMON CASINO MACHINE
    public static SlotButton bet(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 72, 24, ModGuiTextures.BTN_BET, onPress, Component.empty());
    }

    public static SlotButton withdraw(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 72, 24, ModGuiTextures.BTN_WITHDRAW, onPress, Component.empty());
    }

    public static SlotButton menu(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 54, 24, ModGuiTextures.BTN_MENU, onPress, Component.empty());
    }

    // SLOT MACHINE
    public static SlotButton spin(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 40, 40, ModGuiTextures.BTN_SPIN, onPress, Component.empty());
    }

    // SLOT MACHINE MENU
    public static SlotButton plus(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 12, 12, ModGuiTextures.BTN_ADD, onPress, Component.empty());
    }

    public static SlotButton subtract(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 12, 12, ModGuiTextures.BTN_SUBTRACT, onPress, Component.empty());
    }

    public static SlotButton mode1(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 14, 14, ModGuiTextures.BTN_MODE1, onPress, Component.empty());
    }

    public static SlotButton mode2(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 14, 14, ModGuiTextures.BTN_MODE2, onPress, Component.empty());
    }

    public static SlotButton mode3(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new SlotButton(baseX + x, baseY + y, 14, 14, ModGuiTextures.BTN_MODE3, onPress, Component.empty());
    }

    // BLACKJACK
    public static CommonButton play(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Component.translatable("button.casinorocket.play"));
    }

    public static CommonButton hit(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Component.translatable("button.casinorocket.hit"));
    }

    public static CommonButton stand(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Component.translatable("button.casinorocket.stand"));
    }

    public static CommonButton doubleDown(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Component.translatable("button.casinorocket.double_down"));
    }

    public static CommonButton finish(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Component.translatable("button.casinorocket.finish"));
    }

    public static CommonButton doubleOrNothing(int baseX, int baseY, int x, int y, Button.OnPress onPress) {
        return new CommonButton(baseX + x, baseY + y, 59, 12, ModGuiTextures.BTN_MEDIUM, onPress, Component.translatable("button.casinorocket.double_or_nothing"));
    }

}

