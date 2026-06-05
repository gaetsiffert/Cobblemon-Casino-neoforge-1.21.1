package net.andrespr.casinorocket.screen.custom.ledger;

import net.andrespr.casinorocket.screen.ModMenuTypes;
import net.andrespr.casinorocket.screen.opening.CasinoLedgerOpenData;
import net.andrespr.casinorocket.util.MenuValidation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class CasinoLedgerScreenHandler extends AbstractContainerMenu {

    private final CasinoLedgerOpenData data;

    public CasinoLedgerScreenHandler(int syncId, Inventory inventory, CasinoLedgerOpenData data) {
        super(ModMenuTypes.CASINO_LEDGER_MENU_TYPE, syncId);
        this.data = data;
    }

    public CasinoLedgerOpenData getData() {
        return this.data;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return MenuValidation.isValidScoreboard(player, this.data.pos());
    }
}
