package net.narrnouille.cobblemoncasino.network.s2c_handlers;

import net.narrnouille.cobblemoncasino.network.s2c.MoneyValuesSyncS2CPayload;
import net.narrnouille.cobblemoncasino.util.SyncedMoneyValues;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class MoneyValuesSyncReceiver {

    private MoneyValuesSyncReceiver() {}

    public static void handle(MoneyValuesSyncS2CPayload payload, IPayloadContext context) {
        Minecraft.getInstance().execute(() -> SyncedMoneyValues.applyChipValues(payload.chipValues()));
    }
}
