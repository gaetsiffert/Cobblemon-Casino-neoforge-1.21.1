package net.narrnouille.cobblemoncasino.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.util.SuitData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerProfessionLayer.class)
@SuppressWarnings("unused")
public class VillagerClothingFeatureRendererMixin {

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void CobblemonCasino$hideClothingWhenSuited(
            PoseStack matrices, MultiBufferSource vertexConsumers, int light, LivingEntity livingEntity,
            float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch,
            CallbackInfo ci
    ) {
        try {
            if (livingEntity instanceof Villager villager) {
                int suit = SuitData.getSuit(villager);
                if (suit > 0) {
                    ci.cancel();
                }
            }
        } catch (Throwable ex) {
            CobblemonCasino.LOGGER.warn("Error in VillagerClothingFeatureRendererMixin", ex);
        }
    }

}

