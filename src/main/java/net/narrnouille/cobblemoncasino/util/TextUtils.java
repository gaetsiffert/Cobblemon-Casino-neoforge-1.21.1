package net.narrnouille.cobblemoncasino.util;

import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.ChatFormatting;

public class TextUtils {

    public static String formatWithCommas(long number) {
        return NumberFormat.getInstance(Locale.US).format(number);
    }

    public static String formatCompact(long number) {
        double value;
        String suffix;

        if (number >= 1_000_000_000_000_000_000L) {
            value = number / 1_000_000_000_000_000_000.0;
            suffix = "Qi";
        } else if (number >= 1_000_000_000_000_000L) {
            value = number / 1_000_000_000_000_000.0;
            suffix = "Q";
        } else if (number >= 1_000_000_000_000L) {
            value = number / 1_000_000_000_000.0;
            suffix = "T";
        } else if (number >= 1_000_000_000) {
            value = number / 1_000_000_000.0;
            suffix = "B";
        } else if (number >= 1_000_000) {
            value = number / 1_000_000.0;
            suffix = "M";
        } else if (number >= 1_000) {
            value = number / 1_000.0;
            suffix = "K";
        } else {
            return String.valueOf(number);
        }

        value = Math.floor(value * 10.0) / 10.0;

        String formatted = String.format("%.1f%s", value, suffix);
        return formatted.replace(".0", "");
    }


    public static String formatLarge(long number) {

        if (number < 1_000L) {
            return String.format("%,d", number);
        }

        if (number < 1_000_000L) {
            String promoted = tryPromote(number, 1_000L, "K");
            if (promoted != null) return promoted;

            return String.format("%,d", number);
        }

        if (number < 1_000_000_000L) {
            String promoted = tryPromote(number, 1_000_000L, "M");
            if (promoted != null) return promoted;

            long valueK = number / 1_000L;
            return String.format("%,dK", valueK);
        }

        if (number < 1_000_000_000_000L) {
            String promoted = tryPromote(number, 1_000_000_000L, "B");
            if (promoted != null) return promoted;

            long valueM = number / 1_000_000L;
            return String.format("%,dM", valueM);
        }

        if (number < 1_000_000_000_000_000L) {
            String promoted = tryPromote(number, 1_000_000_000_000L, "T");
            if (promoted != null) return promoted;

            long valueB = number / 1_000_000_000L;
            return String.format("%,dB", valueB);
        }

        long valueT = number / 1_000_000_000_000L;
        return String.format("%,dT", valueT);
    }

    private static String tryPromote(long number, long unit, String suffix) {
        if (number < unit) return null;

        if (number % unit == 0) {
            double value = number / (double) unit;
            return String.format(Locale.US, "%.1f%s", value, suffix);
        }

        long tenth = unit / 10L;
        if (number % tenth == 0) {
            double value = number / (double) unit;
            return String.format(Locale.US, "%.1f%s", value, suffix);
        }

        return null;
    }

    public static String formatCompactNoDecimal(long number) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setGroupingUsed(true);
        nf.setMaximumFractionDigits(0);

        if (number >= 1_000_000_000_000_000_000L)
            return nf.format(number / 1_000_000_000_000_000L) + "Q";

        if (number >= 1_000_000_000_000_000L)
            return nf.format(number / 1_000_000_000_000L) + "T";

        if (number >= 1_000_000_000_000L)
            return nf.format(number / 1_000_000_000L) + "B";

        if (number >= 1_000_000_000)
            return nf.format(number / 1_000_000L) + "M";

        if (number >= 1_000_000)
            return nf.format(number / 1_000L) + "K";

        return nf.format(number);
    }

    public static ChatFormatting percentagesColor(double percentage) {
        if (percentage >= 5.01) {
            return ChatFormatting.GREEN;
        } else if (percentage >= 1.01) {
            return ChatFormatting.YELLOW;
        } else if (percentage >= 0.10) {
            return ChatFormatting.RED;
        } else {
            return ChatFormatting.DARK_RED;
        }
    }

    public static ChatFormatting rarityColor(String rarity) {
        return switch (rarity.toLowerCase(Locale.ROOT)) {
            case "common", "bonus" -> ChatFormatting.WHITE;
            case "uncommon" -> ChatFormatting.BLUE;
            case "rare" -> ChatFormatting.GOLD;
            case "ultrarare" -> ChatFormatting.LIGHT_PURPLE;
            case "legendary" -> ChatFormatting.RED;
            default -> ChatFormatting.GRAY;
        };
    }

    public static ChatFormatting coinColor(String rarity) {
        return switch (rarity.toLowerCase(Locale.ROOT)) {
            case "copper" -> ChatFormatting.RED;
            case "iron" -> ChatFormatting.WHITE;
            case "gold" -> ChatFormatting.GOLD;
            case "diamond" -> ChatFormatting.AQUA;
            default -> ChatFormatting.GRAY;
        };
    }

    public static ChatFormatting rankColors(int rank) {
        return switch (rank) {
            case 1 -> ChatFormatting.GOLD;
            case 2 -> ChatFormatting.AQUA;
            case 3 -> ChatFormatting.GREEN;
            default -> ChatFormatting.WHITE;
        };
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }

}

