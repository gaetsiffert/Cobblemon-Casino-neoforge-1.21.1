package net.andrespr.casinorocket.block.entity.custom;

import net.andrespr.casinorocket.block.entity.ModBlockEntities;
import net.andrespr.casinorocket.games.chip_table.ChipTableExchange;
import net.andrespr.casinorocket.screen.opening.ChipTableOpenData;
import net.andrespr.casinorocket.screen.custom.chip_table.ChipTableScreenHandler;
import net.andrespr.casinorocket.util.CobbledollarsBalanceIntegration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;
import org.jetbrains.annotations.Nullable;

public class ChipTableEntity extends BlockEntity implements MenuProvider, IMenuProviderExtension {

    public ChipTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CHIP_TABLE_BE, pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.casinorocket.chip_table");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new ChipTableScreenHandler(syncId, playerInventory,
                createOpenData());
    }

    @Override
    public void writeClientSideData(AbstractContainerMenu menu, RegistryFriendlyByteBuf buffer) {
        ChipTableOpenData.CODEC.encode(buffer, createOpenData());
    }

    private ChipTableOpenData createOpenData() {
        return new ChipTableOpenData(this.worldPosition, false, ChipTableExchange.getCurrencyValues(),
                CobbledollarsBalanceIntegration.isLoaded());
    }

}


