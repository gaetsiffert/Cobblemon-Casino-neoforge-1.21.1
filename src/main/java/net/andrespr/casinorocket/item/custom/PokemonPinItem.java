package net.andrespr.casinorocket.item.custom;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import net.andrespr.casinorocket.util.CobblemonUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import java.util.List;

public class PokemonPinItem extends Item {

    private final int level;
    private final int ivs;
    private final boolean shiny;

    public PokemonPinItem(Properties settings, int level, int ivs, boolean shiny) {
        super(settings.stacksTo(1));
        this.level = level;
        this.ivs = ivs;
        this.shiny = shiny;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if (!world.isClientSide && entity instanceof ServerPlayer player) {

            CustomData nbtComponent = stack.get(DataComponents.CUSTOM_DATA);
            CompoundTag tag = nbtComponent != null ? nbtComponent.copyTag() : new CompoundTag();

            if (!tag.getBoolean("Used")) {
                MinecraftServer server = player.getServer();
                ResourceLocation id = BuiltInRegistries.ITEM.getKey(this);
                String speciesName = id.getPath().replace("_pin", "");

                PokemonProperties properties = CobblemonUtils.safeParse(speciesName, player, server);
                if (properties == null) { return; }

                properties.setLevel(level);
                properties.setIvs(CobblemonUtils.createFixedIVs(ivs));
                properties.setShiny(shiny);
                properties.setForm(null);

                CobblemonUtils.addPokemon(properties, player);

                tag.putBoolean("Used", true);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                player.getInventory().removeItem(stack);
            }
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.casinorocket.pokemon_pin"));
        super.appendHoverText(stack, context, tooltip, type);
    }

}

