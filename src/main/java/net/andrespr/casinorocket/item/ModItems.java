package net.andrespr.casinorocket.item;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.block.ModBlocks;
import net.andrespr.casinorocket.item.custom.*;
import net.andrespr.casinorocket.sound.ModSounds;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CasinoRocket.MOD_ID);

    public static final List<Item> ALL_CHIP_ITEMS = new ArrayList<>();
    public static ChipItem RED_CHIP;
    public static ChipItem BLUE_CHIP;
    public static ChipItem YELLOW_CHIP;
    public static ChipItem PURPLE_CHIP;
    public static ChipItem COPPER_CHIP;
    public static ChipItem IRON_CHIP;
    public static ChipItem EMERALD_CHIP;
    public static ChipItem GOLD_CHIP;
    public static ChipItem DIAMOND_CHIP;
    public static ChipItem NETHERITE_CHIP;
    public static ChipItem BLACK_CHIP;
    public static ChipItem WHITE_CHIP;
    public static ChipItem RAINBOW_CHIP;

    public static CoinItem COPPER_COIN;
    public static CoinItem IRON_COIN;
    public static CoinItem GOLD_COIN;
    public static CoinItem DIAMOND_COIN;
    public static CoinItem EVENT_COIN;
    public static CoinItem PRIMOGEM;

    public static final List<Item> ALL_GACHAPON_ITEMS = new ArrayList<>();
    public static GachaponItem POKE_GACHAPON;
    public static GachaponItem GREAT_GACHAPON;
    public static GachaponItem ULTRA_GACHAPON;
    public static GachaponItem MASTER_GACHAPON;
    public static GachaponItem CHERISH_GACHAPON;
    public static GachaponItem PREMIER_GACHAPON;
    public static GachaponItem EVENT_GACHAPON;
    public static PokemonGachaponItem POKEMON_POKE_GACHAPON;
    public static PokemonGachaponItem POKEMON_GREAT_GACHAPON;
    public static PokemonGachaponItem POKEMON_ULTRA_GACHAPON;
    public static PokemonGachaponItem POKEMON_MASTER_GACHAPON;
    public static PokemonGachaponItem POKEMON_CHERISH_GACHAPON;
    public static PokemonGachaponItem POKEMON_PREMIER_GACHAPON;
    public static PokemonGachaponItem POKEMON_EVENT_GACHAPON;

    public static PokemonPinItem LITWICK_PIN;
    public static PokemonPinItem STARYU_PIN;
    public static PokemonPinItem BELLSPROUT_PIN;
    public static PokemonPinItem TYROGUE_PIN;
    public static PokemonPinItem SCYTHER_PIN;
    public static PokemonPinItem EEVEE_PIN;
    public static PokemonPinItem DRATINI_PIN;
    public static PokemonPinItem ROTOM_PIN;
    public static PokemonPinItem DITTO_PIN;
    public static PokemonPinItem PORYGON_PIN;

    public static final List<Item> ALL_BILL_ITEMS = new ArrayList<>();
    public static BillItem BILL_1;
    public static BillItem BILL_5;
    public static BillItem BILL_10;
    public static BillItem BILL_50;
    public static BillItem BILL_100;
    public static BillItem BILL_500;
    public static BillItem BILL_1K;
    public static BillItem BILL_5K;
    public static BillItem BILL_10K;
    public static BillItem BILL_50K;
    public static BillItem BILL_100K;
    public static BillItem BILL_500K;
    public static BillItem BILL_1M;

    public static WalletItem WALLET;

    public static Item DIAMOND_NUGGET;
    public static TooltipItem CHARGED_DIAMOND;
    public static TooltipItem HYPERCHARGED_DIAMOND;
    public static final Map<Item, Long> DIAMOND_VALUES = new LinkedHashMap<>();

    public static TooltipItem HANDFUL_OF_RELIC_COINS;
    public static TooltipItem STACK_OF_RELIC_COINS;

    public static Item FIRERED_GC_MUSIC_DISC;
    public static Item HEARTGOLD_GC_MUSIC_DISC;
    public static Item EMERALD_GC_MUSIC_DISC;
    public static Item PLATINUM_GC_MUSIC_DISC;

    static {
        registerChipItem("red_chip", item -> RED_CHIP = item);
        registerChipItem("blue_chip", item -> BLUE_CHIP = item);
        registerChipItem("yellow_chip", item -> YELLOW_CHIP = item);
        registerChipItem("purple_chip", item -> PURPLE_CHIP = item);
        registerChipItem("copper_chip", item -> COPPER_CHIP = item);
        registerChipItem("iron_chip", item -> IRON_CHIP = item);
        registerChipItem("emerald_chip", item -> EMERALD_CHIP = item);
        registerChipItem("gold_chip", item -> GOLD_CHIP = item);
        registerChipItem("diamond_chip", item -> DIAMOND_CHIP = item);
        registerChipItem("netherite_chip", item -> NETHERITE_CHIP = item);
        registerChipItem("black_chip", item -> BLACK_CHIP = item);
        registerChipItem("white_chip", item -> WHITE_CHIP = item);
        registerChipItem("rainbow_chip", item -> RAINBOW_CHIP = item);

        registerCustomItem("copper_coin", () -> COPPER_COIN = new CoinItem(new Item.Properties()));
        registerCustomItem("iron_coin", () -> IRON_COIN = new CoinItem(new Item.Properties()));
        registerCustomItem("gold_coin", () -> GOLD_COIN = new CoinItem(new Item.Properties()));
        registerCustomItem("diamond_coin", () -> DIAMOND_COIN = new CoinItem(new Item.Properties()));
        registerCustomItem("event_coin", () -> EVENT_COIN = new CoinItem(new Item.Properties()));
        registerCustomItem("primogem", () -> PRIMOGEM = new CoinItem(new Item.Properties()));

        registerCustomItem("poke_gachapon", () -> POKE_GACHAPON = new GachaponItem(new Item.Properties(), "common"));
        registerCustomItem("great_gachapon", () -> GREAT_GACHAPON = new GachaponItem(new Item.Properties(), "uncommon"));
        registerCustomItem("ultra_gachapon", () -> ULTRA_GACHAPON = new GachaponItem(new Item.Properties(), "rare"));
        registerCustomItem("master_gachapon", () -> MASTER_GACHAPON = new GachaponItem(new Item.Properties(), "ultrarare"));
        registerCustomItem("cherish_gachapon", () -> CHERISH_GACHAPON = new GachaponItem(new Item.Properties(), "legendary"));
        registerCustomItem("premier_gachapon", () -> PREMIER_GACHAPON = new GachaponItem(new Item.Properties(), "bonus"));
        registerCustomItem("event_gachapon", () -> EVENT_GACHAPON = new GachaponItem(new Item.Properties(), "event"));
        registerCustomItem("pokemon_poke_gachapon", () -> POKEMON_POKE_GACHAPON = new PokemonGachaponItem(new Item.Properties(), "common"));
        registerCustomItem("pokemon_great_gachapon", () -> POKEMON_GREAT_GACHAPON = new PokemonGachaponItem(new Item.Properties(), "uncommon"));
        registerCustomItem("pokemon_ultra_gachapon", () -> POKEMON_ULTRA_GACHAPON = new PokemonGachaponItem(new Item.Properties(), "rare"));
        registerCustomItem("pokemon_master_gachapon", () -> POKEMON_MASTER_GACHAPON = new PokemonGachaponItem(new Item.Properties(), "ultrarare"));
        registerCustomItem("pokemon_cherish_gachapon", () -> POKEMON_CHERISH_GACHAPON = new PokemonGachaponItem(new Item.Properties(), "legendary"));
        registerCustomItem("pokemon_premier_gachapon", () -> POKEMON_PREMIER_GACHAPON = new PokemonGachaponItem(new Item.Properties(), "bonus"));
        registerCustomItem("pokemon_event_gachapon", () -> POKEMON_EVENT_GACHAPON = new PokemonGachaponItem(new Item.Properties(), "event"));

        registerCustomItem("litwick_pin", () -> LITWICK_PIN = new PokemonPinItem(new Item.Properties(), 5, 15, false));
        registerCustomItem("staryu_pin", () -> STARYU_PIN = new PokemonPinItem(new Item.Properties(), 5, 15, false));
        registerCustomItem("bellsprout_pin", () -> BELLSPROUT_PIN = new PokemonPinItem(new Item.Properties(), 5, 15, false));
        registerCustomItem("tyrogue_pin", () -> TYROGUE_PIN = new PokemonPinItem(new Item.Properties(), 10, 15, false));
        registerCustomItem("scyther_pin", () -> SCYTHER_PIN = new PokemonPinItem(new Item.Properties(), 10, 15, false));
        registerCustomItem("eevee_pin", () -> EEVEE_PIN = new PokemonPinItem(new Item.Properties(), 10, 21, false));
        registerCustomItem("dratini_pin", () -> DRATINI_PIN = new PokemonPinItem(new Item.Properties(), 10, 15, false));
        registerCustomItem("rotom_pin", () -> ROTOM_PIN = new PokemonPinItem(new Item.Properties(), 10, 21, false));
        registerCustomItem("ditto_pin", () -> DITTO_PIN = new PokemonPinItem(new Item.Properties(), 5, 31, false));
        registerCustomItem("porygon_pin", () -> PORYGON_PIN = new PokemonPinItem(new Item.Properties(), 10, 15, false));

        registerCustomItem("bill_1", () -> BILL_1 = new BillItem(new Item.Properties(), 1));
        registerCustomItem("bill_5", () -> BILL_5 = new BillItem(new Item.Properties(), 5));
        registerCustomItem("bill_10", () -> BILL_10 = new BillItem(new Item.Properties(), 10));
        registerCustomItem("bill_50", () -> BILL_50 = new BillItem(new Item.Properties(), 50));
        registerCustomItem("bill_100", () -> BILL_100 = new BillItem(new Item.Properties(), 100));
        registerCustomItem("bill_500", () -> BILL_500 = new BillItem(new Item.Properties(), 500));
        registerCustomItem("bill_1k", () -> BILL_1K = new BillItem(new Item.Properties(), 1_000));
        registerCustomItem("bill_5k", () -> BILL_5K = new BillItem(new Item.Properties(), 5_000));
        registerCustomItem("bill_10k", () -> BILL_10K = new BillItem(new Item.Properties(), 10_000));
        registerCustomItem("bill_50k", () -> BILL_50K = new BillItem(new Item.Properties(), 50_000));
        registerCustomItem("bill_100k", () -> BILL_100K = new BillItem(new Item.Properties(), 100_000));
        registerCustomItem("bill_500k", () -> BILL_500K = new BillItem(new Item.Properties(), 500_000));
        registerCustomItem("bill_1m", () -> BILL_1M = new BillItem(new Item.Properties(), 1_000_000));

        registerCustomItem("wallet", () -> WALLET = new WalletItem(new Item.Properties()));

        registerItem("diamond_nugget", () -> DIAMOND_NUGGET = new Item(new Item.Properties()));
        registerItemWithTooltip("charged_diamond", () -> CHARGED_DIAMOND = new TooltipItem(new Item.Properties()));
        registerItemWithTooltip("hypercharged_diamond", () -> HYPERCHARGED_DIAMOND = new TooltipItem(new Item.Properties()));
        registerItemWithTooltip("handful_of_relic_coins", () -> HANDFUL_OF_RELIC_COINS = new TooltipItem(new Item.Properties()));
        registerItemWithTooltip("stack_of_relic_coins", () -> STACK_OF_RELIC_COINS = new TooltipItem(new Item.Properties()));

        registerItem("firered_gc_music_disc", () -> FIRERED_GC_MUSIC_DISC = new Item(new Item.Properties().jukeboxPlayable(ModSounds.FIRERED_GC_KEY).stacksTo(1)));
        registerItem("heartgold_gc_music_disc", () -> HEARTGOLD_GC_MUSIC_DISC = new Item(new Item.Properties().jukeboxPlayable(ModSounds.HEARTGOLD_GC_KEY).stacksTo(1)));
        registerItem("emerald_gc_music_disc", () -> EMERALD_GC_MUSIC_DISC = new Item(new Item.Properties().jukeboxPlayable(ModSounds.EMERALD_GC_KEY).stacksTo(1)));
        registerItem("platinum_gc_music_disc", () -> PLATINUM_GC_MUSIC_DISC = new Item(new Item.Properties().jukeboxPlayable(ModSounds.PLATINUM_GC_KEY).stacksTo(1)));
    }

    private static <T extends Item> void registerItem(String name, Supplier<T> supplier) {
        ITEMS.register(name, () -> {
            T item = supplier.get();
            updateDiamondValuesIfReady();
            return item;
        });
    }

    private static void registerItemWithTooltip(String name, Supplier<TooltipItem> supplier) {
        registerItem(name, supplier);
    }

    private static <T extends Item> void registerCustomItem(String name, Supplier<T> supplier) {
        registerItem(name, supplier);
    }

    private static void registerChipItem(String name, Consumer<ChipItem> assignment) {
        registerCustomItem(name, () -> {
            ChipItem item = createChipItem(name);
            assignment.accept(item);
            return item;
        });
    }

    private static ChipItem createChipItem(String name) {
        String typeRaw = CasinoRocket.CONFIG.generalConfig.economy_type;
        String type = (typeRaw == null ? "" : typeRaw.trim().toLowerCase());
        long value;
        switch (type) {
            case "cobbledollars", "cobbledollar"
                    -> value = CasinoRocket.CONFIG.generalConfig.getChipPriceInMoney(name);
            case "diamonds", "diamond", "relic_coins", "relic_coin"
                    -> value = CasinoRocket.CONFIG.generalConfig.getChipPriceInItems(name);
            default -> {
                CasinoRocket.LOGGER.warn("Unknown economy_type='{}'. Only admits 'relic_coins', 'diamonds' or 'cobbledollars'. Falling back to 'relic_coins'.", typeRaw);
                value = CasinoRocket.CONFIG.generalConfig.getChipPriceInItems(name);
                type = "relic_coins";
            }
        }
        return new ChipItem(new Item.Properties(), value, type);
    }

    public static void registerModItems(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CasinoRocket.LOGGER.info("Registering Mod Items for " + CasinoRocket.MOD_ID);
    }

    private static void updateDiamondValuesIfReady() {
        if (CHARGED_DIAMOND == null || HYPERCHARGED_DIAMOND == null || ModBlocks.CONDENSED_DIAMOND_BLOCK == null) {
            return;
        }

        DIAMOND_VALUES.clear();
        DIAMOND_VALUES.put(Items.DIAMOND, 1L);
        DIAMOND_VALUES.put(CHARGED_DIAMOND, 4L);
        DIAMOND_VALUES.put(Items.DIAMOND_BLOCK, 9L);
        DIAMOND_VALUES.put(HYPERCHARGED_DIAMOND, 16L);
        DIAMOND_VALUES.put(ModBlocks.CONDENSED_DIAMOND_BLOCK.asItem(), 81L);
    }

    public static long getDiamondValue(ItemStack stack) {
        updateDiamondValuesIfReady();
        Long value = DIAMOND_VALUES.get(stack.getItem());
        if (value == null) return 0L;
        return value * stack.getCount();
    }
}
