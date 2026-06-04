package net.andrespr.casinorocket.games.chip_table;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.machines.ChipTableConfig;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.util.MoneyCalculator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ChipTableExchange {

    private static final String RELIC_COIN = "cobblemon:relic_coin";
    private static final String HANDFUL_OF_RELIC_COINS = "casinorocket:handful_of_relic_coins";
    private static final String RELIC_COIN_POUCH = "cobblemon:relic_coin_pouch";
    private static final String STACK_OF_RELIC_COINS = "casinorocket:stack_of_relic_coins";

    private ChipTableExchange() {}

    public record ConversionResult(boolean success, List<ItemStack> outputs, Component message) {
        public static ConversionResult success(List<ItemStack> outputs) {
            return new ConversionResult(true, outputs, Component.translatable("message.casinorocket.chip_table_conversion_done"));
        }

        public static ConversionResult fail(String translationKey) {
            return new ConversionResult(false, List.of(), Component.translatable(translationKey));
        }
    }

    private record Denomination(Item item, long value) {}
    private record CurrencyStacks(List<ItemStack> stacks, long representedValue) {}

    public static boolean isAcceptedInput(ItemStack stack) {
        return stack.getItem() instanceof ChipItem || getRelicUnitValue(stack) > 0;
    }

    public static ConversionResult convert(List<ItemStack> inputs, ChipTableConversionMode mode) {
        List<ItemStack> nonEmptyInputs = inputs.stream()
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::copy)
                .toList();

        if (nonEmptyInputs.isEmpty()) {
            return ConversionResult.fail("message.casinorocket.chip_table_no_input");
        }

        ChipTableConfig config = CasinoRocket.CONFIG.chipTable;
        return switch (mode) {
            case CURRENCY_TO_CHIPS -> config.enable_currency_to_chips
                    ? toChips(nonEmptyInputs)
                    : ConversionResult.fail("message.casinorocket.chip_table_mode_disabled");
            case CHIPS_TO_RELIC_COINS -> config.enable_chips_to_relic_coins
                    ? toRelic(nonEmptyInputs)
                    : ConversionResult.fail("message.casinorocket.chip_table_mode_disabled");
        };
    }

    public static long getMoneyValue(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        if (stack.getItem() instanceof ChipItem chip) {
            return safeMultiply(chip.getValue(), stack.getCount());
        }
        long unitValue = getRelicUnitValue(stack);
        if (unitValue <= 0) return 0;
        return safeMultiply(unitValue, stack.getCount());
    }

    private static ConversionResult toChips(List<ItemStack> inputs) {
        long total = getAcceptedInputValue(inputs);
        if (total < 0) return ConversionResult.fail("message.casinorocket.chip_table_invalid_input");
        List<ItemStack> outputs = MoneyCalculator.calculateChipWithdraw(total);
        long represented = getStacksMoneyValue(outputs);
        if (represented != total) {
            return ConversionResult.fail("message.casinorocket.chip_table_unrepresentable_value");
        }
        return ConversionResult.success(outputs);
    }

    private static ConversionResult toRelic(List<ItemStack> inputs) {
        long total = getAcceptedInputValue(inputs);
        if (total < 0) return ConversionResult.fail("message.casinorocket.chip_table_invalid_input");

        CurrencyStacks currency = calculateRelicStacks(total);
        long remainder = Math.max(0, total - currency.representedValue());
        List<ItemStack> outputs = new ArrayList<>(currency.stacks());
        outputs.addAll(MoneyCalculator.calculateChipWithdraw(remainder));

        long represented = getStacksMoneyValue(outputs);
        if (represented != total) {
            return ConversionResult.fail("message.casinorocket.chip_table_unrepresentable_value");
        }
        return ConversionResult.success(outputs);
    }

    private static long getAcceptedInputValue(List<ItemStack> inputs) {
        long total = 0;
        for (ItemStack stack : inputs) {
            if (!isAcceptedInput(stack)) {
                return -1;
            }
            total = MoneyCalculator.safeAdd(total, getMoneyValue(stack), Long.MAX_VALUE);
        }
        return total;
    }

    private static CurrencyStacks calculateRelicStacks(long total) {
        List<Denomination> denominations = getRelicDenominations().stream()
                .filter(denomination -> denomination.value() > 0 && denomination.item() != Items.AIR)
                .sorted(Comparator.comparingLong(Denomination::value).reversed())
                .toList();

        long remaining = total;
        long represented = 0;
        List<ItemStack> stacks = new ArrayList<>();

        for (Denomination denomination : denominations) {
            long count = remaining / denomination.value();
            while (count > 0) {
                int stackSize = (int) Math.min(count, denomination.item().getDefaultMaxStackSize());
                ItemStack stack = new ItemStack(denomination.item(), stackSize);
                stacks.add(stack);
                long stackValue = safeMultiply(denomination.value(), stackSize);
                represented = MoneyCalculator.safeAdd(represented, stackValue, Long.MAX_VALUE);
                remaining -= stackValue;
                count -= stackSize;
            }
        }

        return new CurrencyStacks(stacks, represented);
    }

    private static List<Denomination> getRelicDenominations() {
        ChipTableConfig config = CasinoRocket.CONFIG.chipTable;
        return List.of(
                new Denomination(getItem(RELIC_COIN), config.relic_coin_value),
                new Denomination(getItem(HANDFUL_OF_RELIC_COINS), config.handful_of_relic_coins_value),
                new Denomination(getItem(RELIC_COIN_POUCH), config.relic_coin_pouch_value),
                new Denomination(getItem(STACK_OF_RELIC_COINS), config.stack_of_relic_coins_value)
        );
    }

    private static long getStacksMoneyValue(List<ItemStack> stacks) {
        long total = 0;
        for (ItemStack stack : stacks) {
            total = MoneyCalculator.safeAdd(total, getMoneyValue(stack), Long.MAX_VALUE);
        }
        return total;
    }

    private static long getRelicUnitValue(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (id == null) return 0;
        ChipTableConfig config = CasinoRocket.CONFIG.chipTable;
        return switch (id.toString()) {
            case RELIC_COIN -> config.relic_coin_value;
            case HANDFUL_OF_RELIC_COINS -> config.handful_of_relic_coins_value;
            case RELIC_COIN_POUCH -> config.relic_coin_pouch_value;
            case STACK_OF_RELIC_COINS -> config.stack_of_relic_coins_value;
            default -> 0;
        };
    }

    private static Item getItem(String itemId) {
        ResourceLocation id = ResourceLocation.parse(itemId);
        if (!BuiltInRegistries.ITEM.containsKey(id)) {
            return Items.AIR;
        }
        return BuiltInRegistries.ITEM.get(id);
    }

    private static long safeMultiply(long value, int count) {
        if (value <= 0 || count <= 0) return 0;
        if (value > Long.MAX_VALUE / count) return Long.MAX_VALUE;
        return value * count;
    }
}
