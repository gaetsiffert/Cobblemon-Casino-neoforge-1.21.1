package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineMenuScreen;
import net.andrespr.casinorocket.screen.custom.slot.SlotMachineScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MenuScreenSettingsReceiver {

    public static void handle(SendMenuSettingsS2CPayload payload, IPayloadContext context) {
        long balance = payload.balance();
        int betBase = payload.betBase();
        int linesMode = payload.linesMode();

        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().screen instanceof SlotMachineMenuScreen screen) {
                screen.updateSettings(balance, betBase, linesMode);
            } else if (Minecraft.getInstance().screen instanceof SlotMachineScreen screen) {
                screen.updateDisplay(balance, betBase, linesMode);
            }
        });
    }
}
