package net.narrnouille.cobblemoncasino.screen.opening;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;

public class MenuDataProvider<T> implements MenuProvider, IMenuProviderExtension {

    @FunctionalInterface
    public interface MenuFactory<T> {
        AbstractContainerMenu create(int syncId, Inventory inventory, Player player, T data);
    }

    private final Component title;
    private final T data;
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> codec;
    private final MenuFactory<T> factory;

    public MenuDataProvider(Component title, T data, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, MenuFactory<T> factory) {
        this.title = title;
        this.data = data;
        this.codec = codec;
        this.factory = factory;
    }

    @Override
    public Component getDisplayName() {
        return title;
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        return factory.create(syncId, inventory, player, data);
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        codec.encode(buffer, data);
    }
}


