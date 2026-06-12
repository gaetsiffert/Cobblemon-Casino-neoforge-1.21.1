package net.narrnouille.cobblemoncasino.datagen;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.villager.ModVillagers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import java.util.concurrent.CompletableFuture;

public class ModPoiTagProvider extends TagsProvider<PoiType> {

    public ModPoiTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, ExistingFileHelper existingFileHelper) {
        super(output, Registries.POINT_OF_INTEREST_TYPE, registryLookupFuture, CobblemonCasino.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
                .add(ModVillagers.CHIP_TABLE_POI_KEY);
    }

}

