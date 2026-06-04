package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItemsGroup {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CasinoRocket.MOD_ID);

    public static CreativeModeTab CASINO_ROCKET_ITEMS_GROUP;
    public static CreativeModeTab CASINO_ROCKET_BLOCKS_GROUP;

    static {
        register("casino_rocket_items", () -> CASINO_ROCKET_ITEMS_GROUP =
                CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DIAMOND_CHIP))
                        .title(Component.translatable("itemgroup.casinorocket.casino_rocket_items"))
                        .displayItems((displayContext, entries) -> {
                            // CHIP ITEMS
                            ModItems.ALL_CHIP_ITEMS.forEach(entries::accept);
                            // ECONOMY
                            entries.accept(ModItems.DIAMOND_NUGGET);
                            entries.accept(ModItems.CHARGED_DIAMOND);
                            entries.accept(ModItems.HYPERCHARGED_DIAMOND);
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

        register("casino_rocket_blocks", () -> CASINO_ROCKET_BLOCKS_GROUP =
                CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.GOLD_BRICKS))
                        .title(Component.translatable("itemgroup.casinorocket.casino_rocket_blocks"))
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
                            entries.accept(ModBlocks.CONDENSED_DIAMOND_BLOCK);
                            entries.accept(ModBlocks.CHARGED_DIAMOND_BLOCK);
                            entries.accept(ModBlocks.HYPERCHARGED_DIAMOND_BLOCK);
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
                        }).build());
    }

    private static void register(String name, Supplier<CreativeModeTab> supplier) {
        CREATIVE_TABS.register(name, supplier);
    }

    public static void registerItemGroups(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
        CasinoRocket.LOGGER.info("Registering Item Groups for " + CasinoRocket.MOD_ID);
    }
}
