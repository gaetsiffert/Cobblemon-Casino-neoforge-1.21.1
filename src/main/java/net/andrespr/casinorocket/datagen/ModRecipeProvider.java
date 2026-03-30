package net.andrespr.casinorocket.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {

        // ECONOMY
        offerReversibleCompactingRecipesBetter(recipeExporter, RecipeCategory.MISC, ModItems.DIAMOND_NUGGET, RecipeCategory.MISC, Items.DIAMOND);

        offer2x2Recipe(recipeExporter, RecipeCategory.MISC, ModItems.CHARGED_DIAMOND, Items.DIAMOND, 1);
        offer2x2Recipe(recipeExporter, RecipeCategory.MISC, ModItems.HYPERCHARGED_DIAMOND, ModItems.CHARGED_DIAMOND, 1);

        offer2x2Recipe(recipeExporter, RecipeCategory.MISC, ModItems.HANDFUL_OF_RELIC_COINS, CobblemonItems.RELIC_COIN, 1);
        offer2x2Recipe(recipeExporter, RecipeCategory.MISC, ModItems.STACK_OF_RELIC_COINS, ModItems.HANDFUL_OF_RELIC_COINS, 1);

        offerReversibleCompactingRecipesBetter(recipeExporter, RecipeCategory.MISC, Items.DIAMOND_BLOCK, RecipeCategory.MISC, ModBlocks.CONDENSED_DIAMOND_BLOCK);
        offerReversibleCompactingRecipesBetter(recipeExporter, RecipeCategory.MISC, ModItems.CHARGED_DIAMOND, RecipeCategory.MISC, ModBlocks.CHARGED_DIAMOND_BLOCK);
        offerReversibleCompactingRecipesBetter(recipeExporter, RecipeCategory.MISC, ModItems.HYPERCHARGED_DIAMOND, RecipeCategory.MISC, ModBlocks.HYPERCHARGED_DIAMOND_BLOCK);

        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.DIAMOND, ModItems.CHARGED_DIAMOND, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, ModItems.CHARGED_DIAMOND, ModItems.HYPERCHARGED_DIAMOND, 4);

        offerDeconstruct(recipeExporter, RecipeCategory.MISC, CobblemonItems.RELIC_COIN, ModItems.HANDFUL_OF_RELIC_COINS, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, ModItems.HANDFUL_OF_RELIC_COINS, ModItems.STACK_OF_RELIC_COINS, 4);

        // GOLD BLOCKS CRAFTING TABLE RECIPES
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_BLOCK, Items.GOLD_NUGGET, 1);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICKS, ModBlocks.CUT_GOLD_BLOCK, 4);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.GOLD_BRICKS, 4);

        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.GOLD_NUGGET, ModBlocks.CUT_GOLD_BLOCK, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.GOLD_NUGGET, ModBlocks.GOLD_BRICKS, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, Items.GOLD_NUGGET, ModBlocks.GOLD_TILES, 4);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_BRICK_SLAB,1);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK, 2);
        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_PILLAR, 2);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_SLAB, ModBlocks.CUT_GOLD_BLOCK);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.GOLD_BRICKS);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.GOLD_BRICKS);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.GOLD_BRICKS);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_TILES);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_TILES);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_TILES);

        offerDoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.GOLD_DOOR, Items.GOLD_INGOT);
        offerTrapdoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.GOLD_TRAPDOOR, Items.GOLD_INGOT);

        // STONECUTTING FROM CUT_GOLD_BLOCK
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICKS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK);

        // STONECUTTING FROM GOLD_BRICKS
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.GOLD_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.GOLD_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_BRICKS);

        // STONECUTTING FROM GOLD_TILES
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_TILES,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.GOLD_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_TILES);

        // STONECUTTING FROM GOLD_PILLAR
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_PILLAR);

        // DIAMOND BLOCKS CRAFTING TABLE RECIPES
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_BLOCK, ModItems.DIAMOND_NUGGET, 1);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICKS, ModBlocks.CUT_DIAMOND_BLOCK, 4);
        offer2x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.DIAMOND_BRICKS, 4);

        offerDeconstruct(recipeExporter, RecipeCategory.MISC, ModItems.DIAMOND_NUGGET, ModBlocks.CUT_DIAMOND_BLOCK, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, ModItems.DIAMOND_NUGGET, ModBlocks.DIAMOND_BRICKS, 4);
        offerDeconstruct(recipeExporter, RecipeCategory.MISC, ModItems.DIAMOND_NUGGET, ModBlocks.DIAMOND_TILES, 4);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_BRICK_SLAB,1);

        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK, 2);
        offer1x2Recipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_PILLAR, 2);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_SLAB, ModBlocks.CUT_DIAMOND_BLOCK);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.DIAMOND_BRICKS);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.DIAMOND_BRICKS);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.DIAMOND_BRICKS);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_TILES);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_TILES);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_TILES);

        offerDoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.DIAMOND_DOOR, Items.DIAMOND);
        offerTrapdoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.DIAMOND_TRAPDOOR, Items.DIAMOND);

        // STONECUTTING FROM CUT_DIAMOND_BLOCK
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICKS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK);

        // STONECUTTING FROM DIAMOND_BRICKS
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.DIAMOND_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_BRICKS,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.DIAMOND_BRICKS);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_BRICKS);

        // STONECUTTING FROM DIAMOND_TILES
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_TILES,2);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.DIAMOND_TILES);
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_TILES);

        // STONECUTTING FROM DIAMOND_PILLAR
        offerStonecuttingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_PILLAR);

    }

    public static void offerDeconstruct(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input, int count) {
        ShapelessRecipeJsonBuilder.create(category, output, count)
                .input(input)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output) + "_from_" + getRecipeName(input)));
    }

    public static void offer2x2Recipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input, int count) {
        ShapedRecipeJsonBuilder.create(category, output, count)
                .input('#', input)
                .pattern("##")
                .pattern("##")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offer1x2Recipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input, int count) {
        ShapedRecipeJsonBuilder.create(category, output, count)
                .input('#', input)
                .pattern("#")
                .pattern("#")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offerStairsRecipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(category, output, 4)
                .input('#', input)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offerDoorRecipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(category, output, 3)
                .input('#', input)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offerTrapdoorRecipe(RecipeExporter exporter, RecipeCategory category, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(category, output, 2)
                .input('#', input)
                .pattern("###")
                .pattern("###")
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, Identifier.of(CasinoRocket.MOD_ID, getRecipeName(output)));
    }

    public static void offerReversibleCompactingRecipesBetter(
            RecipeExporter exporter, RecipeCategory reverseCategory, ItemConvertible baseItem, RecipeCategory compactingCategory, ItemConvertible compactItem
    ) {
        String compactingId = getRecipeName(compactItem);
        String reverseId = getRecipeName(baseItem) + "_from_" + compactingId;
        ShapelessRecipeJsonBuilder.create(reverseCategory, baseItem, 9)
                .input(compactItem)
                .group(null)
                .criterion(hasItem(compactItem), conditionsFromItem(compactItem))
                .offerTo(exporter, Identifier.of(reverseId));
        ShapedRecipeJsonBuilder.create(compactingCategory, compactItem)
                .input('#', baseItem)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group(null)
                .criterion(hasItem(baseItem), conditionsFromItem(baseItem))
                .offerTo(exporter, Identifier.of(compactingId));
    }

}