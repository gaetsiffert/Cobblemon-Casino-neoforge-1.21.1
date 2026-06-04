package net.andrespr.casinorocket.util;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MoneyCalculator {

    public record ItemResult(String item, long amount) {}
    private record Denomination(String itemId, long value) {}

    public static ItemResult calculateItemAmount(String itemId, long value) {
        if (value <= 0) return new ItemResult(itemId, 1);

        List<Denomination> denominations = getDenominations(itemId);
        Denomination best = null;
        long bestAmount = 1;
        long bestTotal = Long.MAX_VALUE;

        for (Denomination denomination : denominations) {
            long amount = ceilDiv(value, denomination.value());
            if (amount <= 0 || amount > 64) continue;

            long total = amount * denomination.value();
            if (best == null
                    || total < bestTotal
                    || (total == bestTotal && denomination.value() > best.value())) {
                best = denomination;
                bestAmount = amount;
                bestTotal = total;
            }
        }

        if (best != null) return new ItemResult(best.itemId(), bestAmount);

        Denomination largest = denominations.getLast();
        return new ItemResult(largest.itemId(), 64);
    }

    private static List<Denomination> getDenominations(String itemId) {
        switch (itemId) {
            case "cobblemon:relic_coin" -> {
                return List.of(
                        new Denomination("cobblemon:relic_coin", 1),
                        new Denomination("casinorocket:handful_of_relic_coins", 4),
                        new Denomination("cobblemon:relic_coin_pouch", 9),
                        new Denomination("casinorocket:stack_of_relic_coins", 16),
                        new Denomination("cobblemon:relic_coin_sack", 81)
                );
            }
            default -> {
                return List.of(new Denomination(itemId, 1));
            }
        }
    }

    private static long ceilDiv(long value, long divisor) {
        return (value + divisor - 1) / divisor;
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

