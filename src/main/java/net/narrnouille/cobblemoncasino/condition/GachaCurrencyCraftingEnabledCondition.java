package net.narrnouille.cobblemoncasino.condition;

import com.mojang.serialization.MapCodec;
import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.neoforged.neoforge.common.conditions.ICondition;

public final class GachaCurrencyCraftingEnabledCondition implements ICondition {

    public static final GachaCurrencyCraftingEnabledCondition INSTANCE = new GachaCurrencyCraftingEnabledCondition();
    public static final MapCodec<GachaCurrencyCraftingEnabledCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    private GachaCurrencyCraftingEnabledCondition() {}

    @Override
    public boolean test(IContext context) {
        return CobblemonCasino.CONFIG == null || CobblemonCasino.CONFIG.generalConfig.enableGachaCurrencyCrafting;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "gacha_currency_crafting_enabled";
    }
}
