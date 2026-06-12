package net.andrespr.casinorocket.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.andrespr.casinorocket.network.SuitSync;
import net.andrespr.casinorocket.network.SuitSyncPayload;
import net.andrespr.casinorocket.util.IdleYawData;
import net.andrespr.casinorocket.util.LookPlayerData;
import net.andrespr.casinorocket.util.SuitData;
import net.andrespr.casinorocket.villager.VillagerNbtFactory;
import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.andrespr.casinorocket.villager.ShopsRegistry;
import net.andrespr.casinorocket.villager.shops.IShop;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.EntityHitResult;
import java.util.Map;
import java.util.TreeMap;

public final class VillagerCommands {

    private VillagerCommands() {}

    // === SPAWN ===
    private static final Map<String, IShop> SHOP_TYPES = ShopsRegistry.all();

    // === SET SUIT ===
    private static final Map<String, Integer> SUIT_NAMES = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    static {
        SUIT_NAMES.put("None", 0);
        SUIT_NAMES.put("Black Tuxedo", 1);
        SUIT_NAMES.put("White Tuxedo", 2);
        SUIT_NAMES.put("Gold Tuxedo", 3);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> buildSubcommand() {
        return Commands.literal("villager")
                .requires(src -> src.hasPermission(2))

                // /casinorocket villager spawn <type>
                .then(Commands.literal("spawn")
                        .then(Commands.argument("type", StringArgumentType.greedyString())
                                .suggests((ctx, builder) -> {
                                    for (String k : SHOP_TYPES.keySet()) builder.suggest(k);
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> executeSpawn(
                                        StringArgumentType.getString(ctx, "type"),
                                        ctx.getSource()
                                ))))

                // /casinorocket villager setsuit <suitName>
                .then(Commands.literal("setsuit")
                        .then(Commands.argument("suit", StringArgumentType.greedyString())
                                .suggests((ctx, builder) -> {
                                    SUIT_NAMES.keySet().forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> executeSetSuit(
                                        ctx.getSource(),
                                        StringArgumentType.getString(ctx, "suit")
                                ))))

                // /casinorocket villager setai <true/false>
                .then(Commands.literal("setai")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(ctx -> executeSetAi(
                                        ctx.getSource(),
                                        BoolArgumentType.getBool(ctx, "value")
                                ))))

                // /casinorocket villager lookplayer <true/false>
                .then(Commands.literal("lookplayer")
                        .then(Commands.argument("value", BoolArgumentType.bool())
                                .executes(ctx -> executeLookPlayer(
                                        ctx.getSource(),
                                        BoolArgumentType.getBool(ctx, "value")
                                ))))

                // /casinorocket villager lookdirection <north/south/east/west>
                .then(Commands.literal("lookdirection")
                        .then(Commands.argument("dir", StringArgumentType.word())
                                .suggests((ctx, b) -> {
                                    b.suggest("north");
                                    b.suggest("south");
                                    b.suggest("east");
                                    b.suggest("west");
                                    return b.buildFuture();
                                })
                                .executes(ctx -> executeLookDirection(
                                        ctx.getSource(),
                                        StringArgumentType.getString(ctx, "dir")
                                ))
                        )
                );
    }

    // === SPAWN ===
    private static int executeSpawn(String type, CommandSourceStack source) {

        IShop shop = SHOP_TYPES.get(type);
        if (shop == null) {
            source.sendFailure(Component.translatable("command.casinorocket.unknown_casino_worker_type", type));
            return 0;
        }

        ServerLevel world = source.getLevel();
        BlockPos blockPos = BlockPos.containing(source.getPosition());
        BlockPos villagerPos = blockPos.east();

        VillagerTradeHelper.ShopData data = shop.build();
        if (!VillagerTradeHelper.hasTrades(data)) {
            source.sendFailure(Component.translatable("command.casinorocket.casino_worker_no_valid_offers", type));
            return 0;
        }

        String blockId = (data.jobBlockId != null) ? data.jobBlockId : "cobblemon:display_case";
        Block jobBlock = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockId));
        world.setBlockAndUpdate(blockPos, jobBlock.defaultBlockState());

        String formattedType = ShopsRegistry.all().keySet().stream().filter(k -> k.equalsIgnoreCase(type))
                .findFirst().orElse(type);

        CompoundTag root = VillagerNbtFactory.createBaseVillagerNbt(
                formattedType, blockPos, data.profession, data.suitId
        );

        if (data.shops != null && !data.shops.isEmpty()) {
            root.put("CobbleMerchantShop", data.shops);
        }
        if (VillagerTradeHelper.hasVanillaOffers(data.offersNbt)) {
            root.put("Offers", data.offersNbt);
        }

        Villager villager;
        try {
            villager = EntityType.VILLAGER.create(world);
            if (villager == null) {
                source.sendFailure(Component.translatable("command.casinorocket.failed_create_villager"));
                return 0;
            }
            villager.load(root);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Error creating villager from NBT", e);
            source.sendFailure(Component.translatable("command.casinorocket.error_create_villager_nbt", e.getMessage()));
            return 0;
        }
        SuitData.setSuitServer(villager, data.suitId);

        ServerPlayer player = source.getPlayer();
        float yaw = (player != null) ? player.getYRot() : 0f;

        villager.moveTo
                (villagerPos.getX() + 0.5, villagerPos.getY(), villagerPos.getZ() + 0.5, yaw, 0f);

        boolean spawned;
        try {
            spawned = world.addFreshEntity(villager);
        } catch (Exception e) {
            CasinoRocket.LOGGER.error("Exception when spawning entity", e);
            source.sendFailure(Component.translatable("command.casinorocket.exception_spawning_entity", e.getMessage()));
            return 0;
        }

        if (!spawned) {
            source.sendFailure(Component.translatable("command.casinorocket.entity_spawn_failed"));
            return 0;
        }
        SuitSync.sendSuitSync(villager, data.suitId);
        if (player != null) {
            CasinoRocketPackets.sendToPlayer(player, new SuitSyncPayload(villager.getId(), data.suitId));
        }

        source.sendSuccess(() -> Component.translatable("command.casinorocket.casino_worker_spawned", type), false);
        return 1;
    }

