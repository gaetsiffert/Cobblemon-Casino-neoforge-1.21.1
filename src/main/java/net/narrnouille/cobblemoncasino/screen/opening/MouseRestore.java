package net.narrnouille.cobblemoncasino.screen.opening;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public final class MouseRestore {

    private static boolean pending = false;
    private static double x = 0;
    private static double y = 0;

    private static int applyAttemptsRemaining = 0;

    private MouseRestore() {}

    public static void capture(Minecraft client) {
        if (client == null) return;
        x = client.mouseHandler.xpos();
        y = client.mouseHandler.ypos();
        pending = true;

        applyAttemptsRemaining = 3;
    }

    public static void capture() {
        capture(Minecraft.getInstance());
    }

    public static boolean applyIfPending(Minecraft client) {
        if (!pending || client == null) return false;

        if (applyAttemptsRemaining <= 0) {
            clear();
            return false;
        }

        applyAttemptsRemaining--;

        Window w = client.getWindow();
        long handle = w.getWindow();

        if (handle == 0L) return false;

        GLFW.glfwSetCursorPos(handle, x, y);

        if (applyAttemptsRemaining <= 0) {
            clear();
        }
        return true;
    }

    public static void clear() {
        pending = false;
        applyAttemptsRemaining = 0;
    }

}

