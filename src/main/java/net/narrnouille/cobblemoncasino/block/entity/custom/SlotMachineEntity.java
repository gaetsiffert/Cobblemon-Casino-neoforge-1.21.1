package net.narrnouille.cobblemoncasino.block.entity.custom;

import net.narrnouille.cobblemoncasino.block.entity.ModBlockEntities;
import net.narrnouille.cobblemoncasino.data.PlayerCasinoBalanceData;
import net.narrnouille.cobblemoncasino.data.PlayerSlotMachineData;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineMenuScreenHandler;
import net.narrnouille.cobblemoncasino.screen.custom.slot.SlotMachineScreenHandler;
import net.narrnouille.cobblemoncasino.screen.opening.SlotMachineOpenData;
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
import java.util.Objects;
import java.util.UUID;

public class SlotMachineEntity extends BlockEntity implements MenuProvider, IMenuProviderExtension {

    private UUID currentUser;

    public SlotMachineEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SLOT_MACHINE_BE, pos, state);
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
            currentUser = null;
            setChanged();
        }
    }

    public void forceUnlock() {
        if (currentUser != null) {
            currentUser = null;
            setChanged();
        }
    }

    // === DISPLAY NAME ===
    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.cobblemoncasino.slot_machine");
    }

    // === OPENING DATA ===
    public SlotMachineOpenData getScreenOpeningData(ServerPlayer player) {
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        PlayerCasinoBalanceData balanceStorage = PlayerCasinoBalanceData.get(server);
        PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
        UUID uuid = player.getUUID();

        long balance = balanceStorage.getBalance(uuid);
        int betBase = storage.getBetBase(uuid);
        int lines = storage.getLinesMode(uuid);

        return new SlotMachineOpenData(this.worldPosition, "slots", balance, betBase, lines);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        SlotMachineOpenData data;
        if (menu instanceof SlotMachineScreenHandler handler) {
            data = new SlotMachineOpenData(this.worldPosition, handler.getMachineKey(), handler.getInitialBalance(), handler.getInitialBetBase(), handler.getInitialLinesMode());
        } else if (menu instanceof SlotMachineMenuScreenHandler handler) {
            data = new SlotMachineOpenData(this.worldPosition, handler.getMachineKey(), handler.getInitialBalance(), handler.getInitialBetBase(), handler.getInitialLinesMode());
        } else {
            data = new SlotMachineOpenData(this.worldPosition, "slots", 0L, 10, 1);
        }
        SlotMachineOpenData.CODEC.encode(buffer, data);
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        if (player instanceof ServerPlayer sp) {
            MinecraftServer server = Objects.requireNonNull(sp.getServer());
            PlayerCasinoBalanceData balanceStorage = PlayerCasinoBalanceData.get(server);
            PlayerSlotMachineData storage = PlayerSlotMachineData.get(server);
            UUID uuid = sp.getUUID();

            long balance = balanceStorage.getBalance(uuid);
            int betBase = storage.getBetBase(uuid);
            int lines = storage.getLinesMode(uuid);

            return new SlotMachineScreenHandler(syncId, inv, this.getBlockPos(), "slots", balance, betBase, lines);
        }

        return new SlotMachineScreenHandler(syncId, inv, this.getBlockPos(), "slots", 0L, 10, 1);
    }

}


