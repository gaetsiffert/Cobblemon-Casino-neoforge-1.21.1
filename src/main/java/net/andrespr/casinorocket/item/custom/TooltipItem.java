package net.andrespr.casinorocket.item.custom;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class TooltipItem extends Item {

    public TooltipItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        tooltip.add(Component.translatable("tooltip." + id.getNamespace() + "." + id.getPath()));
    }

}

