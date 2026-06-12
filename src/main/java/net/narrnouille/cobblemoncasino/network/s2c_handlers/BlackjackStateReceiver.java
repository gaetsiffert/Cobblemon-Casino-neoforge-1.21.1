package net.narrnouille.cobblemoncasino.network.s2c_handlers;

import net.narrnouille.cobblemoncasino.network.s2c.SendBlackjackStateS2CPayload;
import net.narrnouille.cobblemoncasino.screen.custom.blackjack.BlackjackTableScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class BlackjackStateReceiver {

    private BlackjackStateReceiver() {}

    public static void handle(SendBlackjackStateS2CPayload payload, IPayloadContext context) {
        Minecraft.getInstance().execute(() -> {
            Minecraft client = Minecraft.getInstance();
            if (client.screen instanceof BlackjackTableScreen screen) {
                screen.applyState(payload);
            }
        });
    }
}
