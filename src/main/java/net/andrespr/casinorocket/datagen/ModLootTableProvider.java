package net.andrespr.casinorocket.datagen;

import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.block.custom.EventGachaMachineBlock;
import net.andrespr.casinorocket.block.custom.GachaMachineBlock;
import net.andrespr.casinorocket.block.custom.PlushiesGachaMachineBlock;
import net.andrespr.casinorocket.block.custom.PokemonGachaMachineBlock;
import net.andrespr.casinorocket.block.custom.SlotMachineBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends BlockLootSubProvider {

    private static final List<Block> KNOWN_BLOCKS = List.of(
            ModBlocks.CUT_GOLD_BLOCK,
            ModBlocks.CUT_GOLD_STAIRS,
            ModBlocks.CUT_GOLD_SLAB,
            ModBlocks.GOLD_BRICKS,
            ModBlocks.GOLD_BRICK_STAIRS,
            ModBlocks.GOLD_BRICK_SLAB,
            ModBlocks.GOLD_BRICK_WALL,
            ModBlocks.GOLD_TILES,
            ModBlocks.GOLD_TILE_STAIRS,
            ModBlocks.GOLD_TILE_SLAB,
            ModBlocks.GOLD_TILE_WALL,
            ModBlocks.CHISELED_GOLD_BLOCK,
            ModBlocks.GOLD_PILLAR,
            ModBlocks.HEAVY_GOLD_PILLAR,
            ModBlocks.GOLD_DOOR,
            ModBlocks.GOLD_TRAPDOOR,
            ModBlocks.CUT_DIAMOND_BLOCK,
            ModBlocks.CUT_DIAMOND_STAIRS,
            ModBlocks.CUT_DIAMOND_SLAB,
            ModBlocks.DIAMOND_BRICKS,
            ModBlocks.DIAMOND_BRICK_STAIRS,
            ModBlocks.DIAMOND_BRICK_SLAB,
            ModBlocks.DIAMOND_BRICK_WALL,
            ModBlocks.DIAMOND_TILES,
            ModBlocks.DIAMOND_TILE_STAIRS,
            ModBlocks.DIAMOND_TILE_SLAB,
            ModBlocks.DIAMOND_TILE_WALL,
            ModBlocks.CHISELED_DIAMOND_BLOCK,
            ModBlocks.DIAMOND_PILLAR,
            ModBlocks.HEAVY_DIAMOND_PILLAR,
            ModBlocks.DIAMOND_DOOR,
            ModBlocks.DIAMOND_TRAPDOOR,
            ModBlocks.GACHA_MACHINE,
            ModBlocks.POKEMON_GACHA_MACHINE,
            ModBlocks.PLUSHIES_GACHA_MACHINE,
            ModBlocks.EVENT_GACHA_MACHINE,
            ModBlocks.SLOT_MACHINE,
            ModBlocks.BLACKJACK_TABLE,
            ModBlocks.CHIP_TABLE,
            ModBlocks.DECORATIVE_CHIP,
            ModBlocks.DECORATIVE_CHIP_STACK1,
            ModBlocks.DECORATIVE_CHIP_STACK2
    );

    public ModLootTableProvider(HolderLookup.Provider registries) {
        super(Set.<Item>of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    public void generate() {
        // ADDING DROPS FOR GOLD BLOCKS
        dropSelf(ModBlocks.CUT_GOLD_BLOCK);
        dropSelf(ModBlocks.CUT_GOLD_STAIRS);
        add(ModBlocks.CUT_GOLD_SLAB, createSlabItemTable(ModBlocks.CUT_GOLD_SLAB));
        dropSelf(ModBlocks.GOLD_BRICKS);
        dropSelf(ModBlocks.GOLD_BRICK_STAIRS);
        add(ModBlocks.GOLD_BRICK_SLAB, createSlabItemTable(ModBlocks.GOLD_BRICK_SLAB));
        dropSelf(ModBlocks.GOLD_BRICK_WALL);
        dropSelf(ModBlocks.GOLD_TILES);
        dropSelf(ModBlocks.GOLD_TILE_STAIRS);
        add(ModBlocks.GOLD_TILE_SLAB, createSlabItemTable(ModBlocks.GOLD_TILE_SLAB));
        dropSelf(ModBlocks.GOLD_TILE_WALL);
        dropSelf(ModBlocks.CHISELED_GOLD_BLOCK);
        dropSelf(ModBlocks.GOLD_PILLAR);
        dropSelf(ModBlocks.HEAVY_GOLD_PILLAR);
        add(ModBlocks.GOLD_DOOR, createDoorTable(ModBlocks.GOLD_DOOR));
        dropSelf(ModBlocks.GOLD_TRAPDOOR);
        // ADDING DROPS FOR DIAMOND BLOCKS
        dropSelf(ModBlocks.CUT_DIAMOND_BLOCK);
        dropSelf(ModBlocks.CUT_DIAMOND_STAIRS);
        add(ModBlocks.CUT_DIAMOND_SLAB, createSlabItemTable(ModBlocks.CUT_DIAMOND_SLAB));
        dropSelf(ModBlocks.DIAMOND_BRICKS);
        dropSelf(ModBlocks.DIAMOND_BRICK_STAIRS);
        add(ModBlocks.DIAMOND_BRICK_SLAB, createSlabItemTable(ModBlocks.DIAMOND_BRICK_SLAB));
        dropSelf(ModBlocks.DIAMOND_BRICK_WALL);
        dropSelf(ModBlocks.DIAMOND_TILES);
        dropSelf(ModBlocks.DIAMOND_TILE_STAIRS);
        add(ModBlocks.DIAMOND_TILE_SLAB, createSlabItemTable(ModBlocks.DIAMOND_TILE_SLAB));
        dropSelf(ModBlocks.DIAMOND_TILE_WALL);
        dropSelf(ModBlocks.CHISELED_DIAMOND_BLOCK);
        dropSelf(ModBlocks.DIAMOND_PILLAR);
        dropSelf(ModBlocks.HEAVY_DIAMOND_PILLAR);
        add(ModBlocks.DIAMOND_DOOR, createDoorTable(ModBlocks.DIAMOND_DOOR));
        dropSelf(ModBlocks.DIAMOND_TRAPDOOR);

        // ADDING DROPS FOR CASINO MACHINES
        add(ModBlocks.GACHA_MACHINE, createSinglePropConditionTable(ModBlocks.GACHA_MACHINE, GachaMachineBlock.HALF, DoubleBlockHalf.LOWER));
        add(ModBlocks.POKEMON_GACHA_MACHINE, createSinglePropConditionTable(ModBlocks.POKEMON_GACHA_MACHINE, PokemonGachaMachineBlock.HALF, DoubleBlockHalf.LOWER));
        add(ModBlocks.PLUSHIES_GACHA_MACHINE, createSinglePropConditionTable(ModBlocks.PLUSHIES_GACHA_MACHINE, PlushiesGachaMachineBlock.HALF, DoubleBlockHalf.LOWER));
        add(ModBlocks.EVENT_GACHA_MACHINE, createSinglePropConditionTable(ModBlocks.EVENT_GACHA_MACHINE, EventGachaMachineBlock.HALF, DoubleBlockHalf.LOWER));
        add(ModBlocks.SLOT_MACHINE, createSinglePropConditionTable(ModBlocks.SLOT_MACHINE, SlotMachineBlock.HALF, DoubleBlockHalf.LOWER));
        dropSelf(ModBlocks.BLACKJACK_TABLE);
        dropSelf(ModBlocks.CHIP_TABLE);
        dropSelf(ModBlocks.DECORATIVE_CHIP);
        dropSelf(ModBlocks.DECORATIVE_CHIP_STACK1);
        dropSelf(ModBlocks.DECORATIVE_CHIP_STACK2);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return KNOWN_BLOCKS;
    }
}
