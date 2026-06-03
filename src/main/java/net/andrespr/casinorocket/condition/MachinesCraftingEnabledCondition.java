package net.andrespr.casinorocket.condition;

import com.mojang.serialization.MapCodec;
import net.andrespr.casinorocket.CasinoRocket;
import net.neoforged.neoforge.common.conditions.ICondition;

public final class MachinesCraftingEnabledCondition implements ICondition {

    public static final MachinesCraftingEnabledCondition INSTANCE = new MachinesCraftingEnabledCondition();
    public static final MapCodec<MachinesCraftingEnabledCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    private MachinesCraftingEnabledCondition() {}

    @Override
    public boolean test(IContext context) {
        return CasinoRocket.CONFIG == null || CasinoRocket.CONFIG.generalConfig.enableMachinesCrafting;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "machines_crafting_enabled";
    }
}
