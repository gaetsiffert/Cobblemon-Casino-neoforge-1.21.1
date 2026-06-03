package net.andrespr.casinorocket.datagen;

import com.cobblemon.mod.common.CobblemonItems;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.condition.MachinesCraftingEnabledCondition;
import net.andrespr.casinorocket.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeExporter) {

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
        slab(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_SLAB, ModBlocks.CUT_GOLD_BLOCK);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.GOLD_BRICKS);
        slab(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.GOLD_BRICKS);
        wall(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.GOLD_BRICKS);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_TILES);
        slab(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_TILES);
        wall(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_TILES);

        offerDoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.GOLD_DOOR, Items.GOLD_INGOT);
        offerTrapdoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.GOLD_TRAPDOOR, Items.GOLD_INGOT);

        // STONECUTTING FROM CUT_GOLD_BLOCK
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_GOLD_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICKS, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.CUT_GOLD_BLOCK,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.CUT_GOLD_BLOCK);

        // STONECUTTING FROM GOLD_BRICKS
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.GOLD_BRICKS,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.GOLD_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_BRICK_WALL, ModBlocks.GOLD_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILES, ModBlocks.GOLD_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_BRICKS,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.GOLD_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_BRICKS);

        // STONECUTTING FROM GOLD_TILES
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_TILES,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TILE_WALL, ModBlocks.GOLD_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_GOLD_BLOCK, ModBlocks.GOLD_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_PILLAR, ModBlocks.GOLD_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_TILES);

        // STONECUTTING FROM GOLD_PILLAR
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_GOLD_PILLAR, ModBlocks.GOLD_PILLAR);

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
        slab(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_SLAB, ModBlocks.CUT_DIAMOND_BLOCK);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.DIAMOND_BRICKS);
        slab(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.DIAMOND_BRICKS);
        wall(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.DIAMOND_BRICKS);

        offerStairsRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_TILES);
        slab(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_TILES);
        wall(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_TILES);

        offerDoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.DIAMOND_DOOR, Items.DIAMOND);
        offerTrapdoorRecipe(recipeExporter, RecipeCategory.REDSTONE, ModBlocks.DIAMOND_TRAPDOOR, Items.DIAMOND);

        // STONECUTTING FROM CUT_DIAMOND_BLOCK
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CUT_DIAMOND_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICKS, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.CUT_DIAMOND_BLOCK,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.CUT_DIAMOND_BLOCK);

        // STONECUTTING FROM DIAMOND_BRICKS
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.DIAMOND_BRICKS,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.DIAMOND_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_BRICK_WALL, ModBlocks.DIAMOND_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILES, ModBlocks.DIAMOND_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_BRICKS,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.DIAMOND_BRICKS);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_BRICKS);

        // STONECUTTING FROM DIAMOND_TILES
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_TILES,2);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TILE_WALL, ModBlocks.DIAMOND_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_DIAMOND_BLOCK, ModBlocks.DIAMOND_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_PILLAR, ModBlocks.DIAMOND_TILES);
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_TILES);

        // STONECUTTING FROM DIAMOND_PILLAR
        stonecutterResultFromBase(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEAVY_DIAMOND_PILLAR, ModBlocks.DIAMOND_PILLAR);

        // CASINO MACHINES
        offerMachineRecipes(recipeExporter.withConditions(MachinesCraftingEnabledCondition.INSTANCE));

    }

    private static void offerMachineRecipes(RecipeOutput exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.POKEMON_GACHA_MACHINE)
                .define('G', Items.GLASS)
                .define('I', Items.IRON_INGOT)
                .define('C', Items.CHEST)
                .define('R', Items.REDSTONE)
                .pattern("GGG")
                .pattern("ICI")
                .pattern("IRI")
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "pokemon_gacha_machine"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.GACHA_MACHINE)
                .define('I', Items.IRON_INGOT)
                .define('L', Items.LAPIS_LAZULI)
                .define('M', ModBlocks.POKEMON_GACHA_MACHINE)
                .pattern("ILI")
                .pattern("LML")
                .pattern("ILI")
                .unlockedBy(getHasName(ModBlocks.POKEMON_GACHA_MACHINE), has(ModBlocks.POKEMON_GACHA_MACHINE))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "gacha_machine"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PLUSHIES_GACHA_MACHINE)
                .define('W', Items.WHITE_WOOL)
                .define('Y', Items.YELLOW_DYE)
                .define('M', ModBlocks.POKEMON_GACHA_MACHINE)
                .pattern("WYW")
                .pattern("YMY")
                .pattern("WYW")
                .unlockedBy(getHasName(ModBlocks.POKEMON_GACHA_MACHINE), has(ModBlocks.POKEMON_GACHA_MACHINE))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "plushies_gacha_machine"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.EVENT_GACHA_MACHINE)
                .define('E', Items.EMERALD)
                .define('D', Items.DIAMOND)
                .define('M', ModBlocks.POKEMON_GACHA_MACHINE)
                .pattern("EDE")
                .pattern("DMD")
                .pattern("EDE")
                .unlockedBy(getHasName(ModBlocks.POKEMON_GACHA_MACHINE), has(ModBlocks.POKEMON_GACHA_MACHINE))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "event_gacha_machine"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SLOT_MACHINE)
                .define('G', Items.GLASS_PANE)
                .define('R', Items.REDSTONE)
                .define('I', Items.IRON_INGOT)
                .define('D', Items.DIAMOND)
                .pattern("GRG")
                .pattern("IDI")
                .pattern("IRI")
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "slot_machine"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.BLACKJACK_TABLE)
                .define('W', Items.GREEN_WOOL)
                .define('G', Items.GOLD_INGOT)
                .define('R', Items.REDSTONE)
                .define('I', Items.IRON_INGOT)
                .pattern("WWW")
                .pattern("GRG")
                .pattern("I I")
                .unlockedBy(getHasName(Items.GREEN_WOOL), has(Items.GREEN_WOOL))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "blackjack_table"));

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.CHIP_TABLE)
                .define('G', Items.GOLD_INGOT)
                .define('C', Items.CHEST)
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .pattern("GCG")
                .pattern("IRI")
                .pattern("G G")
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "chip_table"));
    }

    public static void offerDeconstruct(RecipeOutput exporter, RecipeCategory category, ItemLike output, ItemLike input, int count) {
        ShapelessRecipeBuilder.shapeless(category, output, count)
                .requires(input)
                .unlockedBy(getHasName(input), has(input))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, getSimpleRecipeName(output) + "_from_" + getSimpleRecipeName(input)));
    }

    public static void offer2x2Recipe(RecipeOutput exporter, RecipeCategory category, ItemLike output, ItemLike input, int count) {
        ShapedRecipeBuilder.shaped(category, output, count)
                .define('#', input)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(input), has(input))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, getSimpleRecipeName(output)));
    }

    public static void offer1x2Recipe(RecipeOutput exporter, RecipeCategory category, ItemLike output, ItemLike input, int count) {
        ShapedRecipeBuilder.shaped(category, output, count)
                .define('#', input)
                .pattern("#")
                .pattern("#")
                .unlockedBy(getHasName(input), has(input))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, getSimpleRecipeName(output)));
    }

    public static void offerStairsRecipe(RecipeOutput exporter, RecipeCategory category, ItemLike output, ItemLike input) {
        ShapedRecipeBuilder.shaped(category, output, 4)
                .define('#', input)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .unlockedBy(getHasName(input), has(input))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, getSimpleRecipeName(output)));
    }

    public static void offerDoorRecipe(RecipeOutput exporter, RecipeCategory category, ItemLike output, ItemLike input) {
        ShapedRecipeBuilder.shaped(category, output, 3)
                .define('#', input)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(input), has(input))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, getSimpleRecipeName(output)));
    }

    public static void offerTrapdoorRecipe(RecipeOutput exporter, RecipeCategory category, ItemLike output, ItemLike input) {
        ShapedRecipeBuilder.shaped(category, output, 2)
                .define('#', input)
                .pattern("###")
                .pattern("###")
                .unlockedBy(getHasName(input), has(input))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, getSimpleRecipeName(output)));
    }

    public static void offerReversibleCompactingRecipesBetter(
            RecipeOutput exporter, RecipeCategory reverseCategory, ItemLike baseItem, RecipeCategory compactingCategory, ItemLike compactItem
    ) {
        String compactingId = getSimpleRecipeName(compactItem);
        String reverseId = getSimpleRecipeName(baseItem) + "_from_" + compactingId;
        ShapelessRecipeBuilder.shapeless(reverseCategory, baseItem, 9)
                .requires(compactItem)
                .group(null)
                .unlockedBy(getHasName(compactItem), has(compactItem))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, reverseId));
        ShapedRecipeBuilder.shaped(compactingCategory, compactItem)
                .define('#', baseItem)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .group(null)
                .unlockedBy(getHasName(baseItem), has(baseItem))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, compactingId));
    }

}

