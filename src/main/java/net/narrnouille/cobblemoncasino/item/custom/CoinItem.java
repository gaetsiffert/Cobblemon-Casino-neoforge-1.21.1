package net.narrnouille.cobblemoncasino.item.custom;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class CoinItem extends Item {

    public CoinItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.cobblemoncasino." + BuiltInRegistries.ITEM.getKey(this).getPath()));
        super.appendHoverText(stack, context, tooltip, type);
    }

}

