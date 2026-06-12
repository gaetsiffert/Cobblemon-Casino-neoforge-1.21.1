package net.narrnouille.cobblemoncasino.network;

import net.narrnouille.cobblemoncasino.util.SuitData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SuitSync {

    public static void handleClientPayload(SuitSyncPayload payload, IPayloadContext context) {
        int entityId = payload.entityId();
        int suitValue = payload.suitValue();

        Minecraft.getInstance().execute(() -> {
            var mc = Minecraft.getInstance();
            if (mc.level == null) return;

            Entity entity = mc.level.getEntity(entityId);
            if (entity instanceof Villager villager) {
                SuitData.setSuitClient(villager, suitValue);
            }
        });
    }

    public static void sendSuitSync(Villager villager, int suitValue) {
        PacketDistributor.sendToPlayersTrackingEntity(villager, new SuitSyncPayload(villager.getId(), suitValue));
    }
}
