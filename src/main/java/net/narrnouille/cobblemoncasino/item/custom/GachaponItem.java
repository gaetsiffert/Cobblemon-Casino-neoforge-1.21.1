package net.narrnouille.cobblemoncasino.item.custom;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.item.ModItems;
import net.narrnouille.cobblemoncasino.sound.ModSounds;
import net.narrnouille.cobblemoncasino.util.CobblemonCasinoLogger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.narrnouille.cobblemoncasino.games.gachapon.GachaponUtils;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class GachaponItem extends Item {

    private final String poolKey;

    public GachaponItem(Properties settings, String poolKey) {
        super(settings);
        this.poolKey = poolKey;
        ModItems.ALL_GACHAPON_ITEMS.add(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (!world.isClientSide && user instanceof ServerPlayer player) {
            ItemStack reward = GachaponUtils.pickItemReward(world.random, poolKey);

            if (!reward.isEmpty()) {
                String itemName = reward.getItem().getName(reward).getString();
                int amount = reward.getCount();
                user.addItem(reward);
                CobblemonCasinoLogger.toPlayerTranslated(player, "message.cobblemoncasino.item_gachapon_received", true, amount, itemName);
                world.playSound(null, user.blockPosition(), ModSounds.OPEN_PRIZE, SoundSource.BLOCKS, 1.0F, 1.0F);
                CobblemonCasino.LOGGER.info("[Gachapon] Player {} opened a {} and got {}",
                        player.getName().getString(), stack.getHoverName().getString(), itemName);
                stack.shrink(1);
            } else {
                CobblemonCasinoLogger.toPlayerTranslated(player, "message.cobblemoncasino.item_gachapon_empty", true);
                CobblemonCasino.LOGGER.warn("[Gachapon] Player {} tried to open a {} but all items in pool {} are invalid!",
                        player.getName().getString(), stack.getHoverName().getString(), poolKey);
            }

            for (Item item : ModItems.ALL_GACHAPON_ITEMS) {
                player.getCooldowns().addCooldown(item, 15);
            }

        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag type) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(this);
        tooltip.add(Component.translatable("tooltip.cobblemoncasino." + id.getPath()));
        super.appendHoverText(stack, context, tooltip, type);
    }

}

