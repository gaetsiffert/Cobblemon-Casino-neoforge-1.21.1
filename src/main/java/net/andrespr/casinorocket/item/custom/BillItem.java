package net.andrespr.casinorocket.item.custom;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;
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

                if (!CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.bill_cobbledollars_disabled", true);
                    return InteractionResultHolder.fail(stack);
                }

                if (!ModList.get().isLoaded("cobbledollars")) {
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.bill_cobbledollars_missing", true);
                    return InteractionResultHolder.fail(stack);
                }

                String command = "cobbledollars give " + player.getName().getString() + " " + totalValue;
                int result;
                try {
                    result = server.getCommands().getDispatcher().execute(
                            command,
                            player.createCommandSourceStack().withPermission(4).withSuppressedOutput()
                    );
                } catch (CommandSyntaxException | RuntimeException e) {
                    CasinoRocket.LOGGER.warn("[Bill] Cobbledollars deposit command failed for {}: {}",
                            player.getName().getString(), e.getMessage());
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.bill_deposit_failed", true);
                    return InteractionResultHolder.fail(stack);
                }

                if (result <= 0) {
                    CasinoRocket.LOGGER.warn("[Bill] Cobbledollars deposit command returned {} for {}",
                            result, player.getName().getString());
                    CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.bill_deposit_failed", true);
                    return InteractionResultHolder.fail(stack);
                }

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

