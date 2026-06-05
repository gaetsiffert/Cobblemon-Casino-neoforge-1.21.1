package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.screen.custom.chip_table.WalletScreenFactory;
import net.andrespr.casinorocket.sound.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WalletItem extends Item {

    public WalletItem(Properties settings) {
        super(settings.stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        if (!world.isClientSide) {
            world.playSound(null, player.blockPosition(), ModSounds.WALLET, SoundSource.PLAYERS,2.0F, 1.0F);
            player.openMenu(new WalletScreenFactory(hand));
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.casinorocket.wallet"));
        super.appendHoverText(stack, context, tooltip, type);
    }

}

