package net.narrnouille.cobblemoncasino.screen.custom;

import net.narrnouille.cobblemoncasino.network.c2s.common.OpenBetScreenC2SPayload;
import net.narrnouille.cobblemoncasino.network.c2s.common.OpenMenuScreenC2SPayload;
import net.narrnouille.cobblemoncasino.network.c2s.common.OpenWithdrawScreenC2SPayload;
import net.narrnouille.cobblemoncasino.network.c2s.common.ReturnToMachineScreenC2SPayload;
import net.narrnouille.cobblemoncasino.screen.opening.MouseRestore;
import net.narrnouille.cobblemoncasino.util.IMachineBoundHandler;
import net.narrnouille.cobblemoncasino.network.CobblemonCasinoPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class CasinoMachineScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    public CasinoMachineScreen(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        MouseRestore.applyIfPending(minecraft);
    }

    protected void onBetPressed() {
        if (minecraft != null && minecraft.player != null) {
            MouseRestore.capture();
            CobblemonCasinoPackets.sendToServer(new OpenBetScreenC2SPayload());
        }
    }

    protected void onWithdrawPressed() {
        if (minecraft != null && minecraft.player != null) {
            MouseRestore.capture();
            CobblemonCasinoPackets.sendToServer(new OpenWithdrawScreenC2SPayload());
        }
    }

    protected void onMenuPressed() {
        if (minecraft != null && minecraft.player != null) {
            MouseRestore.capture();
            CobblemonCasinoPackets.sendToServer(new OpenMenuScreenC2SPayload());
        }
    }

    protected boolean returnToMachineOnEsc() {
        return true;
    }

    protected void onBackPressed() {
        this.onClose();
    }

    @Override
    public void onClose() {
        if (returnToMachineOnEsc() && tryReturnToMachine()) return;
        super.onClose();
    }

    protected boolean tryReturnToMachine() {
        if (minecraft == null || minecraft.player == null) return false;

        if (this.menu instanceof IMachineBoundHandler machine) {
            MouseRestore.capture();
            CobblemonCasinoPackets.sendToServer(new ReturnToMachineScreenC2SPayload(machine.getMachinePos(), machine.getMachineKey()));
            return true;
        }
        return false;
    }

}

