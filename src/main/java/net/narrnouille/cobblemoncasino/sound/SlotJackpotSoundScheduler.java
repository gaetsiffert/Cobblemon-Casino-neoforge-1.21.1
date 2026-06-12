package net.narrnouille.cobblemoncasino.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class SlotJackpotSoundScheduler {

    private static final List<ScheduledJackpotSound> PENDING = new ArrayList<>();

    private SlotJackpotSoundScheduler() {}

    public static void schedule(ServerLevel level, BlockPos pos, int delayTicks) {
        PENDING.add(new ScheduledJackpotSound(level, pos.immutable(), delayTicks));
    }

    public static void onServerTick(ServerTickEvent.Post event) {
        if (PENDING.isEmpty()) return;

        Iterator<ScheduledJackpotSound> iterator = PENDING.iterator();
        while (iterator.hasNext()) {
            ScheduledJackpotSound sound = iterator.next();
            if (sound.tick()) {
                sound.play();
                iterator.remove();
            }
        }
    }

    private static final class ScheduledJackpotSound {
        private final ServerLevel level;
        private final BlockPos pos;
        private int ticksRemaining;

        private ScheduledJackpotSound(ServerLevel level, BlockPos pos, int ticksRemaining) {
            this.level = level;
            this.pos = pos;
            this.ticksRemaining = Math.max(0, ticksRemaining);
        }

        private boolean tick() {
            return ticksRemaining-- <= 0;
        }

        private void play() {
            level.playSound(null, pos, ModSounds.JACKPOT, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}
