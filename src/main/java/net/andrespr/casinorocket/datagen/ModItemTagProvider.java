package net.andrespr.casinorocket.datagen;

import net.andrespr.casinorocket.CasinoRocket;
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
        super(output, registriesFuture, blockTags, CasinoRocket.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
    }

}


