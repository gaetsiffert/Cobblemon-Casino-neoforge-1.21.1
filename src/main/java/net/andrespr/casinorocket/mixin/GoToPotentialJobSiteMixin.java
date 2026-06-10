package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.GoToPotentialJobSite;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(GoToPotentialJobSite.class)
public abstract class GoToPotentialJobSiteMixin {

    @Inject(method = "checkExtraStartConditions", at = @At("HEAD"), cancellable = true)
    private void casinorocket$skipDisabledChipTablePoi(ServerLevel level, Villager owner, CallbackInfoReturnable<Boolean> cir) {
        Optional<GlobalPos> potentialJobSite = owner.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
        if (potentialJobSite.isEmpty()) {
            return;
        }

        GlobalPos globalPos = potentialJobSite.get();
        ServerLevel poiLevel = level.getServer().getLevel(globalPos.dimension());
        if (poiLevel == null) {
            return;
        }

        poiLevel.getPoiManager().getType(globalPos.pos()).ifPresent(poi -> {
            if (!ModVillagers.canVillagersAcquirePoi(poi)) {
                releasePotentialJobSite(owner, poiLevel, globalPos.pos());
                cir.setReturnValue(false);
            }
        });
    }

    private static void releasePotentialJobSite(Villager owner, ServerLevel level, BlockPos pos) {
        PoiManager poiManager = level.getPoiManager();
        if (poiManager.exists(pos, poi -> true)) {
            poiManager.release(pos);
            DebugPackets.sendPoiTicketCountPacket(level, pos);
        }
        owner.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
    }
}
