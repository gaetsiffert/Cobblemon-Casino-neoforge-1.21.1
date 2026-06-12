package net.narrnouille.cobblemoncasino.item;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItemsGroup {
    private static final ResourceLocation ITEMS_TAB_ID = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "cobblemon_casino_1_items");
    private static final ResourceLocation BLOCKS_TAB_ID = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "cobblemon_casino_2_blocks");

    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CobblemonCasino.MOD_ID);

    public static CreativeModeTab COBBLEMON_CASINO_ITEMS_GROUP;
    public static CreativeModeTab COBBLEMON_CASINO_BLOCKS_GROUP;

    static {
        register("cobblemon_casino_1_items", () -> COBBLEMON_CASINO_ITEMS_GROUP =
                CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DIAMOND_CHIP))
                        .title(Component.translatable("itemgroup.cobblemoncasino.items"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .withTabsAfter(BLOCKS_TAB_ID)
                        .displayItems((displayContext, entries) -> {
                            // CHIP ITEMS
                            ModItems.ALL_CHIP_ITEMS.forEach(entries::accept);
                            // ECONOMY
                            entries.accept(ModItems.DIAMOND_NUGGET);
                            entries.accept(ModItems.HANDFUL_OF_RELIC_COINS);
                            entries.accept(ModItems.STACK_OF_RELIC_COINS);
                            // COINS
                            entries.accept(ModItems.COPPER_COIN);
                            entries.accept(ModItems.IRON_COIN);
                            entries.accept(ModItems.GOLD_COIN);
                            entries.accept(ModItems.DIAMOND_COIN);
                            entries.accept(ModItems.EVENT_COIN);
                            entries.accept(ModItems.PRIMOGEM);
                            // GACHAPON
                            entries.accept(ModItems.POKE_GACHAPON);
                            entries.accept(ModItems.GREAT_GACHAPON);
                            entries.accept(ModItems.ULTRA_GACHAPON);
                            entries.accept(ModItems.MASTER_GACHAPON);
                            entries.accept(ModItems.CHERISH_GACHAPON);
                            entries.accept(ModItems.PREMIER_GACHAPON);
                            entries.accept(ModItems.EVENT_GACHAPON);
                            entries.accept(ModItems.POKEMON_POKE_GACHAPON);
                            entries.accept(ModItems.POKEMON_GREAT_GACHAPON);
                            entries.accept(ModItems.POKEMON_ULTRA_GACHAPON);
                            entries.accept(ModItems.POKEMON_MASTER_GACHAPON);
                            entries.accept(ModItems.POKEMON_CHERISH_GACHAPON);
                            entries.accept(ModItems.POKEMON_EVENT_GACHAPON);
                            // WALLET
                            entries.accept(ModItems.WALLET);
                            // DISCS
                            entries.accept(ModItems.FIRERED_GC_MUSIC_DISC);
                            entries.accept(ModItems.HEARTGOLD_GC_MUSIC_DISC);
                            entries.accept(ModItems.EMERALD_GC_MUSIC_DISC);
                            entries.accept(ModItems.PLATINUM_GC_MUSIC_DISC);
                        }).build());

        register("cobblemon_casino_2_blocks", () -> COBBLEMON_CASINO_BLOCKS_GROUP =
                CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.GOLD_BRICKS))
                        .title(Component.translatable("itemgroup.cobblemoncasino.blocks"))
                        .withTabsBefore(ITEMS_TAB_ID)
                        .displayItems((displayContext, entries) -> {
                            // GOLD BLOCKS
                            entries.accept(ModBlocks.CUT_GOLD_BLOCK);
                            entries.accept(ModBlocks.CUT_GOLD_STAIRS);
                            entries.accept(ModBlocks.CUT_GOLD_SLAB);
                            entries.accept(ModBlocks.GOLD_BRICKS);
                            entries.accept(ModBlocks.GOLD_BRICK_STAIRS);
                            entries.accept(ModBlocks.GOLD_BRICK_SLAB);
                            entries.accept(ModBlocks.GOLD_BRICK_WALL);
                            entries.accept(ModBlocks.GOLD_TILES);
                            entries.accept(ModBlocks.GOLD_TILE_STAIRS);
                            entries.accept(ModBlocks.GOLD_TILE_SLAB);
                            entries.accept(ModBlocks.GOLD_TILE_WALL);
                            entries.accept(ModBlocks.CHISELED_GOLD_BLOCK);
                            entries.accept(ModBlocks.GOLD_PILLAR);
                            entries.accept(ModBlocks.HEAVY_GOLD_PILLAR);
                            entries.accept(ModBlocks.GOLD_DOOR);
                            entries.accept(ModBlocks.GOLD_TRAPDOOR);
                            // DIAMOND BLOCKS
                            entries.accept(ModBlocks.CUT_DIAMOND_BLOCK);
                            entries.accept(ModBlocks.CUT_DIAMOND_STAIRS);
                            entries.accept(ModBlocks.CUT_DIAMOND_SLAB);
                            entries.accept(ModBlocks.DIAMOND_BRICKS);
                            entries.accept(ModBlocks.DIAMOND_BRICK_STAIRS);
                            entries.accept(ModBlocks.DIAMOND_BRICK_SLAB);
                            entries.accept(ModBlocks.DIAMOND_BRICK_WALL);
                            entries.accept(ModBlocks.DIAMOND_TILES);
                            entries.accept(ModBlocks.DIAMOND_TILE_STAIRS);
                            entries.accept(ModBlocks.DIAMOND_TILE_SLAB);
                            entries.accept(ModBlocks.DIAMOND_TILE_WALL);
                            entries.accept(ModBlocks.CHISELED_DIAMOND_BLOCK);
                            entries.accept(ModBlocks.DIAMOND_PILLAR);
                            entries.accept(ModBlocks.HEAVY_DIAMOND_PILLAR);
                            entries.accept(ModBlocks.DIAMOND_DOOR);
                            entries.accept(ModBlocks.DIAMOND_TRAPDOOR);
                            // GACHA MACHINES
                            entries.accept(ModBlocks.POKEMON_GACHA_MACHINE);
                            entries.accept(ModBlocks.GACHA_MACHINE);
                            entries.accept(ModBlocks.PLUSHIES_GACHA_MACHINE);
                            entries.accept(ModBlocks.EVENT_GACHA_MACHINE);
                            // SLOT MACHINE
                            entries.accept(ModBlocks.SLOT_MACHINE);
                            // BLACKJACK TABLE
                            entries.accept(ModBlocks.BLACKJACK_TABLE);
                            // CHIP TABLE
                            entries.accept(ModBlocks.CHIP_TABLE);
                            entries.accept(ModBlocks.DECORATIVE_CHIP);
                            entries.accept(ModBlocks.DECORATIVE_CHIP_STACK1);
                            entries.accept(ModBlocks.DECORATIVE_CHIP_STACK2);
                            // SCOREBOARD
                            entries.accept(ModBlocks.CASINO_SCOREBOARD);
                        }).build());
    }

    private static void register(String name, Supplier<CreativeModeTab> supplier) {
        CREATIVE_TABS.register(name, supplier);
    }

    public static void registerItemGroups(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
        CobblemonCasino.LOGGER.info("Registering Item Groups for " + CobblemonCasino.MOD_ID);
    }
}
