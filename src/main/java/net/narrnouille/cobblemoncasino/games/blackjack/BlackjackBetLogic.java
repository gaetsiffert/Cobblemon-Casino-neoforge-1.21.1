package net.narrnouille.cobblemoncasino.games.blackjack;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.data.PlayerBlackjackData;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.narrnouille.cobblemoncasino.network.s2c.sender.MachineBalanceSender;
import net.narrnouille.cobblemoncasino.screen.custom.common.BetScreenHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;

public final class BlackjackBetLogic {

    private BlackjackBetLogic() {}

    public static void handle(ServerPlayer player, MinecraftServer server) {
        if (!(player.containerMenu instanceof BetScreenHandler handler)) return;

        long amount = handler.getTotalMoney();
        if (amount <= 0) return;

        SimpleContainer inventory = handler.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            inventory.setItem(i, ItemStack.EMPTY);
        }

        PlayerCasinoBalanceData balanceData = PlayerCasinoBalanceData.get(Objects.requireNonNull(server));
        PlayerBlackjackData data = PlayerBlackjackData.get(Objects.requireNonNull(server));
        balanceData.addBalance(player.getUUID(), amount);
        data.addTotalDeposited(player.getUUID(), amount);

        CobblemonCasino.LOGGER.info("[Blackjack] User {} deposited {}", player.getGameProfile().getName(), amount);

        MachineBalanceSender.send(player, "blackjack", balanceData.getBalance(player.getUUID()));

        handler.slotsChanged(inventory);
    }

}

