package net.narrnouille.cobblemoncasino.screen.custom.chip_table;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.narrnouille.cobblemoncasino.screen.opening.ChipTableOpenData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;

public class WalletScreenFactory implements MenuProvider, IMenuProviderExtension {

    private final InteractionHand hand;

    public WalletScreenFactory(InteractionHand hand) {
        this.hand = hand;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.cobblemoncasino.wallet");
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        return new ChipTableScreenHandler(syncId, inventory, new ChipTableOpenData(BlockPos.ZERO, true, hand));
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        ChipTableOpenData.CODEC.encode(buffer, new ChipTableOpenData(BlockPos.ZERO, true, hand));
    }

}


