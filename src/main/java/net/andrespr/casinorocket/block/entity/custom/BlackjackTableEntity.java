package net.andrespr.casinorocket.block.entity.custom;

import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.data.PlayerBlackjackData;
import net.andrespr.casinorocket.games.blackjack.BlackjackGameController;
import net.andrespr.casinorocket.network.s2c.sender.BlackjackStateSender;
import net.andrespr.casinorocket.screen.custom.blackjack.BlackjackTableScreenHandler;
import net.andrespr.casinorocket.screen.opening.BlackjackTableOpenData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BlackjackTableEntity extends BlockEntity implements MenuProvider, IMenuProviderExtension {

    private UUID currentUser;

    public BlackjackTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLACKJACK_TABLE_BE, pos, state);
    }

    // ===== LOCK API =====
    public boolean isInUse() {
        return currentUser != null;
    }

    public boolean isUsedBy(Player player) {
        return currentUser != null && currentUser.equals(player.getUUID());
    }

    public boolean tryLock(Player player) {
        UUID id = player.getUUID();
        if (currentUser == null) {
            currentUser = id;
            setChanged();
            return true;
        }
        return currentUser.equals(id);
    }

    public void unlock(Player player) {
        if (currentUser != null && currentUser.equals(player.getUUID())) {
            controllers.remove(player.getUUID());
            currentUser = null;
            setChanged();
        }
    }

    public void forceUnlock() {
        if (currentUser != null) {
            controllers.remove(currentUser);
            currentUser = null;
            setChanged();
        }
    }

    // === DISPLAY NAME ===
    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.casinorocket.blackjack_table");
    }

    // === OPENING DATA ===
    public BlackjackTableOpenData getScreenOpeningData(ServerPlayer player) {
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        PlayerBlackjackData storage = PlayerBlackjackData.get(server);
        UUID uuid = player.getUUID();

        long balance = storage.getBalance(uuid);
        int index = storage.getBetIndex(uuid);

        return new BlackjackTableOpenData(this.worldPosition, "blackjack", balance, index);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        BlackjackTableOpenData data;
        if (menu instanceof BlackjackTableScreenHandler handler) {
            data = new BlackjackTableOpenData(this.worldPosition, handler.getMachineKey(), handler.getInitialBalance(), handler.getInitialBetIndex());
        } else {
            data = new BlackjackTableOpenData(this.worldPosition, "blackjack", 0L, 0);
        }
        BlackjackTableOpenData.CODEC.encode(buffer, data);
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        long balance = 0;
        int index = 0;

        if (player instanceof ServerPlayer sp) {
            MinecraftServer server = Objects.requireNonNull(sp.getServer());
            PlayerBlackjackData storage = PlayerBlackjackData.get(server);
            UUID uuid = sp.getUUID();

            balance = storage.getBalance(uuid);
            index = storage.getBetIndex(uuid);

            sendState(sp);
        }

        return new BlackjackTableScreenHandler(syncId, inv, this.getBlockPos(), "blackjack", balance, index);
    }

    // === CONTROLLER ===
    private final Map<UUID, BlackjackGameController> controllers = new HashMap<>();

    public BlackjackGameController getOrCreateController(ServerPlayer player) {
        return controllers.computeIfAbsent(player.getUUID(), id ->
                new BlackjackGameController(id, PlayerBlackjackData.get(Objects.requireNonNull(player.getServer())))
        );
    }

    // === SEND STATE ===
    public void sendState(ServerPlayer player) {
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        PlayerBlackjackData storage = PlayerBlackjackData.get(server);
        BlackjackGameController controller = getOrCreateController(player);

        BlackjackStateSender.send(player, this.getBlockPos(), storage, controller);
    }

}


