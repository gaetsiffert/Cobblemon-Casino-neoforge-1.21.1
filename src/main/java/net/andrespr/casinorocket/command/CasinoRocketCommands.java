package net.andrespr.casinorocket.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class CasinoRocketCommands {

    private CasinoRocketCommands() {}

    // Registering global casinorocket commands
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher,
                                CommandBuildContext registryAccess,
                                Commands.CommandSelection environment) {

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("casinorocket");

        // Registering VillagerCommands
        root.then(VillagerCommands.buildSubcommand());
        // Registering GachaponCommands
        root.then(GachaponCommands.buildSubcommand());
        // Registering SlotsCommands
        root.then(SlotMachineCommands.buildSubcommand());
        // Registering BlackjackCommands
        root.then(BlackjackCommands.buildSubcommand());

        dispatcher.register(root);
    }

}