    // === SET SUIT ===
    private static int executeSetSuit(CommandSourceStack source, String suitName) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.translatable("command.casinorocket.player_only_command"));
            return 0;
        }

        Integer suitValue = SUIT_NAMES.get(suitName);
        if (suitValue == null) {
            source.sendFailure(Component.translatable("command.casinorocket.unknown_suit_name", suitName));
            return 0;
        }

        Villager villager = getLookedVillager(source);
        if (villager == null) {
            source.sendFailure(Component.translatable("command.casinorocket.must_look_at_villager"));
            return 0;
        }

        int current = SuitData.getSuit(villager);
        if (current == suitValue) {
            source.sendSuccess(() -> Component.translatable("command.casinorocket.suit_already_set", suitName), false);
            return 1;
        }

        SuitData.setSuitServer(villager, suitValue);
        SuitSync.sendSuitSync(villager, suitValue);

        source.sendSuccess(() -> Component.translatable("command.casinorocket.suit_set", suitName, villager.getName()), true);
        return 1;
    }

    // === SET AI ===
    private static int executeSetAi(CommandSourceStack source, boolean enabled) {
        Villager villager = getLookedVillager(source);
        if (villager == null) {
            source.sendFailure(Component.translatable("command.casinorocket.must_look_at_villager"));
            return 0;
        }

        villager.setNoAi(!enabled);

        source.sendSuccess(() ->
                Component.translatable("command.casinorocket.ai_set", enabled, villager.getName()), true);
        return 1;
    }

    // === LOOK PLAYER ===
    private static int executeLookPlayer(CommandSourceStack source, boolean enabled) {
        Villager villager = getLookedVillager(source);
        if (villager == null) {
            source.sendFailure(Component.translatable("command.casinorocket.must_look_at_villager"));
            return 0;
        }

        LookPlayerData.setLookPlayer(villager, enabled ? 1 : 0);

        source.sendSuccess(() -> Component.translatable("command.casinorocket.look_player_set", enabled, villager.getName()), true);
        return 1;
    }

    // === LOOK DIRECTION ===
    private static int executeLookDirection(CommandSourceStack source, String dir) {
        Villager villager = getLookedVillager(source);
        if (villager == null) {
            source.sendFailure(Component.translatable("command.casinorocket.must_look_at_villager"));
            return 0;
        }

        Float yaw = directionToYaw(dir);
        if (yaw == null) {
            source.sendFailure(Component.translatable("command.casinorocket.invalid_direction", dir));
            return 0;
        }

        IdleYawData.set(villager, yaw);

        villager.setYRot(yaw);
        villager.yBodyRot = yaw;
        villager.yHeadRot = yaw;
        villager.setXRot(0f);

        villager.yRotO = yaw;
        villager.yBodyRotO = yaw;
        villager.yHeadRotO = yaw;
        villager.xRotO = 0f;

        source.sendSuccess(() -> Component.translatable("command.casinorocket.idle_direction_set", dir, villager.getName()), true);
        return 1;
    }

    // === HELPERS ===
    private static Float directionToYaw(String dir) {
        return switch (dir.toLowerCase()) {
            case "south" -> 0f;
            case "west"  -> 90f;
            case "north" -> 180f;
            case "east"  -> -90f;
            default -> null;
        };
    }

    public static Villager getLookedVillager(CommandSourceStack source) {
        try {
            var player = source.getPlayer();
            if (player == null) return null;

            var world = player.level();
            double reachDistance = 10.0D;

            var eyePos = player.getEyePosition(1.0F);
            var lookVec = player.getViewVector(1.0F);
            var endVec = eyePos.add(lookVec.scale(reachDistance));
            var box = player.getBoundingBox().expandTowards(lookVec.scale(reachDistance)).inflate(1.0D);

            EntityHitResult hit = ProjectileUtil.getEntityHitResult(
                    world, player, eyePos, endVec, box,
                    entity -> entity instanceof Villager && entity.isAlive()
            );

            Entity e = (hit != null) ? hit.getEntity() : null;
            return (e instanceof Villager v) ? v : null;

        } catch (Exception ignored) {}
        return null;
    }

}

