package net.andrespr.casinorocket.games.chip_table;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.machines.ChipTableConfig;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.andrespr.casinorocket.util.CobbledollarsBalanceIntegration;
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
    private static final String RELIC_COIN_SACK = "cobblemon:relic_coin_sack";
    private static final String STACK_OF_RELIC_COINS = "casinorocket:stack_of_relic_coins";
    private static final int TABLE_OUTPUT_SLOTS = 18;

    private ChipTableExchange() {}

    public record ConversionResult(boolean success, List<ItemStack> outputs, Component message, long cobbledollarDelta) {
        public static ConversionResult success(List<ItemStack> outputs) {
            return success(outputs, 0);
        }

        public static ConversionResult success(List<ItemStack> outputs, long cobbledollarDelta) {
            return new ConversionResult(true, outputs, Component.translatable("message.casinorocket.chip_table_conversion_done"),
                    cobbledollarDelta);
        }

        public static ConversionResult fail(String translationKey) {
            return new ConversionResult(false, List.of(), Component.translatable(translationKey), 0);
        }
    }

    private record Denomination(String itemId, Item item, long value) {}
    private record ChipStacks(List<ItemStack> stacks, boolean outputFull) {}
    private record CurrencyStacks(List<ItemStack> stacks, long representedValue) {}
    public record CurrencyValue(String itemId, long value) {}
    public record ValueEntry(ItemStack stack, long value) {}

    public static boolean isAcceptedInput(ItemStack stack) {
        return stack.getItem() instanceof ChipItem || getRelicUnitValue(stack) > 0;
    }

    public static ConversionResult convert(List<ItemStack> inputs, ChipTableConversionMode mode, long cobbledollarAmount) {
        List<ItemStack> nonEmptyInputs = inputs.stream()
                .filter(stack -> !stack.isEmpty())
                .map(ItemStack::copy)
                .toList();

        ChipTableConfig config = CasinoRocket.CONFIG.chipTable;
        return switch (mode) {
            case CURRENCY_TO_CHIPS -> config.enable_currency_to_chips
                    ? toChips(nonEmptyInputs)
                    : ConversionResult.fail("message.casinorocket.chip_table_mode_disabled");
            case CHIPS_TO_RELIC_COINS -> config.enable_chips_to_relic_coins
                    ? toRelic(nonEmptyInputs)
                    : ConversionResult.fail("message.casinorocket.chip_table_mode_disabled");
            case TO_COBBLEDOLLAR -> config.enable_chips_to_cobbledollars
                    ? toCobbledollar(nonEmptyInputs)
                    : ConversionResult.fail("message.casinorocket.chip_table_mode_disabled");
            case FROM_COBBLEDOLLAR -> config.enable_cobbledollars_to_chips
                    ? fromCobbledollar(cobbledollarAmount)
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

    public static List<ValueEntry> getCurrencyValueEntries() {
        return toValueEntries(getCurrencyValues());
    }

    public static List<CurrencyValue> getCurrencyValues() {
        return getRelicDenominations().stream()
                .filter(denomination -> denomination.value() > 0 && denomination.item() != Items.AIR)
                .map(denomination -> new CurrencyValue(denomination.itemId(), denomination.value()))
                .toList();
    }

    public static List<ValueEntry> toValueEntries(List<CurrencyValue> values) {
        return values.stream()
                .map(value -> new ValueEntry(new ItemStack(getItem(value.itemId())), value.value()))
                .filter(entry -> !entry.stack().isEmpty() && entry.stack().getItem() != Items.AIR && entry.value() > 0)
                .toList();
    }

    private static ConversionResult toChips(List<ItemStack> inputs) {
        if (inputs.isEmpty()) {
            return ConversionResult.fail("message.casinorocket.chip_table_no_input");
        }

        long total = getAcceptedInputValue(inputs);
        if (total < 0) return ConversionResult.fail("message.casinorocket.chip_table_invalid_input");
        ChipStacks chipStacks = calculateChipStacks(total);
        if (chipStacks.outputFull()) return ConversionResult.fail("message.casinorocket.chip_table_output_full");
        List<ItemStack> outputs = chipStacks.stacks();
        long represented = getStacksMoneyValue(outputs);
        if (represented != total) {
            return ConversionResult.fail("message.casinorocket.chip_table_unrepresentable_value");
        }
        return ConversionResult.success(outputs);
    }

    private static ConversionResult toRelic(List<ItemStack> inputs) {
        if (inputs.isEmpty()) {
            return ConversionResult.fail("message.casinorocket.chip_table_no_input");
        }

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

    private static ConversionResult toCobbledollar(List<ItemStack> inputs) {
        if (!CobbledollarsBalanceIntegration.isLoaded()) {
            return ConversionResult.fail("message.casinorocket.chip_table_cobbledollars_unavailable");
        }
        if (inputs.isEmpty()) {
            return ConversionResult.fail("message.casinorocket.chip_table_no_input");
        }

        long total = getAcceptedInputValue(inputs);
        if (total < 0) return ConversionResult.fail("message.casinorocket.chip_table_invalid_input");
        return ConversionResult.success(List.of(), total);
    }

    private static ConversionResult fromCobbledollar(long amount) {
        if (!CobbledollarsBalanceIntegration.isLoaded()) {
            return ConversionResult.fail("message.casinorocket.chip_table_cobbledollars_unavailable");
        }
        if (amount <= 0) {
            return ConversionResult.fail("message.casinorocket.chip_table_cobbledollars_invalid_amount");
        }

        ChipStacks chipStacks = calculateChipStacks(amount);
        if (chipStacks.outputFull()) return ConversionResult.fail("message.casinorocket.chip_table_output_full");
        List<ItemStack> outputs = chipStacks.stacks();
        long represented = getStacksMoneyValue(outputs);
        if (represented != amount) {
            return ConversionResult.fail("message.casinorocket.chip_table_unrepresentable_value");
        }
        return ConversionResult.success(outputs, -amount);
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

    private static ChipStacks calculateChipStacks(long amount) {
        List<ItemStack> result = new ArrayList<>();
        if (amount <= 0) return new ChipStacks(result, false);

        List<ChipItem> chips = ModItems.ALL_CHIP_ITEMS.stream()
                .map(item -> (ChipItem) item)
                .filter(chip -> chip.getValue() > 0)
                .sorted(Comparator.comparingLong(ChipItem::getValue).reversed())
                .toList();

        long remaining = amount;

        for (ChipItem chip : chips) {
            long value = chip.getValue();
            long count = remaining / value;
            while (count > 0) {
                if (result.size() >= TABLE_OUTPUT_SLOTS) {
                    return new ChipStacks(result, true);
                }

                int stackSize = (int) Math.min(64, count);
                result.add(new ItemStack(chip, stackSize));
                long stackValue = safeMultiply(value, stackSize);
                remaining -= stackValue;
                count -= stackSize;
            }

            if (remaining <= 0) break;
        }

        return new ChipStacks(result, false);
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
                new Denomination(RELIC_COIN, getItem(RELIC_COIN), config.relic_coin_value),
                new Denomination(HANDFUL_OF_RELIC_COINS, getItem(HANDFUL_OF_RELIC_COINS), config.handful_of_relic_coins_value),
                new Denomination(RELIC_COIN_POUCH, getItem(RELIC_COIN_POUCH), config.relic_coin_pouch_value),
                new Denomination(STACK_OF_RELIC_COINS, getItem(STACK_OF_RELIC_COINS), config.stack_of_relic_coins_value),
                new Denomination(RELIC_COIN_SACK, getItem(RELIC_COIN_SACK), config.relic_coin_sack_value)
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
            case RELIC_COIN_SACK -> config.relic_coin_sack_value;
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
