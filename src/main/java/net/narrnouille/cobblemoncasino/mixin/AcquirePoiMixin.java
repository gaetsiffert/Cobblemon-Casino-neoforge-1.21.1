package net.narrnouille.cobblemoncasino.mixin;

import net.narrnouille.cobblemoncasino.villager.ModVillagers;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(AcquirePoi.class)
public abstract class AcquirePoiMixin {

    @ModifyVariable(
            method = "create(Ljava/util/function/Predicate;Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;ZLjava/util/Optional;)Lnet/minecraft/world/entity/ai/behavior/BehaviorControl;",
            at = @At("HEAD"),
            argsOnly = true,
            index = 0
    )
    private static Predicate<Holder<PoiType>> CobblemonCasino$filterDisabledChipTablePoi(Predicate<Holder<PoiType>> acquirablePois) {
        return poi -> ModVillagers.canVillagersAcquirePoi(poi) && acquirablePois.test(poi);
    }
}
