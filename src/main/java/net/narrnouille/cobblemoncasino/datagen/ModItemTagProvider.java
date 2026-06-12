package net.narrnouille.cobblemoncasino.datagen;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registriesFuture,
            CompletableFuture<TagsProvider.TagLookup<Block>> blockTags,
            ExistingFileHelper existingFileHelper
    ) {
        super(output, registriesFuture, blockTags, CobblemonCasino.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
    }

}


