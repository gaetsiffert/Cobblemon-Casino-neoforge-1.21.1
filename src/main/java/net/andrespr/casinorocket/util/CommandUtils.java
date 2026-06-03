package net.andrespr.casinorocket.util;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.andrespr.casinorocket.data.GachaDataStorage;
import net.andrespr.casinorocket.data.GachaStats;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

public class CommandUtils {

    public static CompletableFuture<Suggestions> suggestPlayerNames(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        MinecraftServer server = context.getSource().getServer();
        GachaDataStorage data = GachaDataStorage.get(server);

        // TreeSet evita duplicados y ordena automáticamente
        Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        // === Nombres de jugadores online ===
        for (ServerPlayer online : server.getPlayerList().getPlayers()) {
            names.add(online.getName().getString());
        }

        // === Nombres guardados en stats persistentes ===
        for (GachaStats stats : data.playerStats.values()) {
            String name = stats.getPlayerName();
            if (name != null && !name.isEmpty()) {
                names.add(name);
            }
        }

        // === Sugerir ===
        for (String s : names) {
            builder.suggest(s);
        }

        return builder.buildFuture();
    }

}


