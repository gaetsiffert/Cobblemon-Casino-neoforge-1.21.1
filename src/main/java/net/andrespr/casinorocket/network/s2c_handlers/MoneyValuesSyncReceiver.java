package net.andrespr.casinorocket.network.s2c_handlers;

import net.andrespr.casinorocket.network.s2c.MoneyValuesSyncS2CPayload;
import net.andrespr.casinorocket.util.SyncedMoneyValues;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class MoneyValuesSyncReceiver {

    private MoneyValuesSyncReceiver() {}

    public static void handle(MoneyValuesSyncS2CPayload payload, IPayloadContext context) {
        Minecraft.getInstance().execute(() -> SyncedMoneyValues.applyChipValues(payload.chipValues()));
    }
}
