package net.andrespr.casinorocket.screen.custom;

import net.andrespr.casinorocket.network.c2s.common.OpenBetScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.OpenMenuScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.OpenWithdrawScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.ReturnToMachineScreenC2SPayload;
import net.andrespr.casinorocket.screen.opening.MouseRestore;
import net.andrespr.casinorocket.util.IMachineBoundHandler;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
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

    protected void onBetPressed() {
        if (minecraft != null && minecraft.player != null) {
            CasinoRocketPackets.sendToServer(new OpenBetScreenC2SPayload());
        }
    }

    protected void onWithdrawPressed() {
        if (minecraft != null && minecraft.player != null) {
            CasinoRocketPackets.sendToServer(new OpenWithdrawScreenC2SPayload());
        }
    }

    protected void onMenuPressed() {
        if (minecraft != null && minecraft.player != null) {
            MouseRestore.capture();
            CasinoRocketPackets.sendToServer(new OpenMenuScreenC2SPayload());
        }
    }

    protected boolean returnToMachineOnEsc() {
        return true;
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
            CasinoRocketPackets.sendToServer(new ReturnToMachineScreenC2SPayload(machine.getMachinePos(), machine.getMachineKey()));
            return true;
        }
        return false;
    }

}

