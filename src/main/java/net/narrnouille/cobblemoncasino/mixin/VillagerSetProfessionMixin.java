package net.narrnouille.cobblemoncasino.mixin;

import net.narrnouille.cobblemoncasino.network.SuitSync;
import net.narrnouille.cobblemoncasino.util.SuitData;
import net.narrnouille.cobblemoncasino.villager.ModVillagers;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerSetProfessionMixin {

    @Shadow
    public abstract VillagerData getVillagerData();

    @Inject(method = "setVillagerData", at = @At("HEAD"), cancellable = true)
    private void CobblemonCasino$onProfessionChange(VillagerData newData, CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;
        if (villager.level().isClientSide()) return;

        VillagerProfession oldProfession = this.getVillagerData().getProfession();
        VillagerProfession newProfession = newData.getProfession();

        if (oldProfession != ModVillagers.CASINO_WORKER && newProfession == ModVillagers.CASINO_WORKER) {
            if (!ModVillagers.isChipTableCasinoVillagerConversionEnabled()) {
                ci.cancel();
                return;
            }
            CobblemonCasino$applySuit(villager, 1);
        } else if (oldProfession == ModVillagers.CASINO_WORKER && newProfession != ModVillagers.CASINO_WORKER) {
            CobblemonCasino$applySuit(villager, 0);
        }
    }

    private static void CobblemonCasino$applySuit(Villager villager, int suit) {
        int current = SuitData.getSuit(villager);
        if (current == suit) return;

        SuitData.setSuitServer(villager, suit);
        SuitSync.sendSuitSync(villager, suit);
    }

}

