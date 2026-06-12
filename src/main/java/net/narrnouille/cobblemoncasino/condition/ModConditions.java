package net.narrnouille.cobblemoncasino.condition;

import com.mojang.serialization.MapCodec;
import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModConditions {

    private static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
            DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, CobblemonCasino.MOD_ID);

    private ModConditions() {}

    public static void register(IEventBus eventBus) {
        CONDITIONS.register("machines_crafting_enabled", () -> MachinesCraftingEnabledCondition.CODEC);
        CONDITIONS.register("gacha_currency_crafting_enabled", () -> GachaCurrencyCraftingEnabledCondition.CODEC);
        CONDITIONS.register(eventBus);
    }
}
