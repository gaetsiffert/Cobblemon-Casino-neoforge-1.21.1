package net.narrnouille.cobblemoncasino.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.screen.custom.chip_table.ChipTableScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@JeiPlugin
public final class CobblemonCasinoJeiPlugin implements IModPlugin {

    private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(ChipTableScreen.class, new IGuiContainerHandler<>() {
            @Override
            public List<Rect2i> getGuiExtraAreas(ChipTableScreen screen) {
                return screen.getCurrencyValuePanelArea();
            }
        });
    }
}
