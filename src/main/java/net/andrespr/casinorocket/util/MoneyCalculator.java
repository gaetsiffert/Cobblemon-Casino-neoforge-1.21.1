package net.andrespr.casinorocket.util;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.BillItem;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MoneyCalculator {

    public record ItemResult(String item, long amount) {}

    public static ItemResult calculateItemAmount(String itemId, long value) {

        if (value <= 0) return new ItemResult(itemId, 1);
        if (value <= 64 && value > 0) return new ItemResult(itemId, value);

        long valueDivided4 = Math.min(value / 4, 64);
        boolean isDivisibleBy4 = value % 4 == 0;
        if (isDivisibleBy4 && (value / 4) <= 64) return new ItemResult(getItemId(itemId, 4), valueDivided4);

        long valueDivided9 = Math.min(value / 9, 64);
        boolean isDivisibleBy9 = value % 9 == 0;
        if (isDivisibleBy9 && (value / 9) <= 64) return new ItemResult(getItemId(itemId, 9), valueDivided9);

        long valueDivided16 = Math.min(value / 16, 64);
        boolean isDivisibleBy16 = value % 16 == 0;
        if (isDivisibleBy16 && (value / 16) <= 64) return new ItemResult(getItemId(itemId, 16), valueDivided16);

        long valueDivided81 = Math.min(value / 81, 64);
        boolean isDivisibleBy81 = value % 81 == 0;
        if (isDivisibleBy81 && (value / 81) <= 64) return new ItemResult(getItemId(itemId, 81), valueDivided81);

        if (valueDivided4 < 64 && valueDivided4 > 0) return new ItemResult(getItemId(itemId, 4), valueDivided4);
        if (valueDivided9 < 64 && valueDivided9 > 0) return new ItemResult(getItemId(itemId, 9), valueDivided9);
        if (valueDivided16 < 64 && valueDivided16 > 0) return new ItemResult(getItemId(itemId, 16), valueDivided16);
        if (valueDivided81 > 0) return new ItemResult(getItemId(itemId, 81), valueDivided81);

        return new ItemResult(itemId, 1);

    }

    public static String getItemId(String itemId, int division) {
        switch (itemId) {
            case "minecraft:diamond" -> {
                return switch (division) {
                    case 4 -> "casinorocket:charged_diamond";
                    case 9 -> "minecraft:diamond_block";
                    case 16 -> "casinorocket:hypercharged_diamond";
                    case 81 -> "casinorocket:condensed_diamond_block";
                    default -> "minecraft:diamond";
                };
            }
            case "cobblemon:relic_coin" -> {
                return switch (division) {
                    case 4 -> "casinorocket:handful_of_relic_coins";
                    case 9 -> "cobblemon:relic_coin_pouch";
                    case 16 -> "casinorocket:stack_of_relic_coins";
                    case 81 -> "cobblemon:relic_coin_sack";
                    default -> "cobblemon:relic_coin";
                };
            }
            default -> {
                return "cobblemon:relic_coin";
            }
        }
    }

    public record MoneyResult(BillItem billType, int amount) {}

    public static MoneyResult calculateDenomination(long value) {

        if (value <= 0) return new MoneyResult(ModItems.BILL_10, 1);

        List<BillItem> bills = ModItems.ALL_BILL_ITEMS.stream()
                .map(item -> (BillItem) item)
                .sorted(Comparator.comparingLong(BillItem::getValue).reversed())
                .toList();

        for (BillItem bill : bills) {
            long billValue = bill.getValue();

            if (billValue > 0 && value % billValue == 0) {
                long amount = value / billValue;
                if (amount > 64) amount = 64;
                return new MoneyResult(bill, (int) amount);
            }
        }

        return new MoneyResult(ModItems.BILL_10, 1);
    }

    public static List<ItemStack> calculateChipWithdraw(long amount) {

        List<ItemStack> result = new ArrayList<>();
        if (amount <= 0) return result;

        List<ChipItem> chips = ModItems.ALL_CHIP_ITEMS.stream()
                .map(item -> (ChipItem) item)
                .sorted(Comparator.comparingLong(ChipItem::getValue).reversed())
                .toList();

        long remaining = amount;

        for (ChipItem chip : chips) {
            long value = chip.getValue();
            if (value <= 0) continue;

            long count = remaining / value;
            if (count <= 0) continue;

            while (count > 0) {
                int stackSize = (int) Math.min(64, count);
                result.add(new ItemStack(chip, stackSize));

                count -= stackSize;
                remaining -= (long) stackSize * value;
            }

            if (remaining <= 0) break;
        }

        return result;
    }

    public static long safeAdd(long a, long b, long max) {
        if (b > 0 && a > max - b) {
            return max;
        }
        return a + b;
    }

}

