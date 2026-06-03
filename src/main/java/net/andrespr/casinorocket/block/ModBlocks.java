package net.andrespr.casinorocket.block;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.custom.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CasinoRocket.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CasinoRocket.MOD_ID);

    // BLOCKS

    public static Block CUT_GOLD_BLOCK;
    public static Block CUT_GOLD_STAIRS;
    public static Block CUT_GOLD_SLAB;
    public static Block GOLD_BRICKS;
    public static Block GOLD_BRICK_STAIRS;
    public static Block GOLD_BRICK_SLAB;
    public static Block GOLD_BRICK_WALL;
    public static Block GOLD_TILES;
    public static Block GOLD_TILE_STAIRS;
    public static Block GOLD_TILE_SLAB;
    public static Block GOLD_TILE_WALL;
    public static Block CHISELED_GOLD_BLOCK;
    public static Block GOLD_PILLAR;
    public static Block HEAVY_GOLD_PILLAR;
    public static Block GOLD_DOOR;
    public static Block GOLD_TRAPDOOR;
    public static Block CUT_DIAMOND_BLOCK;
    public static Block CUT_DIAMOND_STAIRS;
    public static Block CUT_DIAMOND_SLAB;
    public static Block DIAMOND_BRICKS;
    public static Block DIAMOND_BRICK_STAIRS;
    public static Block DIAMOND_BRICK_SLAB;
    public static Block DIAMOND_BRICK_WALL;
    public static Block DIAMOND_TILES;
    public static Block DIAMOND_TILE_STAIRS;
    public static Block DIAMOND_TILE_SLAB;
    public static Block DIAMOND_TILE_WALL;
    public static Block CHISELED_DIAMOND_BLOCK;
    public static Block DIAMOND_PILLAR;
    public static Block HEAVY_DIAMOND_PILLAR;
    public static Block DIAMOND_DOOR;
    public static Block DIAMOND_TRAPDOOR;
    public static Block CONDENSED_DIAMOND_BLOCK;
    public static Block CHARGED_DIAMOND_BLOCK;
    public static Block HYPERCHARGED_DIAMOND_BLOCK;
    public static Block GACHA_MACHINE;
    public static Block POKEMON_GACHA_MACHINE;
    public static Block PLUSHIES_GACHA_MACHINE;
    public static Block EVENT_GACHA_MACHINE;
    public static Block SLOT_MACHINE;
    public static Block BLACKJACK_TABLE;
    public static Block CHIP_TABLE;

    static {
        // GOLD BLOCKS
        registerBlock("cut_gold_block", () -> CUT_GOLD_BLOCK =
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(2.5F, 5.5F)
                            .sound(SoundType.METAL)
                    )
            );

        registerBlock("cut_gold_stairs", () -> CUT_GOLD_STAIRS =
                    new StairBlock(ModBlocks.CUT_GOLD_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CUT_GOLD_BLOCK)));

        registerBlock("cut_gold_slab", () -> CUT_GOLD_SLAB =
                    new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CUT_GOLD_BLOCK)));

        registerBlock("gold_bricks", () -> GOLD_BRICKS =
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(2.5F, 5.5F)
                            .sound(SoundType.METAL)
                    )
            );

        registerBlock("gold_brick_stairs", () -> GOLD_BRICK_STAIRS =
                    new StairBlock(ModBlocks.GOLD_BRICKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GOLD_BRICKS)));

        registerBlock("gold_brick_slab", () -> GOLD_BRICK_SLAB =
                    new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GOLD_BRICKS)));

        registerBlock("gold_brick_wall", () -> GOLD_BRICK_WALL =
                    new WallBlock(BlockBehaviour.Properties.ofFullCopy(GOLD_BRICKS)));

        registerBlock("gold_tiles", () -> GOLD_TILES =
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.GOLD)
                            .instrument(NoteBlockInstrument.BELL)
                            .requiresCorrectToolForDrops()
                            .strength(2.5F, 5.5F)
                            .sound(SoundType.METAL)
                    )
            );

        registerBlock("gold_tile_stairs", () -> GOLD_TILE_STAIRS =
                    new StairBlock(ModBlocks.GOLD_TILES.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(GOLD_TILES)));

        registerBlock("gold_tile_slab", () -> GOLD_TILE_SLAB =
                    new SlabBlock(BlockBehaviour.Properties.ofFullCopy(GOLD_TILES)));

        registerBlock("gold_tile_wall", () -> GOLD_TILE_WALL =
                    new WallBlock(BlockBehaviour.Properties.ofFullCopy(GOLD_TILES)));

        registerBlock("chiseled_gold_block", () -> CHISELED_GOLD_BLOCK =
                    new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CUT_GOLD_BLOCK)));

        registerBlock("gold_pillar", () -> GOLD_PILLAR =
                    new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CUT_GOLD_BLOCK)));

        registerBlock("heavy_gold_pillar", () -> HEAVY_GOLD_PILLAR =
                    new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CUT_GOLD_BLOCK)));

        registerBlock("gold_door", () -> GOLD_DOOR =
                    new DoorBlock(BlockSetType.GOLD, BlockBehaviour.Properties.ofFullCopy(CUT_GOLD_BLOCK).noOcclusion()));

        registerBlock("gold_trapdoor", () -> GOLD_TRAPDOOR =
                    new TrapDoorBlock(BlockSetType.GOLD, BlockBehaviour.Properties.ofFullCopy(CUT_GOLD_BLOCK).noOcclusion()));

        // DIAMOND BLOCKS
        registerBlock("cut_diamond_block", () -> CUT_DIAMOND_BLOCK =
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIAMOND)
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 5.5F)
                            .sound(SoundType.METAL)
                    )
            );

        registerBlock("cut_diamond_stairs", () -> CUT_DIAMOND_STAIRS =
                    new StairBlock(ModBlocks.CUT_DIAMOND_BLOCK.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(CUT_DIAMOND_BLOCK)));

        registerBlock("cut_diamond_slab", () -> CUT_DIAMOND_SLAB =
                    new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CUT_DIAMOND_BLOCK)));

        registerBlock("diamond_bricks", () -> DIAMOND_BRICKS =
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIAMOND)
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 5.5F)
                            .sound(SoundType.METAL)
                    )
            );

        registerBlock("diamond_brick_stairs", () -> DIAMOND_BRICK_STAIRS =
                    new StairBlock(ModBlocks.DIAMOND_BRICKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DIAMOND_BRICKS)));

        registerBlock("diamond_brick_slab", () -> DIAMOND_BRICK_SLAB =
                    new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DIAMOND_BRICKS)));

        registerBlock("diamond_brick_wall", () -> DIAMOND_BRICK_WALL =
                    new WallBlock(BlockBehaviour.Properties.ofFullCopy(DIAMOND_BRICKS)));

        registerBlock("diamond_tiles", () -> DIAMOND_TILES =
                    new Block(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.DIAMOND)
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 5.5F)
                            .sound(SoundType.METAL)
                    )
            );

        registerBlock("diamond_tile_stairs", () -> DIAMOND_TILE_STAIRS =
                    new StairBlock(ModBlocks.DIAMOND_TILES.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(DIAMOND_TILES)));

        registerBlock("diamond_tile_slab", () -> DIAMOND_TILE_SLAB =
                    new SlabBlock(BlockBehaviour.Properties.ofFullCopy(DIAMOND_TILES)));

        registerBlock("diamond_tile_wall", () -> DIAMOND_TILE_WALL =
                    new WallBlock(BlockBehaviour.Properties.ofFullCopy(DIAMOND_TILES)));

        registerBlock("chiseled_diamond_block", () -> CHISELED_DIAMOND_BLOCK =
                    new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CUT_DIAMOND_BLOCK)));

        registerBlock("diamond_pillar", () -> DIAMOND_PILLAR =
                    new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CUT_DIAMOND_BLOCK)));

        registerBlock("heavy_diamond_pillar", () -> HEAVY_DIAMOND_PILLAR =
                    new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(CUT_DIAMOND_BLOCK)));

        registerBlock("diamond_door", () -> DIAMOND_DOOR =
                    new DoorBlock(BlockSetType.GOLD, BlockBehaviour.Properties.ofFullCopy(CUT_DIAMOND_BLOCK).noOcclusion()));

        registerBlock("diamond_trapdoor", () -> DIAMOND_TRAPDOOR =
                    new TrapDoorBlock(BlockSetType.GOLD, BlockBehaviour.Properties.ofFullCopy(CUT_DIAMOND_BLOCK).noOcclusion()));

        // ECONOMY
        registerBlock("condensed_diamond_block", () -> CONDENSED_DIAMOND_BLOCK =
                    new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));

        registerBlock("charged_diamond_block", () -> CHARGED_DIAMOND_BLOCK =
                    new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));

        registerBlock("hypercharged_diamond_block", () -> HYPERCHARGED_DIAMOND_BLOCK =
                    new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));

        // ENTITIES
        registerBlock("gacha_machine", () -> GACHA_MACHINE =
                    new GachaMachineBlock(BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable()
                            .isValidSpawn(Blocks::never)
                            .noOcclusion()
                    )
            );

        registerBlock("pokemon_gacha_machine", () -> POKEMON_GACHA_MACHINE =
                    new PokemonGachaMachineBlock(BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable()
                            .isValidSpawn(Blocks::never)
                            .noOcclusion()
                    )
            );

        registerBlock("plushies_gacha_machine", () -> PLUSHIES_GACHA_MACHINE =
                    new PlushiesGachaMachineBlock(BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable()
                            .isValidSpawn(Blocks::never)
                            .noOcclusion()
                    )
            );

        registerBlock("event_gacha_machine", () -> EVENT_GACHA_MACHINE =
                    new EventGachaMachineBlock(BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable()
                            .isValidSpawn(Blocks::never)
                            .noOcclusion()
                    )
            );

        registerBlock("slot_machine", () -> SLOT_MACHINE =
                    new SlotMachineBlock(BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable()
                            .isValidSpawn(Blocks::never)
                            .lightLevel(state -> 14)
                            .noOcclusion()
                    )
            );

        registerBlock("blackjack_table", () -> BLACKJACK_TABLE =
                    new BlackjackTableBlock(BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable()
                            .isValidSpawn(Blocks::never)
                            .noOcclusion()
                    )
            );

        registerBlock("chip_table", () -> CHIP_TABLE =
                    new ChipTableBlock(BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F)
                            .noLootTable()
                            .isValidSpawn(Blocks::never)
                            .noOcclusion()
                    )
            );
    }

    // METHODS

    private static <T extends Block> void registerBlock(String name, Supplier<T> supplier) {
        DeferredBlock<T> block = BLOCKS.register(name, supplier);
        ITEMS.registerSimpleBlockItem(name, block);
    }

    public static void registerModBlocks(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        CasinoRocket.LOGGER.info("Registering Mod Blocks for " + CasinoRocket.MOD_ID);
    }

}

