package net.narrnouille.cobblemoncasino.games.slot;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.narrnouille.cobblemoncasino.network.s2c.sender.MachineBalanceSender;
import net.narrnouille.cobblemoncasino.util.MoneyCalculator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import java.util.UUID;

public final class SlotsWithdrawLogic {

    private SlotsWithdrawLogic() {}

    public static void handle(ServerPlayer player, MinecraftServer server) {
        PlayerCasinoBalanceData storage = PlayerCasinoBalanceData.get(server);
        UUID uuid = player.getUUID();

        long balance = storage.getBalance(uuid);
        if (balance <= 0) return;

        var stacks = MoneyCalculator.calculateChipWithdraw(balance);

        for (ItemStack stack : stacks) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }

        storage.setBalance(uuid, 0);
        CobblemonCasino.LOGGER.info("[SlotMachine] User {} withdrew {}", player.getGameProfile().getName(), balance);

        MachineBalanceSender.send(player, "slots", 0);
    }

}

