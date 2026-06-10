package net.andrespr.casinorocket.villager;

import com.google.common.collect.ImmutableSet;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

public class ModVillagers {
    private static final DeferredRegister<PoiType> POIS =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, CasinoRocket.MOD_ID);
    private static final DeferredRegister<VillagerProfession> PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, CasinoRocket.MOD_ID);

    public static final ResourceKey<PoiType> CHIP_TABLE_POI_KEY = registerPoiKey("chip_table_poi");
    public static PoiType CHIP_TABLE_POI;
    public static VillagerProfession CASINO_WORKER;

    static {
        registerPOI("chip_table_poi", () -> CHIP_TABLE_POI = createPoi(ModBlocks.CHIP_TABLE));
        registerProfession("casino_worker", () -> CASINO_WORKER = createProfession("casino_worker", CHIP_TABLE_POI_KEY));
    }

    private static VillagerProfession createProfession(String name, ResourceKey<PoiType> type) {
        return new VillagerProfession(name, entry -> entry.is(type),
                entry -> entry.is(type) && isChipTableCasinoVillagerConversionEnabled(),
                ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_LIBRARIAN);
    }

    public static boolean canVillagersAcquirePoi(Holder<PoiType> poi) {
        return isChipTableCasinoVillagerConversionEnabled() || !poi.is(CHIP_TABLE_POI_KEY);
    }

    public static boolean isChipTableCasinoVillagerConversionEnabled() {
        return CasinoRocket.CONFIG == null
                || CasinoRocket.CONFIG.generalConfig == null
                || CasinoRocket.CONFIG.generalConfig.enableChipTableCasinoVillagerConversion;
    }

    private static PoiType createPoi(Block block) {
        Set<BlockState> states = ImmutableSet.copyOf(block.getStateDefinition().getPossibleStates());
        return new PoiType(states, 1, 1);
    }

    private static void registerProfession(String name, Supplier<VillagerProfession> supplier) {
        PROFESSIONS.register(name, supplier);
    }

    private static void registerPOI(String name, Supplier<PoiType> supplier) {
        POIS.register(name, supplier);
    }

    private static ResourceKey<PoiType> registerPoiKey(String name) {
        return ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, name));
    }

    public static void registerVillagers(IEventBus eventBus) {
        POIS.register(eventBus);
        PROFESSIONS.register(eventBus);
        CasinoRocket.LOGGER.info("Registering villagers for " + CasinoRocket.MOD_ID);
    }
}
