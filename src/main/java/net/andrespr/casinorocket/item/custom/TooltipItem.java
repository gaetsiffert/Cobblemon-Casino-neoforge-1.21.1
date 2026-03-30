package net.andrespr.casinorocket.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class TooltipItem extends Item {

    public TooltipItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        Identifier id = Registries.ITEM.getId(stack.getItem());
        tooltip.add(Text.translatable("tooltip." + id.getNamespace() + "." + id.getPath()));
    }

}