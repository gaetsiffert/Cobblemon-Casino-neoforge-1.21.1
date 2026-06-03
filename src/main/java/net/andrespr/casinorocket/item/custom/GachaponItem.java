package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
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
import net.andrespr.casinorocket.games.gachapon.GachaponUtils;
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
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.item_gachapon_received", true, amount, itemName);
                world.playSound(null, user.blockPosition(), ModSounds.OPEN_PRIZE, SoundSource.BLOCKS, 1.0F, 1.0F);
                CasinoRocket.LOGGER.info("[Gachapon] Player {} opened a {} and got {}",
                        player.getName().getString(), stack.getHoverName().getString(), itemName);
                stack.shrink(1);
            } else {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.item_gachapon_empty", true);
                CasinoRocket.LOGGER.warn("[Gachapon] Player {} tried to open a {} but all items in pool {} are invalid!",
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
        tooltip.add(Component.translatable("tooltip.casinorocket." + id.getPath()));
        super.appendHoverText(stack, context, tooltip, type);
    }

}

