package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChipItem extends Item {

    private final long value;
    private final String economyType;

    public ChipItem(Properties settings, long value, String economyType) {
        super(settings);
        this.value = value;
        this.economyType = economyType;
        ModItems.ALL_CHIP_ITEMS.add(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag type) {
        Component valueText;
        if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {
            valueText = Component.literal(TextUtils.formatCompact(value));
            tooltip.add(Component.translatable("tooltip.casinorocket.chip_money", valueText.copy().withStyle(ChatFormatting.GOLD)));
        } else {
            valueText = Component.literal(TextUtils.formatWithCommas(value));
            tooltip.add(Component.translatable("tooltip.casinorocket.chip_item", valueText.copy().withStyle(ChatFormatting.GOLD), getItemName().withStyle(ChatFormatting.GOLD)));
        }
        super.appendHoverText(stack, context, tooltip, type);
    }

    // === GETTERS ===

    public String getItem() {
        return switch (economyType) {
            case "diamonds" -> "minecraft:diamond";
            default -> "cobblemon:relic_coin";
        };
    }

    public MutableComponent getItemName() {
        String id = getItem();
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(id));
        MutableComponent itemName = item.getDescription().copy();
        if (value > 1) itemName = itemName.append("s");
        return itemName;
    }

    public long getValue() {
        return value;
    }

}

