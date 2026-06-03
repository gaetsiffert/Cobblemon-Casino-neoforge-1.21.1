package net.andrespr.casinorocket.games.gachapon;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.custom.PokemonGachaMachineBlock;
import net.andrespr.casinorocket.config.machines.GachaMachinesConfig;
import net.andrespr.casinorocket.data.GachaDataStorage;
import net.andrespr.casinorocket.data.GachaStats;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GachaMachinesUtils {

    private static final int DELAY_TICKS = 20;
    private static final Object2LongOpenHashMap<UUID> PLAYER_COOLDOWNS = new Object2LongOpenHashMap<>();

    private static final Object2ObjectOpenHashMap<BlockPos, UUID> LAST_PLAYER_USED = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<UUID, BlockPos> LAST_MACHINE_USED = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<BlockPos, String> LAST_COIN_USED = new Object2ObjectOpenHashMap<>();

    private static final Set<Long> PENDING_PREMIER_BONUS = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final Map<Long, UUID> PENDING_PREMIER_USER = new java.util.concurrent.ConcurrentHashMap<>();

    private static final Locale LOCALE = Locale.ROOT;

    private static final Map<UUID, Long> CLEAN_CONFIRMATION = new HashMap<>();

    // === EXECUTED WHEN A COIN IS DEPOSITED ===
    public static InteractionResult handleUse(Level world, BlockPos pos, Player player, String coinKey) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        final long currentTick = world.getGameTime();
        final long machineKey = pos.asLong();

        PLAYER_COOLDOWNS.object2LongEntrySet().removeIf(e -> currentTick > e.getLongValue());
        UUID currentUser = LAST_PLAYER_USED.get(pos);

        if (currentUser != null && !currentUser.equals(player.getUUID()) || PENDING_PREMIER_BONUS.contains(machineKey)) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_another_occupied", true);
            return InteractionResult.FAIL;
        }

        LAST_MACHINE_USED.put(player.getUUID(), pos);

        long playerCooldownEnd = PLAYER_COOLDOWNS.getLong(player.getUUID());
        if (currentTick < playerCooldownEnd) {
            BlockPos lastUsed = LAST_MACHINE_USED.get(player.getUUID());
            if (lastUsed != null && lastUsed.equals(pos)) {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_occupied", true);
            } else {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_another_occupied", true);
            }
            return InteractionResult.FAIL;
        }

        player.getMainHandItem().shrink(1);

        PLAYER_COOLDOWNS.put(player.getUUID(), currentTick + DELAY_TICKS);
        LAST_COIN_USED.put(pos, coinKey);
        LAST_PLAYER_USED.put(pos, player.getUUID());

        world.playSound(null, pos, ModSounds.INSERTING_COIN, SoundSource.BLOCKS, 1.0F, 1.0F);
        world.scheduleTick(pos, world.getBlockState(pos).getBlock(), DELAY_TICKS);
        CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_coin_inserted", false);

        return InteractionResult.SUCCESS;
    }

    // === EXECUTED AFTER THE DELAY, TO GIVE THE PRIZE ===
    public static void finishDispense(ServerLevel world, BlockPos pos, Direction facing) {
        final long key = pos.asLong();
        final long currentTick = world.getGameTime();

        // === CHECKING BONUS TICK ===
        if (PENDING_PREMIER_BONUS.remove(key)) {
            UUID userId = PENDING_PREMIER_USER.remove(key);
            Player user = userId != null ? world.getPlayerByUUID(userId) : null;
            if (user == null) return;

            boolean pokemon = world.getBlockState(pos).getBlock() instanceof PokemonGachaMachineBlock;
            givePremierBonus(world, pos, facing, user, pokemon);

            LAST_COIN_USED.remove(pos);
            LAST_PLAYER_USED.remove(pos);
            return;
        }

        // === REGULAR PRIZE ===
        UUID uuid = LAST_PLAYER_USED.get(pos);
        Player user = uuid != null ? world.getPlayerByUUID(uuid) : null;

        BlockState state = world.getBlockState(pos);
        boolean pokemon = state.getBlock() instanceof PokemonGachaMachineBlock;

        String coinKey = LAST_COIN_USED.getOrDefault(pos, "copper");

        Map<String, Double> probs = CasinoRocket.CONFIG.gachaMachines.normalizedProbabilities(coinKey);
        Map<String, Double> pityAdjusted = applyPity(probs, coinKey, user);

        String rarity = pickWeightedRarity(world.getRandom(), pityAdjusted);
        ItemStack reward = getRewardForRarity(rarity, pokemon);

        if (rarity.equalsIgnoreCase("legendary") && user != null) {
            GachaDataStorage data = GachaDataStorage.get(requireServer(user.level()));
            Map<String, Integer> playerMap = data.pityTracker.get(user.getUUID());
            if (playerMap != null) {
                playerMap.put(coinKey, 0);
                data.setDirty();
                CasinoRocket.LOGGER.debug("[GachaMachines-Pity] {} pity reset for {}", coinKey, user.getName().getString());
            }
        }

        spawnRarityParticles(world, pos, rarity);
        playRaritySound(world, pos, rarity);
        spawnFireworkByRarity(world, pos, rarity);
        dropFromFront(world, pos, facing, reward);

        boolean scheduledBonus = false;

        if (user != null) {
            GachaDataStorage data = GachaDataStorage.get(world.getServer());
            GachaStats stats = data.playerStats.computeIfAbsent(user.getUUID(),
                    k -> new GachaStats(user.getName().getString()));
            stats.setPlayerName(user.getName().getString());
            stats.recordUse(coinKey, rarity);
            data.setDirty();

            var config = CasinoRocket.CONFIG.gachaMachines.premier_bonus;
            if (config.enable && stats.getTotalCoinsUsed() % config.coinsToBonus == 0) {
                PENDING_PREMIER_BONUS.add(key);
                PENDING_PREMIER_USER.put(key, user.getUUID());
                world.scheduleTick(pos, world.getBlockState(pos).getBlock(), 30);
                scheduledBonus = true;
            }
        }

        giveUserFeedback(probs, coinKey, reward, user);

        if (!scheduledBonus || user == null) {
            LAST_COIN_USED.remove(pos);
            LAST_PLAYER_USED.remove(pos);
        }

        if (user != null) {
            int rarityDelay = getRarityDelayTicks(rarity);
            if (scheduledBonus && rarityDelay < 60) rarityDelay = 60;
            long nextAvailable = currentTick + rarityDelay;
            PLAYER_COOLDOWNS.put(user.getUUID(), nextAvailable);
            LAST_MACHINE_USED.put(user.getUUID(), pos);
        }

    }

    // ===== APPLY PITY TO BASE PROBS =====
    public static Map<String, Double> applyPity(Map<String, Double> probs, String coinKey, Player player) {
        if (!CasinoRocket.CONFIG.gachaMachines.pity.enable) return probs;
        if (player == null) return probs;

        GachaDataStorage data = GachaDataStorage.get(requireServer(player.level()));
        var pityData = getPityData(coinKey);
        if (pityData == null || pityData.usesToMax <= 0 || pityData.maxLegendaryChance <= 0) return probs;

        UUID id = player.getUUID();
        Map<String, Integer> playerMap = data.pityTracker.computeIfAbsent(id, k -> new HashMap<>());
        int uses = playerMap.getOrDefault(coinKey, 0) + 1;
        playerMap.put(coinKey, uses);

        double baseLegendary = probs.getOrDefault("legendary", 0.0);
        double t = Math.min(1.0, (double) uses / pityData.usesToMax);
        double newLegendary = baseLegendary + (pityData.maxLegendaryChance - baseLegendary) * t;

        Map<String, Double> adjusted = new HashMap<>(probs);
        double diff = newLegendary - baseLegendary;

        if (diff > 0) {
            double totalOther = 1.0 - baseLegendary;
            for (String key : adjusted.keySet()) {
                if (!key.equals("legendary")) {
                    adjusted.put(key, adjusted.get(key) * (1.0 - diff) / totalOther);
                }
            }
            adjusted.put("legendary", newLegendary);
        }

        CasinoRocket.LOGGER.debug("[GachaMachines-Pity] {} used {} {} times -> legendary {}%", player.getName().getString(), coinKey, uses, newLegendary * 100);

        data.setDirty();

        return adjusted;
    }

    // ===== PICK RARITY =====
    public static String pickWeightedRarity(RandomSource random, Map<String, Double> probs) {
        double r = random.nextDouble();
        double cumulative = 0.0;
        for (var e : probs.entrySet()) {
            cumulative += e.getValue();
            if (r <= cumulative) return e.getKey();
        }
        return "common";
    }

    // ===== REWARD GETTERS =====

    // Randomly picked Rarity -> Corresponding ItemStack
    public static ItemStack getRewardForRarity(String rarity, boolean pokemon) {
        String key = rarity.toLowerCase(LOCALE);
        ItemStack reward;

        if (pokemon) {
            reward = switch (key) {
                case "common" -> new ItemStack(ModItems.POKEMON_POKE_GACHAPON);
                case "uncommon" -> new ItemStack(ModItems.POKEMON_GREAT_GACHAPON);
                case "rare" -> new ItemStack(ModItems.POKEMON_ULTRA_GACHAPON);
                case "ultrarare" -> new ItemStack(ModItems.POKEMON_MASTER_GACHAPON);
                case "legendary" -> new ItemStack(ModItems.POKEMON_CHERISH_GACHAPON);
                case "bonus" -> new ItemStack(ModItems.POKEMON_PREMIER_GACHAPON);
                default -> ItemStack.EMPTY;
            };
        } else {
            reward = switch (key) {
                case "common" -> new ItemStack(ModItems.POKE_GACHAPON);
                case "uncommon" -> new ItemStack(ModItems.GREAT_GACHAPON);
                case "rare" -> new ItemStack(ModItems.ULTRA_GACHAPON);
                case "ultrarare" -> new ItemStack(ModItems.MASTER_GACHAPON);
                case "legendary" -> new ItemStack(ModItems.CHERISH_GACHAPON);
                case "bonus" -> new ItemStack(ModItems.PREMIER_GACHAPON);
                default -> ItemStack.EMPTY;
            };
        }

        if (reward.isEmpty()) {
            CasinoRocket.LOGGER.warn("[GachaMachines] No valid reward found for rarity '{}'", rarity);
        }

        return reward;
    }

    // Used to give the premier ball bonus prize
    private static void givePremierBonus(ServerLevel world, BlockPos pos, Direction facing, Player user, boolean pokemon) {
        String bonusKey = "bonus";
        ItemStack bonus = getRewardForRarity(bonusKey, pokemon);

        spawnRarityParticles(world, pos, bonusKey);
        playRaritySound(world, pos, bonusKey);
        dropFromFront(world, pos, facing, bonus);

        GachaDataStorage data = GachaDataStorage.get(world.getServer());
        GachaStats stats = data.playerStats.computeIfAbsent(user.getUUID(),
                k -> new GachaStats(user.getName().getString()));
        stats.recordBonus();
        data.setDirty();

        CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_bonus_prize", true);
    }

    // ===== VISUAL AND AUDIO EFFECTS =====

    // SOUND: Picked rarity -> Play special sound
    public static void playRaritySound(Level world, BlockPos pos, String rarity) {
        if (world.isClientSide) return;

        SoundEvent sound = switch (rarity.toLowerCase(LOCALE)) {
            case "common" -> ModSounds.COMMON_PRIZE;
            case "uncommon" -> ModSounds.UNCOMMON_PRIZE;
            case "rare" -> ModSounds.RARE_PRIZE;
            case "ultrarare" -> ModSounds.ULTRARARE_PRIZE;
            case "legendary" -> ModSounds.LEGENDARY_PRIZE;
            case "bonus" -> ModSounds.BONUS_PRIZE;
            default -> null;
        };

        if (sound == null) {
            CasinoRocket.LOGGER.warn("[GachaMachines] No sound event defined for rarity '{}' at {}", rarity, pos);
            return;
        }

        world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);

    }

    // Particles: Picked rarity -> Spawn special particles
    public static void spawnRarityParticles(ServerLevel world, BlockPos pos, String rarity) {
        ParticleOptions particle = switch (rarity.toLowerCase(LOCALE)) {
            case "common" -> ParticleTypes.END_ROD;
            case "uncommon" -> ParticleTypes.HAPPY_VILLAGER;
            case "rare" -> ParticleTypes.FIREWORK;
            case "ultrarare" -> ParticleTypes.DRAGON_BREATH;
            case "legendary" -> ParticleTypes.FLAME;
            case "bonus" -> ParticleTypes.GLOW;
            default -> null;
        };

        if (particle == null) {
            CasinoRocket.LOGGER.warn("[GachaMachines] No particle effect defined for rarity '{}' at {}", rarity, pos);
            return;
        }

        world.sendParticles(particle, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                20, 0.3, 0.3, 0.3, 0.02);
    }

    // Fireworks: Picked rarity -> Spawn special firework (Only for ultrarare/legendary)
    public static void spawnFireworkByRarity(ServerLevel world, BlockPos pos, String rarity) {
        int flight = 0;
        boolean trail = true;
        boolean twinkle = true;
        int color;
        FireworkExplosion.Shape shape;

        switch (rarity.toLowerCase(LOCALE)) {
            case "ultrarare" -> {
                color = 0x8000FF; // PURPLE
                shape = FireworkExplosion.Shape.SMALL_BALL;
            }
            case "legendary" -> {
                color = 0xFF0000; // RED
                shape = FireworkExplosion.Shape.STAR;
            }
            default -> {
                return;
            }
        }

        IntArrayList colors = new IntArrayList(new int[]{color});

        FireworkExplosion explosion = new FireworkExplosion(
                shape, colors, new IntArrayList(), trail, twinkle);

        Fireworks fwComponent = new Fireworks(flight, List.of(explosion));

        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        rocket.set(DataComponents.FIREWORKS, fwComponent);

        FireworkRocketEntity entity = new FireworkRocketEntity(
                world, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, rocket);

        world.addFreshEntity(entity);
    }

    // Feedback: Notification of item gotten & pity update (If active)
    public static void giveUserFeedback(Map<String, Double> probs, String coinKey, ItemStack reward, Player user) {

        if (user == null) return;
        CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_reward", true, reward.getHoverName());

        if ((coinKey.equals("gold") || coinKey.equals("diamond")) && CasinoRocket.CONFIG.gachaMachines.pity.pityUpdateMessages) {

            GachaDataStorage data = GachaDataStorage.get(requireServer(user.level()));
            Map<String, Integer> playerMap = data.pityTracker.get(user.getUUID());
            double pityChance = getCurrentLegendaryChance(coinKey, user, probs);
            int maxUses = getMaxUses(coinKey);

            MutableComponent coinName = Objects.requireNonNull(getCoinStack(coinKey)).getHoverName().copy();
            String formattedChance = String.format("%.2f", pityChance * 100.0);

            if (playerMap != null && playerMap.getOrDefault(coinKey, 0) == 0) {
                CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_pity_reset", false,
                        coinName.withStyle(ChatFormatting.GOLD), Component.literal(formattedChance).withStyle(ChatFormatting.YELLOW));
            } else if (playerMap != null && playerMap.getOrDefault(coinKey, 0) >= maxUses) {
                CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_pity_max", false,
                        coinName.withStyle(ChatFormatting.GOLD), Component.literal(formattedChance).withStyle(ChatFormatting.YELLOW));
            } else {
                CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_pity_update", false,
                        coinName.withStyle(ChatFormatting.GOLD), Component.literal(formattedChance).withStyle(ChatFormatting.YELLOW));
            }

        }

    }

    // ===== GETTERS =====

    public static String getCoinKey(ItemStack stack) {
        if (stack.is(ModItems.COPPER_COIN)) return "copper";
        if (stack.is(ModItems.IRON_COIN)) return "iron";
        if (stack.is(ModItems.GOLD_COIN)) return "gold";
        if (stack.is(ModItems.DIAMOND_COIN)) return "diamond";
        return null;
    }

    public static ItemStack getCoinStack(String coinKey) {
        return switch (coinKey.toLowerCase(LOCALE)) {
            case "copper" -> new ItemStack(ModItems.COPPER_COIN);
            case "iron" -> new ItemStack(ModItems.IRON_COIN);
            case "gold" -> new ItemStack(ModItems.GOLD_COIN);
            case "diamond" -> new ItemStack(ModItems.DIAMOND_COIN);
            default -> ItemStack.EMPTY;
        };
    }

    public static GachaMachinesConfig.PityConfig.CoinPity getPityData(String coinKey) {
        return switch (coinKey) {
            case "iron" -> CasinoRocket.CONFIG.gachaMachines.pity.iron;
            case "gold" -> CasinoRocket.CONFIG.gachaMachines.pity.gold;
            case "diamond" -> CasinoRocket.CONFIG.gachaMachines.pity.diamond;
            default -> null;
        };
    }

    public static double getCurrentLegendaryChance(String coinKey, Player player, Map<String, Double> baseProbs) {
        GachaDataStorage data = GachaDataStorage.get(requireServer(player.level()));
        Map<String, Integer> playerMap = data.pityTracker.get(player.getUUID());
        if (playerMap == null) return baseProbs.getOrDefault("legendary", 0.0);

        int uses = playerMap.getOrDefault(coinKey, 0);
        var pityData = getPityData(coinKey);
        if (pityData == null) return baseProbs.getOrDefault("legendary", 0.0);

        double base = baseProbs.getOrDefault("legendary", 0.0);
        double t = Math.min(1.0, (double) uses / pityData.usesToMax);
        return base + (pityData.maxLegendaryChance - base) * t;
    }

    public static int getMaxUses(String coinKey) {
        var pityData = getPityData(coinKey);
        return pityData != null ? pityData.usesToMax : 1;
    }

    public static int getRarityDelayTicks(String rarity) {
        return switch (rarity.toLowerCase(LOCALE)) {
            case "uncommon" -> 20;
            case "rare" -> 35;
            case "ultrarare" -> 80;
            case "legendary" -> 70;
            case "bonus" -> 40;
            default -> 10;
        };
    }

    // === EVENT GACHAPON ===
    public static InteractionResult handleEventUse(Level world, BlockPos pos, Player player) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        final long currentTick = world.getGameTime();

        PLAYER_COOLDOWNS.object2LongEntrySet().removeIf(e -> currentTick > e.getLongValue());

        long playerCooldownEnd = PLAYER_COOLDOWNS.getLong(player.getUUID());
        if (currentTick < playerCooldownEnd) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_occupied", true);
            return InteractionResult.FAIL;
        }

        PLAYER_COOLDOWNS.put(player.getUUID(), currentTick + DELAY_TICKS);
        LAST_PLAYER_USED.put(pos, player.getUUID());

        world.playSound(null, pos, ModSounds.INSERTING_COIN, SoundSource.BLOCKS, 1.0F, 1.0F);
        world.scheduleTick(pos, world.getBlockState(pos).getBlock(), DELAY_TICKS);
        CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_coin_inserted", false);

        return InteractionResult.SUCCESS;
    }

    public static void finishEventDispense(ServerLevel world, BlockPos pos, Direction facing) {
        UUID uuid = LAST_PLAYER_USED.remove(pos);
        Player user = uuid != null ? world.getPlayerByUUID(uuid) : null;

        if (user == null) return;

        boolean pokemon = world.random.nextBoolean();
        boolean hasItems = GachaponUtils.hasValidPool("event");
        boolean hasPokemon = PokemonGachaponUtils.hasValidPool("event");

        if (pokemon && !hasPokemon) pokemon = false;
        if (!pokemon && !hasItems) pokemon = true;

        ItemStack reward = pokemon ? new ItemStack(ModItems.POKEMON_EVENT_GACHAPON) : new ItemStack(ModItems.EVENT_GACHAPON);

        // === FORCE RARE EFFECTS ===
        spawnRarityParticles(world, pos, "rare");
        playRaritySound(world, pos, "rare");
        spawnFireworkByRarity(world, pos, "rare");
        dropFromFront(world, pos, facing, reward);

        CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_reward", true, reward.getHoverName());

        long nextAvailable = world.getGameTime() + getRarityDelayTicks("rare");
        PLAYER_COOLDOWNS.put(user.getUUID(), nextAvailable);
    }

    // === PLUSHIES ===
    public static InteractionResult handlePlushiesUse(Level world, BlockPos pos, Player player) {
        if (world.isClientSide) return InteractionResult.SUCCESS;

        final long currentTick = world.getGameTime();

        PLAYER_COOLDOWNS.object2LongEntrySet().removeIf(e -> currentTick > e.getLongValue());

        UUID currentUser = LAST_PLAYER_USED.get(pos);
        if (currentUser != null && !currentUser.equals(player.getUUID())) {
            CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_another_occupied", true);
            return InteractionResult.FAIL;
        }

        LAST_MACHINE_USED.put(player.getUUID(), pos);

        long playerCooldownEnd = PLAYER_COOLDOWNS.getLong(player.getUUID());
        if (currentTick < playerCooldownEnd) {
            BlockPos lastUsed = LAST_MACHINE_USED.get(player.getUUID());
            if (lastUsed != null && lastUsed.equals(pos)) {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_occupied", true);
            } else {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_another_occupied", true);
            }
            return InteractionResult.FAIL;
        }

        PLAYER_COOLDOWNS.put(player.getUUID(), currentTick + DELAY_TICKS);
        LAST_PLAYER_USED.put(pos, player.getUUID());

        world.playSound(null, pos, ModSounds.INSERTING_COIN, SoundSource.BLOCKS, 1.0F, 1.0F);
        world.scheduleTick(pos, world.getBlockState(pos).getBlock(), DELAY_TICKS);
        CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.gacha_machines_coin_inserted", false);

        return InteractionResult.SUCCESS;
    }

    public static void finishPlushiesDispense(ServerLevel world, BlockPos pos, Direction facing) {

        final long currentTick = world.getGameTime();

        UUID uuid = LAST_PLAYER_USED.remove(pos);
        Player user = uuid != null ? world.getPlayerByUUID(uuid) : null;
        if (user == null) return;

        ItemStack reward = PlushiesGachaponUtils.pickPlushie(world.getRandom());
        if (reward.isEmpty()) {
            CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.item_gachapon_empty", true);
            CasinoRocket.LOGGER.warn("[PlushiesMachine] No valid plushies in config (or total weight 0).");
            return;
        }

        spawnRarityParticles(world, pos, "rare");
        playRaritySound(world, pos, "rare");
        spawnFireworkByRarity(world, pos, "rare");
        dropFromFront(world, pos, facing, reward);

        CasinoRocketLogger.toPlayerTranslated(user, "message.casinorocket.gacha_machines_reward", true, reward.getHoverName());

        long nextAvailable = currentTick + getRarityDelayTicks("rare");
        PLAYER_COOLDOWNS.put(user.getUUID(), nextAvailable);
        LAST_MACHINE_USED.put(user.getUUID(), pos);

    }

    // ===== HELPERS =====

    public static void dropFromFront(Level world, BlockPos pos, Direction facing, ItemStack stack) {
        if (world.isClientSide) return;
        double x = pos.getX() + 0.5 + facing.getStepX() * 0.7;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5 + facing.getStepZ() * 0.7;

        ItemEntity entity = new ItemEntity(world, x, y, z, stack.copy());
        double speed = 0.15;
        entity.setDeltaMovement(facing.getStepX() * speed, 0.1, facing.getStepZ() * speed);
        world.addFreshEntity(entity);
    }

    private static MinecraftServer requireServer(Level world) {
        return Objects.requireNonNull(world.getServer(), "[CasinoRocket] Server not found!");
    }

    // ===== STATS / COMMAND HELPERS =====

    public static Component getPlayerStatsText(Object targetOrName, MinecraftServer server) {
        GachaDataStorage data = GachaDataStorage.get(server);
        GachaStats stats = null;
        String playerName = null;

        // === Resolve player or name ===
        if (targetOrName instanceof ServerPlayer player) {
            stats = data.playerStats.get(player.getUUID());
            playerName = (stats != null && stats.getPlayerName() != null)
                    ? stats.getPlayerName()
                    : player.getName().getString();
        } else if (targetOrName instanceof String name) {
            for (GachaStats s : data.playerStats.values()) {
                if (s.getPlayerName() != null && s.getPlayerName().equalsIgnoreCase(name)) {
                    stats = s;
                    playerName = name;
                    break;
                }
            }
        }

        if (stats == null || playerName == null) {
            return Component.literal("No recorded stats for player.").withStyle(ChatFormatting.GRAY);
        }

        // === Build formatted text ===
        MutableComponent text = Component.literal("\n")
                .append(Component.literal("Gacha Machine Stats for ").withStyle(ChatFormatting.BOLD))
                .append(Component.literal(playerName).withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD))
                .append(Component.literal(":\n").withStyle(ChatFormatting.BOLD))
                .append(Component.literal("Total Coins Used: ").withStyle(ChatFormatting.YELLOW))
                .append(Component.literal(stats.getTotalCoinsUsed() + "\n\n").withStyle(ChatFormatting.YELLOW));

        // === Coins Used by Type ===
        text.append(Component.literal("Coins Inserted:\n").withStyle(ChatFormatting.YELLOW));

        Map<String, Integer> coins = new LinkedHashMap<>();
        coins.put("Copper", stats.copperUsed);
        coins.put("Iron", stats.ironUsed);
        coins.put("Gold", stats.goldUsed);
        coins.put("Diamond", stats.diamondUsed);

        int coinIndex = 0;
        int coinSize = coins.size();
        for (var e : coins.entrySet()) {
            String coin = e.getKey();
            int count = e.getValue();

            text.append(Component.literal("• ").withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(coin + ": ").withStyle(TextUtils.coinColor(coin)))
                    .append(Component.literal(String.valueOf(count)).withStyle(ChatFormatting.YELLOW));

            if (++coinIndex < coinSize) {
                text.append(Component.literal("\n"));
            }
        }

        // === Rarity Stats ===
        text.append(Component.literal("\n\nGachapon Rarity Obtained:\n").withStyle(ChatFormatting.YELLOW));

        var entries = stats.getRarityCounts().entrySet();
        int index = 0;
        int size = entries.size();

        for (var e : entries) {
            String rarity = e.getKey();
            int count = e.getValue();

            text.append(Component.literal("• ").withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(capitalize(rarity) + ": ").withStyle(TextUtils.rarityColor(rarity)))
                    .append(Component.literal(String.valueOf(count)).withStyle(ChatFormatting.YELLOW));

            if (++index < size) {
                text.append(Component.literal("\n"));
            }
        }

        return text;
    }

    public static Component getLeaderboardText(MinecraftServer server, String category, String key) {
        GachaDataStorage data = GachaDataStorage.get(server);
        data.playerStats.values().removeIf(Objects::isNull);

        final String input = key.toLowerCase(Locale.ROOT);
        boolean isRarity = category.equalsIgnoreCase("rarity");
        boolean isCoin = category.equalsIgnoreCase("coins");

        // === Type validation ===
        if (!isRarity && !isCoin) {
            return Component.literal("Invalid leaderboard type. Use 'rarity' or 'coins'.").withStyle(ChatFormatting.RED);
        }

        // === Dynamic sort ===
        Comparator<Map.Entry<UUID, GachaStats>> comparator = Comparator.comparingInt(
                (Map.Entry<UUID, GachaStats> e) -> isRarity ? e.getValue().getRarityCount(input) : e.getValue().getCoinCount(input)
                ).reversed();

        // === Top 10 ===
        List<Map.Entry<UUID, GachaStats>> sorted = data.playerStats.entrySet().stream()
                .sorted(comparator)
                .limit(10)
                .toList();

        if (sorted.isEmpty()) {
            return Component.literal("No entries for " + category + " '" + key + "'.").withStyle(ChatFormatting.GRAY);
        }

        // === Head ===
        ChatFormatting titleColor = isRarity ? TextUtils.rarityColor(input) : TextUtils.coinColor(input);
        String titleLabel = (isRarity ? "Gacha Wins (" : "Coins Used (") + key.toUpperCase(Locale.ROOT) + ")";

        MutableComponent out = Component.literal("\n")
                .append(Component.literal("Top 10 - " + titleLabel).withStyle(titleColor, ChatFormatting.BOLD))
                .append(Component.literal("\n"));

        // === Body ===
        int rank = 1;
        for (Map.Entry<UUID, GachaStats> e : sorted) {
            UUID id = e.getKey();
            GachaStats stats = e.getValue();
            String name = stats.getPlayerName() != null ? stats.getPlayerName() : shortUuid(id);

            int count = isRarity ? stats.getRarityCount(input) : stats.getCoinCount(input);

            out.append(Component.literal(rank + ". ").withStyle(ChatFormatting.YELLOW))
                    .append(Component.literal(name).withStyle(TextUtils.rankColors(rank)))
                    .append(Component.literal(" - ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(String.valueOf(count)).withStyle(ChatFormatting.YELLOW));

            if (rank++ < sorted.size()) {
                out.append(Component.literal("\n"));
            }
        }

        return out;
    }

    public static Component clearAllGachaData(MinecraftServer server, boolean confirm, @Nullable ServerPlayer sender) {
        UUID id = sender != null ? sender.getUUID() : UUID.randomUUID();
        long now = server.overworld().getGameTime();
        long window = 20 * 30;

        if (!confirm) {
            CLEAN_CONFIRMATION.put(id, now);
            return Component.literal("⚠ Are you sure you want to delete ALL Gacha data?\n")
                    .append(Component.literal("Type again within 30 seconds:\n").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal("'/casinorocket gachapon machines cleandata confirm'").withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        }

        Long lastRequest = CLEAN_CONFIRMATION.get(id);
        if (lastRequest == null) {
            return Component.literal("You must run '/casinorocket gachapon machines cleandata' first.").withStyle(ChatFormatting.RED);
        }
        if (now - lastRequest > window) {
            return Component.literal("Confirmation expired. Type again '/casinorocket gachapon machines cleandata'").withStyle(ChatFormatting.GRAY);
        }

        CLEAN_CONFIRMATION.remove(id);

        GachaDataStorage data = GachaDataStorage.get(server);
        int playerCount = data.playerStats.size();
        int pityCount = data.pityTracker.size();

        data.playerStats.clear();
        data.pityTracker.clear();
        data.setDirty();

        CasinoRocket.LOGGER.warn("[CasinoRocket] Cleared Gacha data: {} player stats, {} pity entries removed.", playerCount, pityCount);

        return Component.literal("All Gacha data cleared successfully.\n")
                .append(Component.literal(playerCount + " player stats and " + pityCount + " pity entries removed.").withStyle(ChatFormatting.GRAY));
    }

    public static Component getMachineRatesText(@Nullable ServerPlayer player, String coinKey, boolean includePity) {
        Map<String, Double> base = CasinoRocket.CONFIG.gachaMachines.normalizedProbabilities(coinKey);
        if (base == null || base.isEmpty()) {
            return Component.literal("No rates available for coin '" + coinKey + "'.").withStyle(ChatFormatting.RED);
        }

        Map<String, Double> rates = new LinkedHashMap<>(base);

        if (includePity && player != null && CasinoRocket.CONFIG.gachaMachines.pity.enable) {
            rates = previewPityAdjusted(base, coinKey, player);
        }

        MutableComponent result = Component.literal("");
        String title = includePity
                ? "Rates with Pity (" + coinKey.toUpperCase(Locale.ROOT) + ")"
                : "Base Rates (" + coinKey.toUpperCase(Locale.ROOT) + ")";
        result.append(Component.literal(title).withStyle(ChatFormatting.UNDERLINE)).append("\n");

        boolean first = true;
        for (var e : rates.entrySet()) {
            if (!first) result.append(Component.literal(", "));
            first = false;

            String rarity = capitalize(e.getKey());
            double percentage = e.getValue() * 100.0;
            double rounded = Math.round(percentage * 100.0) / 100.0;

            ChatFormatting color = TextUtils.percentagesColor(rounded);

            result.append(Component.literal(rarity + ": ")
                    .append(Component.literal(String.format("%.2f%%", rounded)).withStyle(color)));
        }

        return result;
    }

    public static Map<String, Double> previewPityAdjusted(Map<String, Double> probs, String coinKey, ServerPlayer player) {
        GachaMachinesConfig.PityConfig.CoinPity pityData = getPityData(coinKey);
        if (pityData == null || !CasinoRocket.CONFIG.gachaMachines.pity.enable || player == null) return probs;

        GachaDataStorage data = GachaDataStorage.get(requireServer(player.level()));
        int uses = data.pityTracker.getOrDefault(player.getUUID(), Map.of()).getOrDefault(coinKey, 0);

        double baseLegendary = probs.getOrDefault("legendary", 0.0);
        double t = Math.min(1.0, (double) uses / pityData.usesToMax);
        double newLegendary = baseLegendary + (pityData.maxLegendaryChance - baseLegendary) * t;

        if (newLegendary <= baseLegendary) return probs;

        Map<String, Double> adjusted = new LinkedHashMap<>(probs);
        double diff = newLegendary - baseLegendary;
        double totalOther = 1.0 - baseLegendary;

        for (String key : adjusted.keySet()) {
            if (!key.equals("legendary")) {
                adjusted.put(key, adjusted.get(key) * (1.0 - diff) / totalOther);
            }
        }
        adjusted.put("legendary", newLegendary);

        return adjusted;
    }

    public static Set<String> getRarityKeys() {
        return Set.of("common", "uncommon", "rare", "ultrarare", "legendary", "bonus");
    }

    public static Set<String> getCoinKeys() {
        return Set.of("copper", "iron", "gold", "diamond");
    }

    private static String capitalize(String s) {
        return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static String shortUuid(UUID id) {
        String s = id.toString();
        return "Player-" + s.substring(0, 8);
    }

}

