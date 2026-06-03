package net.andrespr.casinorocket.item.custom;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.CobblemonUtils;
import net.andrespr.casinorocket.games.gachapon.PokemonGachaponUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PokemonGachaponItem extends Item {

    private final String poolKey;

    public PokemonGachaponItem(Properties settings, String poolKey) {
        super(settings.stacksTo(1));
        this.poolKey = poolKey;
        ModItems.ALL_GACHAPON_ITEMS.add(this);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);

        if (!world.isClientSide && user instanceof ServerPlayer player) {
            MinecraftServer server = player.getServer();
            PokemonGachaponUtils.CachedEntry reward = PokemonGachaponUtils.pickPokemonReward(world.random, poolKey);

            if (reward != null) {
                PokemonProperties properties = CobblemonUtils.safeParse(reward.pokemonId(), player, server);
                Objects.requireNonNull(properties).setLevel(reward.level());
                properties.setIvs(CobblemonUtils.createFixedIVs(reward.ivs()));
                properties.setShiny(CobblemonUtils.itWillBeShiny(world.random, reward.shiny()));
                properties.setPokeball(CobblemonUtils.getCherishBallIfLegendary(reward.pokemonId()));

                CobblemonUtils.addPokemon(properties, player);
                world.playSound(null, user.blockPosition(), ModSounds.OPEN_PRIZE, SoundSource.BLOCKS, 1.0F, 1.0F);
                CasinoRocket.LOGGER.info("[Pokémon Gachapon] Player {} opened a {} and got {}",
                        player.getName().getString(), stack.getHoverName().getString(), CobblemonUtils.getPokemonName(properties));

                stack.shrink(1);
            } else {
                CasinoRocketLogger.toPlayerTranslated(player, "message.casinorocket.item_gachapon_empty", true, poolKey);
                CasinoRocket.LOGGER.warn("[Pokémon Gachapon] All Pokémon in {} are invalid or have 0 weight!", poolKey);
            }

            for (Item item : ModItems.ALL_GACHAPON_ITEMS) {
                player.getCooldowns().addCooldown(item, 15);
            }

        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, @NotNull List<Component> tooltip, TooltipFlag type) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(this);
        tooltip.add(Component.translatable("tooltip.casinorocket." + id.getPath()));
        super.appendHoverText(stack, context, tooltip, type);
    }

}

