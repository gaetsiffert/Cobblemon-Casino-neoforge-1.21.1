package net.narrnouille.cobblemoncasino.item.custom;

import net.narrnouille.cobblemoncasino.item.ModItems;
import net.narrnouille.cobblemoncasino.util.SyncedMoneyValues;
import net.narrnouille.cobblemoncasino.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChipItem extends Item {

    private final long value;

    public ChipItem(Properties settings, long value) {
        super(settings);
        this.value = value;
        ModItems.ALL_CHIP_ITEMS.add(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag type) {
        long displayValue = SyncedMoneyValues.getChipValue(this, value);
        Component valueText = Component.literal(TextUtils.formatWithCommas(displayValue));
        tooltip.add(Component.translatable("tooltip.cobblemoncasino.chip_value", valueText.copy().withStyle(ChatFormatting.GOLD)));
        super.appendHoverText(stack, context, tooltip, type);
    }

    // === GETTERS ===

    public long getValue() {
        return value;
    }

}

