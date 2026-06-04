package net.andrespr.casinorocket.util;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.List;

public final class CobbledollarsBankIntegration {

    private CobbledollarsBankIntegration() {}

    public static void registerChipsForBuyback() {
        if (!ModList.get().isLoaded("cobbledollars")) {
            return;
        }

        try {
            Class<?> cobbleDollarsClass = Class.forName("fr.harmex.cobbledollars.common.CobbleDollars");
            Field instanceField = cobbleDollarsClass.getField("INSTANCE");
            Object cobbleDollars = instanceField.get(null);

            Object bankConfig = cobbleDollarsClass.getMethod("getBankConfig").invoke(cobbleDollars);
            Object bank = bankConfig.getClass().getMethod("getBank").invoke(bankConfig);

            Method containsStack = bank.getClass().getMethod("contains", ItemStack.class);
            Class<?> offerClass = Class.forName("fr.harmex.cobbledollars.common.world.item.trading.shop.Offer");
            Constructor<?> offerConstructor = offerClass.getConstructor(ItemStack.class, BigInteger.class, int.class);

            @SuppressWarnings("unchecked")
            List<Object> offers = (List<Object>) bank;

            for (Item item : ModItems.ALL_CHIP_ITEMS) {
                ChipItem chip = (ChipItem) item;
                ItemStack stack = new ItemStack(chip);
                if ((boolean) containsStack.invoke(bank, stack)) {
                    continue;
                }

                long chipValue = CasinoRocket.CONFIG.generalConfig.getMoneyChipValue(BuiltInRegistries.ITEM.getKey(chip).getPath());
                offers.add(offerConstructor.newInstance(stack, BigInteger.valueOf(chipValue), -1));
            }
        } catch (ReflectiveOperationException | RuntimeException exception) {
            CasinoRocket.LOGGER.warn("Could not register Casino Rocket chips in CobbleDollars bank buyback.", exception);
        }
    }
}
