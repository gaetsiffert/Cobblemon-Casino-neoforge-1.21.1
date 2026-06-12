package net.narrnouille.cobblemoncasino.util;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

import java.lang.reflect.Method;
import java.math.BigInteger;

public final class CobbledollarsBalanceIntegration {

    private static Method getCobbleDollars;
    private static Method setCobbleDollars;
    private static boolean methodsResolved;

    private CobbledollarsBalanceIntegration() {}

    public static boolean isLoaded() {
        return ModList.get().isLoaded("cobbledollars");
    }

    public static boolean deposit(Player player, long amount) {
        if (amount <= 0 || !resolveMethods()) return false;

        try {
            BigInteger current = getBalance(player);
            setCobbleDollars.invoke(null, player, current.add(BigInteger.valueOf(amount)));
            return true;
        } catch (ReflectiveOperationException | RuntimeException exception) {
            CobblemonCasino.LOGGER.warn("Could not deposit CobbleDollars.", exception);
            return false;
        }
    }

    public static boolean withdraw(Player player, long amount) {
        if (amount <= 0 || !resolveMethods()) return false;

        try {
            BigInteger current = getBalance(player);
            BigInteger requested = BigInteger.valueOf(amount);
            if (current.compareTo(requested) < 0) {
                return false;
            }

            setCobbleDollars.invoke(null, player, current.subtract(requested));
            return true;
        } catch (ReflectiveOperationException | RuntimeException exception) {
            CobblemonCasino.LOGGER.warn("Could not withdraw CobbleDollars.", exception);
            return false;
        }
    }

    private static BigInteger getBalance(Player player) throws ReflectiveOperationException {
        Object value = getCobbleDollars.invoke(null, player);
        return value instanceof BigInteger balance ? balance : BigInteger.ZERO;
    }

    private static boolean resolveMethods() {
        if (!isLoaded()) return false;
        if (methodsResolved) return getCobbleDollars != null && setCobbleDollars != null;

        methodsResolved = true;
        try {
            Class<?> extensions = Class.forName("fr.harmex.cobbledollars.common.utils.extensions.PlayerExtensionKt");
            getCobbleDollars = extensions.getMethod("getCobbleDollars", Player.class);
            setCobbleDollars = extensions.getMethod("setCobbleDollars", Player.class, BigInteger.class);
            return true;
        } catch (ReflectiveOperationException | RuntimeException exception) {
            CobblemonCasino.LOGGER.warn("Could not resolve CobbleDollars player balance integration.", exception);
            return false;
        }
    }
}
