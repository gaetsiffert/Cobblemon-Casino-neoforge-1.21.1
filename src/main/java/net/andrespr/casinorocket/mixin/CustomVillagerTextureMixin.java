package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.util.SuitData;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerRenderer.class)
@SuppressWarnings("unused")
public class CustomVillagerTextureMixin {

    private static final ResourceLocation TUX_BLACK = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "textures/entity/villager/black_tuxedo.png");
    private static final ResourceLocation TUX_WHITE = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "textures/entity/villager/white_tuxedo.png");
    private static final ResourceLocation TUX_GOLD  = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "textures/entity/villager/gold_tuxedo.png");

    @Inject(method = "getTexture(Lnet/minecraft/entity/passive/VillagerEntity;)Lnet/minecraft/util/Identifier;",
            at = @At("HEAD"), cancellable = true)
    private void casinorocket$getTexture(Villager villager, CallbackInfoReturnable<ResourceLocation> cir) {
        int suit = SuitData.getSuit(villager);
        switch (suit) {
            case 1 -> cir.setReturnValue(TUX_BLACK);
            case 2 -> cir.setReturnValue(TUX_WHITE);
            case 3 -> cir.setReturnValue(TUX_GOLD);
            default -> { return; }
        }
        if (CasinoRocket.LOGGER.isDebugEnabled()) {
            CasinoRocket.LOGGER.debug("[Render] Using custom suit {} for {}", suit, villager.getUUID());
        }
    }

}

