package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.TextUtils;
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
        Component valueText = Component.literal(TextUtils.formatWithCommas(value));
        tooltip.add(Component.translatable("tooltip.casinorocket.chip_value", valueText.copy().withStyle(ChatFormatting.GOLD)));
        super.appendHoverText(stack, context, tooltip, type);
    }

    // === GETTERS ===

    public long getValue() {
        return value;
    }

}

