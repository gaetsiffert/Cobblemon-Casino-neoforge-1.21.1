package net.andrespr.casinorocket.config.machines;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = "machines/blackjack_table")
public class BlackjackTableConfig implements ConfigData {

    public List<Long> bet_amounts = new ArrayList<>(List.of(
            1L, 5L, 10L, 50L, 100L, 500L, 1_000L, 5_000L, 10_000L, 50_000L, 100_000L, 500_000L, 1_000_000L
    ));

    @Override
    public void validatePostLoad() {
        if (bet_amounts == null) bet_amounts = new ArrayList<>();

        List<Long> cleaned = bet_amounts.stream()
                .filter(v -> v != null && v > 0)
                .distinct()
                .sorted()
                .toList();

        bet_amounts.clear();
        bet_amounts.addAll(cleaned);

        if (bet_amounts.isEmpty()) bet_amounts.add(1L);
    }
}
