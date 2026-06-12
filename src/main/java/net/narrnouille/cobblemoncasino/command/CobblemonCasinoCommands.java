package net.narrnouille.cobblemoncasino.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class CobblemonCasinoCommands {

    private CobblemonCasinoCommands() {}

    // Registering global CobblemonCasino commands
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                CommandBuildContext registryAccess,
                                Commands.CommandSelection environment) {

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("cobblemoncasino");

        // Registering VillagerCommands
        root.then(VillagerCommands.buildSubcommand());
        // Registering GachaponCommands
        root.then(GachaponCommands.buildSubcommand());
        // Registering SlotsCommands
        root.then(SlotMachineCommands.buildSubcommand());
        // Registering BlackjackCommands
        root.then(BlackjackCommands.buildSubcommand());
        // Temporary debug commands
        root.then(CasinoLedgerDebugCommands.buildSubcommand());

        dispatcher.register(root);
    }

}

