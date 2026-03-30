package net.andrespr.casinorocket.games.slot;

import me.shedaniel.autoconfig.AutoConfig;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.config.CasinoRocketConfig;
import net.andrespr.casinorocket.config.machines.SlotMachineConfig;

import java.util.ArrayList;
import java.util.List;

public final class SlotMachineConstants {

    private static final List<Integer> FALLBACK_BET_VALUES_MONEY =
            List.of(10, 25, 50, 100, 250, 500, 1_000, 5_000, 10_000, 25_000, 50_000, 100_000, 250_000, 500_000, 1_000_000, 5_000_000);
    private static final List<Integer> FALLBACK_BET_VALUES_ITEMS =
            List.of(1, 2, 4, 8, 12, 16, 32, 48, 64, 96, 128, 192, 256);
    
    public static final long MAX_BALANCE = 9_000_000_000_000_000_000L;

    private SlotMachineConstants() {}

    private static SlotMachineConfig cfg() {
        CasinoRocketConfig root = AutoConfig.getConfigHolder(CasinoRocketConfig.class).getConfig();
        return root.slotMachine;
    }

    public static List<Integer> betValues() {
        if (SlotClientSynced.has()) {
            return SlotClientSynced.BET_VALUES;
        }

        try {
            SlotMachineConfig c = cfg();
            if (useMoney()) {
                List<Integer> v = c.bet_amounts_in_money;
                return (v == null || v.isEmpty()) ? FALLBACK_BET_VALUES_MONEY : v;
            } else {
                List<Integer> v = c.bet_amounts_in_items;
                return (v == null || v.isEmpty()) ? FALLBACK_BET_VALUES_ITEMS : v;
            }
        } catch (Exception e) {
            return useMoney() ? FALLBACK_BET_VALUES_MONEY : FALLBACK_BET_VALUES_ITEMS;
        }
    }

    public static int defaultBetBase() {
        List<Integer> v = betValues();
        return v.isEmpty() ? 1 : v.getFirst();
    }

    public static int defaultLinesMode() {
        return 1;
    }

    public static int getBetMultiplierForMode(int mode) {

        if (SlotClientSynced.has()) {
            return switch (mode) {
                case 2 -> SlotClientSynced.MODE2;
                case 3 -> SlotClientSynced.MODE3;
                default -> SlotClientSynced.MODE1;
            };
        }

        try {
            SlotMachineConfig c = cfg();
            return switch (mode) {
                case 2 -> Math.max(1, c.bet_multipliers.mode2);
                case 3 -> Math.max(1, c.bet_multipliers.mode3);
                default -> Math.max(1, c.bet_multipliers.mode1);
            };
        } catch (Exception e) {
            return switch (mode) {
                case 2 -> 3;
                case 3 -> 5;
                default -> 1;
            };
        }

    }

    private static boolean useMoney() {
        return CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive();
    }

}