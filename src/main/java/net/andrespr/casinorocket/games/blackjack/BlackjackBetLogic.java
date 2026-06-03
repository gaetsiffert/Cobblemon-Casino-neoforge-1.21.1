package net.andrespr.casinorocket.games.blackjack;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.network.s2c.sender.MachineBalanceSender;
import net.andrespr.casinorocket.screen.custom.common.BetScreenHandler;
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

        PlayerBlackjackData data = PlayerBlackjackData.get(Objects.requireNonNull(server));
        data.addBalance(player.getUUID(), amount);
        data.addTotalDeposited(player.getUUID(), amount);

        CasinoRocket.LOGGER.info("[Blackjack] User {} deposited {}", player.getGameProfile().getName(), amount);

        MachineBalanceSender.send(player, "blackjack", data.getBalance(player.getUUID()));

        handler.slotsChanged(inventory);
    }

}

