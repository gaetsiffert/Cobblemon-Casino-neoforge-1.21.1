package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.games.records.CasinoLedgerService;
import net.andrespr.casinorocket.screen.custom.ledger.CasinoLedgerScreenHandler;
import net.andrespr.casinorocket.screen.opening.CasinoLedgerOpenData;
import net.andrespr.casinorocket.screen.opening.MenuDataProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CasinoLedgerItem extends Item {

    public CasinoLedgerItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer && serverPlayer.getServer() != null) {
            CasinoLedgerOpenData data = CasinoLedgerService.createSnapshot(serverPlayer.getServer(), serverPlayer);
            serverPlayer.openMenu(new MenuDataProvider<>(
                    Component.translatable("gui.casinorocket.casino_ledger"),
                    data,
                    CasinoLedgerOpenData.CODEC,
                    (syncId, inventory, menuPlayer, openData) -> new CasinoLedgerScreenHandler(syncId, inventory, openData)
            ));
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.casinorocket.casino_ledger"));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
