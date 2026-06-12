package net.narrnouille.cobblemoncasino.screen;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.games.slot.SlotSymbol;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public final class ModGuiTextures {

    private ModGuiTextures() {}

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, path);
    }

    // === BACKGROUNDS ===
    public static final ResourceLocation SLOT_MACHINE_GUI =
            id("textures/gui/slot_machine/slot_machine_gui.png");       // 230x181
    public static final ResourceLocation SLOT_MACHINE_MENU_GUI =
            id("textures/gui/slot_machine/slot_machine_menu_gui.png");     // 200x236
    public static final ResourceLocation BET_GUI =
            id("textures/gui/common/gui/bet_gui.png");      // 174x166
    public static final ResourceLocation WITHDRAW_GUI =
            id("textures/gui/common/gui/withdraw_gui.png");     // 174x166
    public static final ResourceLocation BLACKJACK_TABLE_GUI =
            id("textures/gui/blackjack_table/blackjack_table_gui.png");     // 242x222
    public static final ResourceLocation CHIP_TABLE_GUI =
            id("textures/gui/chip_table/chip_table_gui.png");     // 174x171
    public static final ResourceLocation CHIP_TABLE_EXCHANGE_GUI =
            id("textures/gui/chip_table/chip_table_exchange_gui.png");     // 174x199

    // === BUTTONS ===
    // COMMON
    public static final ResourceLocation BTN_SMALL =
            id("textures/gui/common/buttons/cobblemon_small_button.png");       // 29x36 -> 29x12 (x3)
    public static final ResourceLocation BTN_MEDIUM =
            id("textures/gui/common/buttons/cobblemon_medium_button.png");      // 59x36 -> 59x12 (x3)
    public static final ResourceLocation BTN_LARGE =
            id("textures/gui/common/buttons/cobblemon_large_button.png");       // 82x36 -> 82x12 (x3)
    // SPECIAL
    public static final ResourceLocation BTN_BET =
            id("textures/gui/common/buttons/bet_button.png");       // 72x72 -> 72x72 (x3)
    public static final ResourceLocation BTN_MENU =
            id("textures/gui/common/buttons/menu_button.png");      // 54x72 -> 54x72 (x3)
    public static final ResourceLocation BTN_WITHDRAW =
            id("textures/gui/common/buttons/withdraw_button.png");      // 72x72 -> 72x72 (x3)
    public static final ResourceLocation BTN_SPIN =
            id("textures/gui/common/buttons/spin_button.png");      // 40x120 -> 40x40 (x3)
    // SLOT MACHINE MENU
    public static final ResourceLocation BTN_ADD =
            id("textures/gui/common/buttons/add_button.png");       // 12x36 -> 12x12 (x3)
    public static final ResourceLocation BTN_SUBTRACT =
            id("textures/gui/common/buttons/subtract_button.png");      // 12x36 -> 12x12 (x3)
    public static final ResourceLocation BTN_MODE1 =
            id("textures/gui/common/buttons/mode1_button.png");     // 14x42 -> 14x14 (x3)
    public static final ResourceLocation BTN_MODE2 =
            id("textures/gui/common/buttons/mode2_button.png");     // 14x42 -> 14x14 (x3)
    public static final ResourceLocation BTN_MODE3 =
            id("textures/gui/common/buttons/mode3_button.png");     // 14x42 -> 14x14 (x3)
    public static final ResourceLocation BTN_BACK =
            id("textures/gui/common/buttons/back_button.png");      // 25x24 -> 25x12 (x2)

    // === SLOT MACHINE SYMBOLS ===
    public static final ResourceLocation HAUNTER =
            id("textures/gui/slot_machine/symbols/haunter.png");
    public static final ResourceLocation CHERRY =
            id("textures/gui/slot_machine/symbols/cherry.png");
    public static final ResourceLocation BULBASAUR =
            id("textures/gui/slot_machine/symbols/bulbasaur.png");
    public static final ResourceLocation SQUIRTLE =
            id("textures/gui/slot_machine/symbols/squirtle.png");
    public static final ResourceLocation CHARMANDER =
            id("textures/gui/slot_machine/symbols/charmander.png");
    public static final ResourceLocation PIKACHU =
            id("textures/gui/slot_machine/symbols/pikachu.png");
    public static final ResourceLocation MEW =
            id("textures/gui/slot_machine/symbols/mew.png");
    public static final ResourceLocation ROCKET =
            id("textures/gui/slot_machine/symbols/rocket.png");
    public static final ResourceLocation SEVEN =
            id("textures/gui/slot_machine/symbols/seven.png");

    public static class SlotTextures {
        public static final Map<SlotSymbol, ResourceLocation> SYMBOL_TEXTURES = Map.of(
                SlotSymbol.HAUNTER,    HAUNTER,
                SlotSymbol.CHERRY,     CHERRY,
                SlotSymbol.BULBASAUR,  BULBASAUR,
                SlotSymbol.SQUIRTLE,   SQUIRTLE,
                SlotSymbol.CHARMANDER, CHARMANDER,
                SlotSymbol.PIKACHU,    PIKACHU,
                SlotSymbol.MEW,        MEW,
                SlotSymbol.ROCKET,     ROCKET,
                SlotSymbol.SEVEN,      SEVEN
        );
    }

    // === LINES (SLOT MACHINE) ===
    public static final ResourceLocation SLOT_LINE_ONE =
            id("textures/gui/slot_machine/lines/line_one.png");
    public static final ResourceLocation SLOT_LINE_TWO =
            id("textures/gui/slot_machine/lines/line_two.png");
    public static final ResourceLocation SLOT_LINE_THREE_TOP =
            id("textures/gui/slot_machine/lines/line_three_top.png");
    public static final ResourceLocation SLOT_LINE_THREE_BOTTOM =
            id("textures/gui/slot_machine/lines/line_three_bottom.png");

    // === CARDS (BLACKJACK) ===
    public static final ResourceLocation CARDS_SPRITESHEET =
            id("textures/gui/common/cards/cards_spritesheet.png"); // 24x32 each
    public static final ResourceLocation CARD_BOTTOM =
            id("textures/gui/common/cards/card_bottom.png"); // 24x32

    // === ECONOMY ===
    public static final ResourceLocation CHIP =
            id("textures/gui/common/economy/chip.png");

    // === MISC ===
    public static final ResourceLocation LINES_LAYOUT =
            ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "textures/gui/common/misc/lines_layout.png");
    public static final ResourceLocation DANCING_CLEFAIRY =
            id("textures/gui/common/misc/dancing_clefairy.png");
    public static final ResourceLocation REELS =
            ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "textures/gui/common/misc/reels.png");

}

