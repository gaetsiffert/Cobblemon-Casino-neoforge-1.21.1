package net.andrespr.casinorocket.datagen;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, ExistingFileHelper existingFileHelper) {
        super(output, registriesFuture, CasinoRocket.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                // ADDING GOLD BLOCKS
                .add(ModBlocks.CUT_GOLD_BLOCK)
                .add(ModBlocks.CUT_GOLD_STAIRS)
                .add(ModBlocks.CUT_GOLD_SLAB)
                .add(ModBlocks.GOLD_BRICKS)
                .add(ModBlocks.GOLD_BRICK_STAIRS)
                .add(ModBlocks.GOLD_BRICK_SLAB)
                .add(ModBlocks.GOLD_BRICK_WALL)
                .add(ModBlocks.GOLD_TILES)
                .add(ModBlocks.GOLD_TILE_STAIRS)
                .add(ModBlocks.GOLD_TILE_SLAB)
                .add(ModBlocks.GOLD_TILE_WALL)
                .add(ModBlocks.CHISELED_GOLD_BLOCK)
                .add(ModBlocks.GOLD_PILLAR)
                .add(ModBlocks.HEAVY_GOLD_PILLAR)
                .add(ModBlocks.GOLD_DOOR)
                .add(ModBlocks.GOLD_TRAPDOOR)
                // ADDING DIAMOND BLOCKS
                .add(ModBlocks.CUT_DIAMOND_BLOCK)
                .add(ModBlocks.CUT_DIAMOND_STAIRS)
                .add(ModBlocks.CUT_DIAMOND_SLAB)
                .add(ModBlocks.DIAMOND_BRICKS)
                .add(ModBlocks.DIAMOND_BRICK_STAIRS)
                .add(ModBlocks.DIAMOND_BRICK_SLAB)
                .add(ModBlocks.DIAMOND_BRICK_WALL)
                .add(ModBlocks.DIAMOND_TILES)
                .add(ModBlocks.DIAMOND_TILE_STAIRS)
                .add(ModBlocks.DIAMOND_TILE_SLAB)
                .add(ModBlocks.DIAMOND_TILE_WALL)
                .add(ModBlocks.CHISELED_DIAMOND_BLOCK)
                .add(ModBlocks.DIAMOND_PILLAR)
                .add(ModBlocks.HEAVY_DIAMOND_PILLAR)
                .add(ModBlocks.DIAMOND_DOOR)
                .add(ModBlocks.DIAMOND_TRAPDOOR)
                // ADDING CASINO MACHINES
                .add(ModBlocks.GACHA_MACHINE)
                .add(ModBlocks.POKEMON_GACHA_MACHINE)
                .add(ModBlocks.PLUSHIES_GACHA_MACHINE)
                .add(ModBlocks.EVENT_GACHA_MACHINE)
                .add(ModBlocks.SLOT_MACHINE)
                .add(ModBlocks.BLACKJACK_TABLE)
                .add(ModBlocks.CHIP_TABLE);

        tag(BlockTags.NEEDS_IRON_TOOL)
                // ADDING GOLD BLOCKS
                .add(ModBlocks.CUT_GOLD_BLOCK)
                .add(ModBlocks.CUT_GOLD_STAIRS)
                .add(ModBlocks.CUT_GOLD_SLAB)
                .add(ModBlocks.GOLD_BRICKS)
                .add(ModBlocks.GOLD_BRICK_STAIRS)
                .add(ModBlocks.GOLD_BRICK_SLAB)
                .add(ModBlocks.GOLD_BRICK_WALL)
                .add(ModBlocks.GOLD_TILES)
                .add(ModBlocks.GOLD_TILE_STAIRS)
                .add(ModBlocks.GOLD_TILE_SLAB)
                .add(ModBlocks.GOLD_TILE_WALL)
                .add(ModBlocks.CHISELED_GOLD_BLOCK)
                .add(ModBlocks.GOLD_PILLAR)
                .add(ModBlocks.HEAVY_GOLD_PILLAR)
                .add(ModBlocks.GOLD_DOOR)
                .add(ModBlocks.GOLD_TRAPDOOR)
                // ADDING DIAMOND BLOCKS
                .add(ModBlocks.CUT_DIAMOND_BLOCK)
                .add(ModBlocks.CUT_DIAMOND_STAIRS)
                .add(ModBlocks.CUT_DIAMOND_SLAB)
                .add(ModBlocks.DIAMOND_BRICKS)
                .add(ModBlocks.DIAMOND_BRICK_STAIRS)
                .add(ModBlocks.DIAMOND_BRICK_SLAB)
                .add(ModBlocks.DIAMOND_BRICK_WALL)
                .add(ModBlocks.DIAMOND_TILES)
                .add(ModBlocks.DIAMOND_TILE_STAIRS)
                .add(ModBlocks.DIAMOND_TILE_SLAB)
                .add(ModBlocks.DIAMOND_TILE_WALL)
                .add(ModBlocks.CHISELED_DIAMOND_BLOCK)
                .add(ModBlocks.DIAMOND_PILLAR)
                .add(ModBlocks.HEAVY_DIAMOND_PILLAR)
                .add(ModBlocks.DIAMOND_DOOR)
                .add(ModBlocks.DIAMOND_TRAPDOOR);

        tag(BlockTags.WALLS)
                .add(ModBlocks.GOLD_BRICK_WALL)
                .add(ModBlocks.GOLD_TILE_WALL)
                .add(ModBlocks.DIAMOND_BRICK_WALL)
                .add(ModBlocks.DIAMOND_TILE_WALL);
    }

}


