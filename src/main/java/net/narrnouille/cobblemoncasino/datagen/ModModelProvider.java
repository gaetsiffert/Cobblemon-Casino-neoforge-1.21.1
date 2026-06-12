package net.narrnouille.cobblemoncasino.datagen;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.block.ModBlocks;
import net.narrnouille.cobblemoncasino.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModModelProvider extends BlockStateProvider {

    public ModModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CobblemonCasino.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockFamily(ModBlocks.CUT_GOLD_BLOCK, ModBlocks.CUT_GOLD_STAIRS, ModBlocks.CUT_GOLD_SLAB, null);
        blockFamily(ModBlocks.GOLD_BRICKS, ModBlocks.GOLD_BRICK_STAIRS, ModBlocks.GOLD_BRICK_SLAB, ModBlocks.GOLD_BRICK_WALL);
        blockFamily(ModBlocks.GOLD_TILES, ModBlocks.GOLD_TILE_STAIRS, ModBlocks.GOLD_TILE_SLAB, ModBlocks.GOLD_TILE_WALL);
        column(ModBlocks.CHISELED_GOLD_BLOCK);
        column(ModBlocks.GOLD_PILLAR);
        column(ModBlocks.HEAVY_GOLD_PILLAR);
        door(ModBlocks.GOLD_DOOR);
        trapdoor(ModBlocks.GOLD_TRAPDOOR);

        blockFamily(ModBlocks.CUT_DIAMOND_BLOCK, ModBlocks.CUT_DIAMOND_STAIRS, ModBlocks.CUT_DIAMOND_SLAB, null);
        blockFamily(ModBlocks.DIAMOND_BRICKS, ModBlocks.DIAMOND_BRICK_STAIRS, ModBlocks.DIAMOND_BRICK_SLAB, ModBlocks.DIAMOND_BRICK_WALL);
        blockFamily(ModBlocks.DIAMOND_TILES, ModBlocks.DIAMOND_TILE_STAIRS, ModBlocks.DIAMOND_TILE_SLAB, ModBlocks.DIAMOND_TILE_WALL);
        column(ModBlocks.CHISELED_DIAMOND_BLOCK);
        column(ModBlocks.DIAMOND_PILLAR);
        column(ModBlocks.HEAVY_DIAMOND_PILLAR);
        door(ModBlocks.DIAMOND_DOOR);
        trapdoor(ModBlocks.DIAMOND_TRAPDOOR);

        flatItems(ModItems.ALL_CHIP_ITEMS.toArray(Item[]::new));

        flatItems(
                ModItems.COPPER_COIN,
                ModItems.IRON_COIN,
                ModItems.GOLD_COIN,
                ModItems.DIAMOND_COIN,
                ModItems.EVENT_COIN,
                ModItems.PRIMOGEM,
                ModItems.POKE_GACHAPON,
                ModItems.GREAT_GACHAPON,
                ModItems.ULTRA_GACHAPON,
                ModItems.MASTER_GACHAPON,
                ModItems.CHERISH_GACHAPON,
                ModItems.PREMIER_GACHAPON,
                ModItems.EVENT_GACHAPON,
                ModItems.POKEMON_POKE_GACHAPON,
                ModItems.POKEMON_GREAT_GACHAPON,
                ModItems.POKEMON_ULTRA_GACHAPON,
                ModItems.POKEMON_MASTER_GACHAPON,
                ModItems.POKEMON_CHERISH_GACHAPON,
                ModItems.POKEMON_PREMIER_GACHAPON,
                ModItems.POKEMON_EVENT_GACHAPON,
                ModItems.LITWICK_PIN,
                ModItems.STARYU_PIN,
                ModItems.BELLSPROUT_PIN,
                ModItems.TYROGUE_PIN,
                ModItems.SCYTHER_PIN,
                ModItems.EEVEE_PIN,
                ModItems.DRATINI_PIN,
                ModItems.ROTOM_PIN,
                ModItems.DITTO_PIN,
                ModItems.PORYGON_PIN,
                ModItems.WALLET,
                ModItems.FIRERED_GC_MUSIC_DISC,
                ModItems.HEARTGOLD_GC_MUSIC_DISC,
                ModItems.EMERALD_GC_MUSIC_DISC,
                ModItems.PLATINUM_GC_MUSIC_DISC,
                ModItems.DIAMOND_NUGGET,
                ModItems.HANDFUL_OF_RELIC_COINS,
                ModItems.STACK_OF_RELIC_COINS
        );
    }

    private void blockFamily(Block base, Block stairs, Block slab, Block wall) {
        cube(base);
        stairsBlock((StairBlock) stairs, blockTexture(base));
        itemModels().simpleBlockItem(stairs);
        slabBlock((SlabBlock) slab, blockTexture(base), blockTexture(base));
        itemModels().simpleBlockItem(slab);
        if (wall != null) {
            wallBlock((WallBlock) wall, blockTexture(base));
            itemModels().wallInventory(name(wall), blockTexture(base));
        }
    }

    private void cube(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }

    private void column(Block block) {
        axisBlock((RotatedPillarBlock) block, texture(name(block)), texture(name(block) + "_top"));
        itemModels().simpleBlockItem(block);
    }

    private void door(Block block) {
        String name = name(block);
        doorBlock((DoorBlock) block, texture(name + "_bottom"), texture(name + "_top"));
        itemModels().basicItem(block.asItem());
    }

    private void trapdoor(Block block) {
        String name = name(block);
        trapdoorBlock((TrapDoorBlock) block, blockTexture(block), true);
        itemModels().withExistingParent(name, modLoc("block/" + name + "_bottom"));
    }

    private void flatItems(Item... items) {
        for (Item item : items) {
            itemModels().basicItem(item);
        }
    }

    private ResourceLocation texture(String path) {
        return modLoc("block/" + path);
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
