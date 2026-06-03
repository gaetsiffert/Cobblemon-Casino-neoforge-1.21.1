package net.andrespr.casinorocket.sound;

import net.andrespr.casinorocket.CasinoRocket;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class ModSounds {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, CasinoRocket.MOD_ID);

    // === SOUND EFFECTS ===
    // GACHA MACHINE
    public static SoundEvent INSERTING_COIN;
    // PRIZES
    public static SoundEvent COMMON_PRIZE;
    public static SoundEvent UNCOMMON_PRIZE;
    public static SoundEvent RARE_PRIZE;
    public static SoundEvent ULTRARARE_PRIZE;
    public static SoundEvent LEGENDARY_PRIZE;
    public static SoundEvent BONUS_PRIZE;
    public static SoundEvent OPEN_PRIZE;
    // SLOT MACHINE
    public static SoundEvent REELS_SPINNING;
    public static SoundEvent JACKPOT;
    // WALLET
    public static SoundEvent WALLET;
    public static SoundEvent WALLET2;
    // BLACKJACK
    public static SoundEvent WIN;
    public static SoundEvent DRAW;
    public static SoundEvent LOSE;
    public static SoundEvent CARD;
    // BUTTON
    public static SoundEvent BUTTON;

    // === MUSIC DISCS ===
    // First Generation Remasters
    public static SoundEvent FIRERED_GC;
    public static final ResourceKey<JukeboxSong> FIRERED_GC_KEY = of("firered_gc");
    // Second Generation Remasters
    public static SoundEvent HEARTGOLD_GC;
    public static final ResourceKey<JukeboxSong> HEARTGOLD_GC_KEY = of("heartgold_gc");
    // Third Generation
    public static SoundEvent EMERALD_GC;
    public static final ResourceKey<JukeboxSong> EMERALD_GC_KEY = of("emerald_gc");
    // First Generation
    public static SoundEvent PLATINUM_GC;
    public static final ResourceKey<JukeboxSong> PLATINUM_GC_KEY = of("platinum_gc");

    static {
        registerSoundEvent("inserting_coin", sound -> INSERTING_COIN = sound);
        registerSoundEvent("common_prize", sound -> COMMON_PRIZE = sound);
        registerSoundEvent("uncommon_prize", sound -> UNCOMMON_PRIZE = sound);
        registerSoundEvent("rare_prize", sound -> RARE_PRIZE = sound);
        registerSoundEvent("ultrarare_prize", sound -> ULTRARARE_PRIZE = sound);
        registerSoundEvent("legendary_prize", sound -> LEGENDARY_PRIZE = sound);
        registerSoundEvent("bonus_prize", sound -> BONUS_PRIZE = sound);
        registerSoundEvent("open_prize", sound -> OPEN_PRIZE = sound);
        registerSoundEvent("reels_spinning", sound -> REELS_SPINNING = sound);
        registerSoundEvent("jackpot", sound -> JACKPOT = sound);
        registerSoundEvent("wallet", sound -> WALLET = sound);
        registerSoundEvent("wallet2", sound -> WALLET2 = sound);
        registerSoundEvent("win", sound -> WIN = sound);
        registerSoundEvent("draw", sound -> DRAW = sound);
        registerSoundEvent("lose", sound -> LOSE = sound);
        registerSoundEvent("card", sound -> CARD = sound);
        registerSoundEvent("button", sound -> BUTTON = sound);
        registerSoundEvent("firered_gc", sound -> FIRERED_GC = sound);
        registerSoundEvent("heartgold_gc", sound -> HEARTGOLD_GC = sound);
        registerSoundEvent("emerald_gc", sound -> EMERALD_GC = sound);
        registerSoundEvent("platinum_gc", sound -> PLATINUM_GC = sound);
    }

    private static ResourceKey<JukeboxSong> of(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, id(name));
    }

    private static void registerSoundEvent(String name, Consumer<SoundEvent> assignment) {
        SOUND_EVENTS.register(name, () -> {
            SoundEvent sound = SoundEvent.createVariableRangeEvent(id(name));
            assignment.accept(sound);
            return sound;
        });
    }

    public static void registerSounds(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
        CasinoRocket.LOGGER.info("Registering Mod Sounds for " + CasinoRocket.MOD_ID);
    }

    private static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, name);
    }
}
