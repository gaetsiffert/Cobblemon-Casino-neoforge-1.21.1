package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
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

public class BillItem extends Item {

    private final long value;

    public BillItem(Properties settings, long value) {
        super(settings);
        this.value = value;
        ModItems.ALL_BILL_ITEMS.add(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (!world.isClientSide && user instanceof ServerPlayer player && player.isShiftKeyDown()) {
            MinecraftServer server = player.getServer();
            if (server != null) {
                ItemStack stack = player.getItemInHand(hand);
                int count = stack.getCount();
                long totalValue = value * count;

                String command = "cobbledollars give " + player.getName().getString() + " " + totalValue;
                server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withPermission(4).withSuppressedOutput(), command);

                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.money_deposited", true, TextUtils.formatCompact(totalValue));
                CasinoRocket.LOGGER.info("[Bill] Player {} deposited ${} into his wallet.", player.getName().getString(), TextUtils.formatCompact(totalValue));

                stack.shrink(count);
            }
        }
        return InteractionResultHolder.success(user.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.casinorocket.bill"));
        super.appendHoverText(stack, context, tooltip, type);
    }

    // === GETTERS ===

    public long getValue() {
        return value;
    }

}

