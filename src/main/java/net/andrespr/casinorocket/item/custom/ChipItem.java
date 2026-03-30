package net.andrespr.casinorocket.item.custom;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChipItem extends Item {

    private final long value;
    private final String economyType;

    public ChipItem(Settings settings, long value, String economyType) {
        super(settings);
        this.value = value;
        this.economyType = economyType;
        ModItems.ALL_CHIP_ITEMS.add(this);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, @NotNull List<Text> tooltip, TooltipType type) {
        Text valueText;
        if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {
            valueText = Text.literal(TextUtils.formatCompact(value));
            tooltip.add(Text.translatable("tooltip.casinorocket.chip_money", valueText.copy().formatted(Formatting.GOLD)));
        } else {
            valueText = Text.literal(TextUtils.formatWithCommas(value));
            tooltip.add(Text.translatable("tooltip.casinorocket.chip_item", valueText.copy().formatted(Formatting.GOLD), getItemName().formatted(Formatting.GOLD)));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    // === GETTERS ===

    public String getItem() {
        return switch (economyType) {
            case "diamonds" -> "minecraft:diamond";
            default -> "cobblemon:relic_coin";
        };
    }

    public MutableText getItemName() {
        String id = getItem();
        Item item = Registries.ITEM.get(Identifier.tryParse(id));
        MutableText itemName = item.getName().copy();
        if (value > 1) itemName = itemName.append("s");
        return itemName;
    }

    public long getValue() {
        return value;
    }

}