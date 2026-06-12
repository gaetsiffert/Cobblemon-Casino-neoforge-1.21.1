package net.narrnouille.cobblemoncasino.mixin;

import net.narrnouille.cobblemoncasino.util.IdleYawData;
import net.narrnouille.cobblemoncasino.util.LookPlayerData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Villager.class)
public abstract class VillagerLookAtPlayerMixin {

    private static final double RANGE = 10.0;
    private static final float MAX_YAW_STEP = 12.0f;
    private static final float MAX_PITCH_STEP = 10.0f;

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void CobblemonCasino$lookAtClosestPlayerOrIdle(CallbackInfo ci) {
        Villager villager = (Villager) (Object) this;
        if (villager.level().isClientSide()) return;

        if (LookPlayerData.getLookPlayer(villager) != 1) return;

        Player player = villager.level().getNearestPlayer(villager, RANGE);

        float targetYaw;
        float targetPitch;

        if (player != null && !player.isSpectator()) {
            Vec3 from = villager.getEyePosition();
            Vec3 to = player.getEyePosition();

            double dx = to.x - from.x;
            double dy = to.y - from.y;
            double dz = to.z - from.z;

            double distXZ = Math.sqrt(dx * dx + dz * dz);
            if (distXZ < 1.0e-4) return;

            targetYaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0f;
            targetPitch = (float) (-(Mth.atan2(dy, distXZ) * (180.0 / Math.PI)));
        } else {
            targetYaw = IdleYawData.get(villager);
            targetPitch = 0.0f;
        }

        float newYaw = stepAngle(villager.getYRot(), targetYaw, MAX_YAW_STEP);
        float newPitch = stepAngle(villager.getXRot(), targetPitch, MAX_PITCH_STEP);

        villager.setYRot(newYaw);
        villager.yBodyRot = newYaw;
        villager.yHeadRot = newYaw;

        villager.setXRot(newPitch);

        villager.yRotO = newYaw;
        villager.yHeadRotO = newYaw;
        villager.yBodyRotO = newYaw;
        villager.xRotO = newPitch;
    }

    private static float stepAngle(float current, float target, float maxStep) {
        float delta = Mth.wrapDegrees(target - current);
        if (delta > maxStep) delta = maxStep;
        if (delta < -maxStep) delta = -maxStep;
        return current + delta;
    }

}

