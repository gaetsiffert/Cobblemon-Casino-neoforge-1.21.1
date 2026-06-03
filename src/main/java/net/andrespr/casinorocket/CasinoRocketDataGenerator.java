package net.andrespr.casinorocket;

import net.andrespr.casinorocket.datagen.*;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import java.util.List;
import java.util.Set;

public final class CasinoRocketDataGenerator {

    private CasinoRocketDataGenerator() {}

    public static void gatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            event.addProvider(new ModRecipeProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
            event.addProvider(new LootTableProvider(
                    event.getGenerator().getPackOutput(),
                    Set.of(),
                    List.of(new LootTableProvider.SubProviderEntry(ModLootTableProvider::new, LootContextParamSets.BLOCK)),
                    event.getLookupProvider()
            ));
            event.createBlockAndItemTags(
                    (output, lookupProvider) -> new ModBlockTagProvider(output, lookupProvider, event.getExistingFileHelper()),
                    (output, lookupProvider, blockTags) -> new ModItemTagProvider(output, lookupProvider, blockTags, event.getExistingFileHelper())
            );
            event.addProvider(new ModPoiTagProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), event.getExistingFileHelper()));
        }

        if (event.includeClient()) {
            event.addProvider(new ModModelProvider(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
        }
    }

}

