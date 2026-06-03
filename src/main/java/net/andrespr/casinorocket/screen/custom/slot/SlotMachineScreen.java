package net.andrespr.casinorocket.screen.custom.slot;

import com.mojang.blaze3d.systems.RenderSystem;
import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.slot.SlotLineResult;
import net.andrespr.casinorocket.games.slot.SlotMachineConstants;
import net.andrespr.casinorocket.games.slot.SlotReels;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.andrespr.casinorocket.network.c2s.slots.DoSpinC2SPayload;
import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.custom.CasinoMachineScreen;
import net.andrespr.casinorocket.screen.layout.DancingClefairy;
import net.andrespr.casinorocket.screen.layout.SlotLineSprite;
import net.andrespr.casinorocket.screen.opening.MouseRestore;
import net.andrespr.casinorocket.screen.widget.ModButtons;
import net.andrespr.casinorocket.screen.widget.SlotButton;
import net.andrespr.casinorocket.sound.ModSounds;
import net.andrespr.casinorocket.util.CasinoRocketLogger;
import net.andrespr.casinorocket.util.TextUtils;
import net.andrespr.casinorocket.network.CasinoRocketPackets;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.EnumSet;
import java.util.Set;

public class SlotMachineScreen extends CasinoMachineScreen<SlotMachineScreenHandler> {

    private long balance = 0L;
    private long pendingBalance = -1L;
    private int betAmount = 10;

    private List<SlotLineResult> lastWins = List.of();
    private long lastWinAmount = 0;
    private long pendingWinAmount = -1;
    private int linesMode = 1;

    // --- BUTTONS ---
    private SlotButton spinButton;
    private SlotButton betButton;
    private SlotButton menuButton;
    private SlotButton withdrawButton;

    // --- LINES ---
    private SlotLineSprite lineOneSprite;
    private SlotLineSprite lineTwoSprite;
    private SlotLineSprite lineThreeTopSprite;
    private SlotLineSprite lineThreeBottomSprite;

    private enum WinLine {
        CENTER,
        TOP,
        BOTTOM,
        DIAG_TOP,
        DIAG_BOTTOM
    }

    private final Set<WinLine> flashingLines = EnumSet.noneOf(WinLine.class);

    private final Set<WinLine> pendingFlashingLines = EnumSet.noneOf(WinLine.class);
    private int pendingFlashTicks = 0;
    private boolean flashVisible = true;
    private int flashTicksRemaining = 0;
    private int flashTickCounter = 0;

    // --- REEL / LAYOUT CONSTANTS --
    private static final int SYMBOL_SIZE = 32;
    private static final int[] COLUMN_X = { 55 , 94 , 133 };
    private static final int ROW_Y = 53;
    private static final int VIRTUAL_ROWS = 5;

    // --- ANIMATION / STRIPS ---
    private boolean isSpinning = false;

    private final int[] reelIndex = new int[3];
    private final int[] targetTopIndex = new int[3];

    private final float[] reelOffset = new float[3];
    private final int[] reelTimer = new int[3];
    private final boolean[] reelSpinning = new boolean[3];
    private static final float reelVelocity = 24.0f;

    // --- CLEFAIRY ---
    private DancingClefairy clefairy;
    private boolean hasSpunOnce = false;

    public SlotMachineScreen(SlotMachineScreenHandler handler, Inventory inv, Component title) {
        super(handler, inv, title);
        this.imageWidth = 220;
        this.imageHeight = 205;
    }

    // === INIT ===
    @Override
    @SuppressWarnings("unused")
    protected void init() {
        super.init();

        initRandomReels();

        int baseX = (this.width - this.imageWidth) / 2;
        int baseY = (this.height - this.imageHeight) / 2;

        this.betButton = ModButtons.bet(baseX, baseY, 4, 4, b -> onBetPressed());
        this.menuButton = ModButtons.menu(baseX, baseY, 83, 4, b -> onMenuPressed());
        this.withdrawButton = ModButtons.withdraw(baseX, baseY, 144, 4, b -> onWithdrawPressed());
        this.spinButton = ModButtons.spin(baseX, baseY, 90, 161, b -> onSpinPressed());

        this.addRenderableWidget(betButton);
        this.addRenderableWidget(menuButton);
        this.addRenderableWidget(withdrawButton);
        this.addRenderableWidget(spinButton);

        lineOneSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_ONE, 160, 14);
        lineTwoSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_TWO, 160, 14);
        lineThreeTopSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_THREE_TOP, 160, 112);
        lineThreeBottomSprite = new SlotLineSprite(ModGuiTextures.SLOT_LINE_THREE_BOTTOM, 160, 112);

        clefairy = new DancingClefairy(ModGuiTextures.DANCING_CLEFAIRY, 22, 23);

        updateDisplay(
                this.menu.getInitialBalance(),
                this.menu.getInitialBetBase(),
                this.menu.getInitialLinesMode()
        );
    }

    private void initRandomReels() {
        for (int col = 0; col < 3; col++) {
            int len = SlotReels.STRIPS[col].length;
            reelIndex[col] = ThreadLocalRandom.current().nextInt(len);
            reelOffset[col] = 0f;
            reelSpinning[col] = false;
            reelTimer[col] = 0;
            targetTopIndex[col] = reelIndex[col];
        }
    }

    // === BUTTONS ===
    private void onSpinPressed() {
        if (minecraft != null && minecraft.player != null && !isSpinning) {
            CasinoRocketPackets.sendToServer(new DoSpinC2SPayload());
        }
    }

    // === SERVER RESULT ===
    public void onSpinResult(SlotSymbol[][] matrix, List<SlotLineResult> wins, int modeUsed,
                             long totalWin, long newBalance, int stop1, int stop2, int stop3) {
        this.lastWins = wins;
        this.linesMode = modeUsed;
        this.pendingWinAmount = totalWin;
        this.pendingBalance = newBalance;

        debugCheckSpin(matrix, stop1, stop2, stop3);
        pendingFlashingLines.clear();
        pendingFlashTicks = 0;

        if (!wins.isEmpty()) {
            int maxFlash = 0;

            for (SlotLineResult win : wins) {
                WinLine lineId = mapWinToLine(win);
                pendingFlashingLines.add(lineId);

                int lineFlash = getFlashDurationForLine(win);
                if (lineFlash > maxFlash) {
                    maxFlash = lineFlash;
                }
            }

            pendingFlashTicks = maxFlash;
        }

        for (int col = 0; col < 3; col++) {
            SlotSymbol[] strip = SlotReels.STRIPS[col];
            int len = strip.length;

            int stop = switch (col) {
                case 0 -> stop1;
                case 1 -> stop2;
                default -> stop3;
            };

            targetTopIndex[col] = Math.floorMod(stop - 2, len);
        }

        startSpinAnimation();
    }

    // === SPIN ANIMATION ===
    private void startSpinAnimation() {

        isSpinning = true;
        hasSpunOnce = true;
        updateSpinButtonState();

        if (minecraft != null && minecraft.player != null) {
            minecraft.player.playSound(ModSounds.REELS_SPINNING, 1.0f, 1.0f);
        }

        if (this.spinButton != null) this.spinButton.active = false;

        // Minecraft ticks of spinning
        reelTimer[0] = 40;  // Left Reel
        reelTimer[1] = 46;  // Middle Reel
        reelTimer[2] = 54;  // Right Reel

        for (int col = 0; col < 3; col++) {
            reelOffset[col] = 0f;
            reelSpinning[col] = true;
        }

    }

    private void finishSpin() {
        isSpinning = false;

        if (pendingBalance >= 0) {
            this.balance = pendingBalance;
            this.pendingBalance = -1L;
        }

        if (pendingWinAmount >= 0) {
            this.lastWinAmount = pendingWinAmount;
            this.pendingWinAmount = -1;
        }

        if (this.spinButton != null) {
            this.spinButton.active = true;
        }

        flashingLines.clear();
        if (pendingFlashTicks > 0 && !pendingFlashingLines.isEmpty()) {
            flashingLines.addAll(pendingFlashingLines);
            flashTicksRemaining = pendingFlashTicks;
            flashVisible = true;
            flashTickCounter = 0;
            playWinSound(lastWins);
        }

        updateSpinButtonState();

    }

    // === EVERY TICK ===
    @Override
    public void containerTick() {
        super.containerTick();
        MouseRestore.applyIfPending(minecraft);

        if (isSpinning) {
            boolean anySpinning = false;

            for (int col = 0; col < 3; col++) {
                if (!reelSpinning[col]) continue;

                anySpinning = true;

                int len = SlotReels.STRIPS[col].length;

                reelOffset[col] += reelVelocity;

                if (reelOffset[col] >= SYMBOL_SIZE) {
                    reelOffset[col] -= SYMBOL_SIZE;
                    reelIndex[col] = (reelIndex[col] + 1) % len;
                }

                reelTimer[col]--;

                if (reelTimer[col] <= 0) {
                    reelIndex[col] = targetTopIndex[col];
                    reelOffset[col] = 0f;
                    reelSpinning[col] = false;
                }
            }

            if (!anySpinning) {
                finishSpin();
            }
        } else {
            if (flashTicksRemaining > 0 && !flashingLines.isEmpty()) {
                flashTicksRemaining--;
                flashTickCounter++;

                if (flashTickCounter >= 2) {
                    flashTickCounter = 0;
                    flashVisible = !flashVisible;
                }

                if (flashTicksRemaining <= 0) {
                    flashingLines.clear();
                    flashVisible = true;
                }
            }
        }

        updateSpinButtonState();
        updateUiLockState();

        if (clefairy != null) {
            clefairy.tick(getClefairyPhase());
        }

    }

    // === DRAW ===
    private void drawSymbols(GuiGraphics ctx, int originX, int originY) {
        for (int col = 0; col < 3; col++) {
            SlotSymbol[] strip = SlotReels.STRIPS[col];
            int len = strip.length;

            for (int row = 0; row < VIRTUAL_ROWS; row++) {

                int stripIndex = (reelIndex[col] + row) % len;
                SlotSymbol symbol = strip[stripIndex];
                if (symbol == null) continue;

                ResourceLocation texture = ModGuiTextures.SlotTextures.SYMBOL_TEXTURES.get(symbol);

                int drawX = originX + COLUMN_X[col];
                int drawY = originY + ROW_Y + ((row - 1) * SYMBOL_SIZE) + (int) reelOffset[col];

                ctx.blit(texture, drawX, drawY,
                        0, 0, SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE, SYMBOL_SIZE);
            }
        }
    }

    private void drawLines(GuiGraphics context, int originX, int originY) {
        if (lineOneSprite == null) return;

        int x = originX + 30;

        int yDiag = originY + 47;
        int yTop = originY + 70;
        int yMid = originY + 96;
        int yBottom = originY + 122;

        boolean centerOnByMode = (linesMode >= 1);
        boolean horizontalsOnByMode = (linesMode >= 2);
        boolean diagonalsOnByMode = (linesMode >= 3);

        boolean centerWinning = flashingLines.contains(WinLine.CENTER);
        boolean topWinning = flashingLines.contains(WinLine.TOP);
        boolean bottomWinning = flashingLines.contains(WinLine.BOTTOM);
        boolean diagTopWinning = flashingLines.contains(WinLine.DIAG_TOP);
        boolean diagBottomWinning = flashingLines.contains(WinLine.DIAG_BOTTOM);

        boolean centerEnabled = centerOnByMode && (!centerWinning || flashVisible);
        boolean topEnabled = horizontalsOnByMode && (!topWinning || flashVisible);
        boolean bottomEnabled = horizontalsOnByMode && (!bottomWinning || flashVisible);
        boolean diagTopEnabled = diagonalsOnByMode && (!diagTopWinning || flashVisible);
        boolean diagBottomEnabled = diagonalsOnByMode && (!diagBottomWinning || flashVisible);

        lineThreeTopSprite.render(context, x, yDiag, diagTopEnabled);
        lineTwoSprite.render(context, x, yTop, topEnabled);
        lineOneSprite.render(context, x, yMid, centerEnabled);
        lineTwoSprite.render(context, x, yBottom, bottomEnabled);
        lineThreeBottomSprite.render(context, x, yDiag, diagBottomEnabled);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, ModGuiTextures.SLOT_MACHINE_GUI);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        context.blit(ModGuiTextures.REELS, x + 50, y + 47, 0, 0, 120, 112, 120, 112);
        drawSymbols(context, x, y);
        context.blit(ModGuiTextures.SLOT_MACHINE_GUI, x, y, 0, 0, imageWidth, imageHeight);
        drawLines(context, x, y);

        if (clefairy != null) {
            clefairy.render(context, x + 7,   y + 131);
            clefairy.render(context, x + 191, y + 131);
        }

        if (CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive()) {
            context.blit(ModGuiTextures.COBBLEDOLLARS_GREEN, x + 151, y + 34, 0, 0, 12, 12, 12,12);
            context.blit(ModGuiTextures.COBBLEDOLLARS_GREEN, x + 69, y + 184, 0, 0, 12, 12, 12,12);
            context.blit(ModGuiTextures.COBBLEDOLLARS_GREEN, x + 193, y + 184, 0, 0, 12, 12, 12,12);
        }
        if (CasinoRocket.CONFIG.generalConfig.isRelicCoinActive()) {
            context.blit(ModGuiTextures.RELIC_COIN_GREEN, x + 151, y + 34, 0, 0, 12, 12, 12,12);
            context.blit(ModGuiTextures.RELIC_COIN_GREEN, x + 69, y + 184, 0, 0, 12, 12, 12,12);
            context.blit(ModGuiTextures.RELIC_COIN_GREEN, x + 193, y + 184, 0, 0, 12, 12, 12,12);
        }
        if (CasinoRocket.CONFIG.generalConfig.isDiamondActive()) {
            context.blit(ModGuiTextures.DIAMOND_GREEN, x + 151, y + 34, 0, 0, 12, 12, 12, 12);
            context.blit(ModGuiTextures.DIAMOND_GREEN, x + 69, y + 184, 0, 0, 12, 12, 12, 12);
            context.blit(ModGuiTextures.DIAMOND_GREEN, x + 193, y + 184, 0, 0, 12, 12, 12, 12);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        drawBalanceAmount(context);
        drawBetAmount(context);
        drawWinAmount(context);
    }

    // === BLOCK ESC / CLOSE SCREEN ===
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (isBusy() && minecraft != null && minecraft.player != null) {
            if (keyCode == 256 /* GLFW.GLFW_KEY_ESCAPE */) {
                CasinoRocketLogger.toPlayerTranslated(minecraft.player, "gui.casinorocket.slot_machine.esc", true);
                return true;
            }
            if (minecraft.options != null && minecraft.options.keyInventory.matches(keyCode, scanCode)) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        if (isBusy()) return;
        super.onClose();
    }

    @Override
    protected boolean returnToMachineOnEsc() {
        return false;
    }

    // === UPDATERS ===
    public void updateBalance(long amount) {
        this.balance = amount;
    }

    public void updateDisplay(long balance, int betBase, int linesMode) {
        this.balance = balance;
        this.linesMode = linesMode;

        int mult = SlotMachineConstants.getBetMultiplierForMode(linesMode);
        this.betAmount = betBase * mult;
    }

    private void updateSpinButtonState() {
        if (spinButton == null) return;

        boolean spinning = isSpinning;
        boolean flashing = flashTicksRemaining > 0;

        boolean busy = spinning || flashing;

        spinButton.active = !busy;
        spinButton.setForcedPressed(busy);
    }

    // === HELPERS: LINES ===
    private WinLine mapWinToLine(SlotLineResult win) {
        int idx = win.lineIndex();

        if (this.linesMode == 1) {
            return WinLine.CENTER;
        }

        if (this.linesMode == 2) {
            return switch (idx) {
                case 0 -> WinLine.TOP;
                case 1 -> WinLine.CENTER;
                case 2 -> WinLine.BOTTOM;
                default -> WinLine.CENTER;
            };
        }

        return switch (idx) {
            case 0 -> WinLine.TOP;
            case 1 -> WinLine.CENTER;
            case 2 -> WinLine.BOTTOM;
            case 3 -> WinLine.DIAG_TOP;
            case 4 -> WinLine.DIAG_BOTTOM;
            default -> WinLine.CENTER;
        };

    }

    private int getFlashDurationForLine(SlotLineResult win) {
        SlotSymbol symbol = win.symbol();

        int seconds;
        if (symbol == null) {
            seconds = 2;
        } else {
            switch (symbol) {
                case PIKACHU -> seconds = 3;
                case MEW     -> seconds = 4;
                case ROCKET  -> seconds = 6;
                case SEVEN   -> seconds = 8;
                default      -> seconds = 2;
            }
        }

        return seconds * 20;
    }

    // === HELPER: SOUND ===
    private void playWinSound(List<SlotLineResult> wins) {
        if (minecraft == null || minecraft.player == null) return;
        if (wins.isEmpty()) return;

        SlotSymbol best = null;

        for (SlotLineResult win : wins) {
            SlotSymbol s = win.symbol();
            if (s == null) continue;

            if (best == null || s.getTripleMultiplier() > best.getTripleMultiplier()) {
                best = s;
            }
        }

        if (best == null) return;

        switch (best) {
            case SEVEN -> minecraft.player.playSound(ModSounds.JACKPOT, 1f, 1f);
            case ROCKET -> minecraft.player.playSound(ModSounds.LEGENDARY_PRIZE, 1f, 1f);
            case MEW -> minecraft.player.playSound(ModSounds.ULTRARARE_PRIZE, 1f, 1f);
            case PIKACHU -> minecraft.player.playSound(ModSounds.RARE_PRIZE, 1f, 1f);
            case BULBASAUR, SQUIRTLE, CHARMANDER ->
                    minecraft.player.playSound(ModSounds.COMMON_PRIZE, 1f, 1f);
            case CHERRY -> minecraft.player.playSound(ModSounds.BONUS_PRIZE, 1f, 1f);
            default -> {} // No sound
        }
    }

    // === HELPER: CLEFAIRY ===
    private DancingClefairy.Phase getClefairyPhase() {
        if (isSpinning) return DancingClefairy.Phase.NEUTRAL;
        if (!hasSpunOnce) return DancingClefairy.Phase.NEUTRAL;
        if (lastWinAmount > 0) {
            return DancingClefairy.Phase.WIN;
        } else {
            return DancingClefairy.Phase.LOSS;
        }
    }

    // === HELPERS: BLOCK BUTTONS ===
    private boolean isBusy() {
        boolean flashing = flashTicksRemaining > 0 && !flashingLines.isEmpty();
        return isSpinning || flashing;
    }

    private void updateUiLockState() {
        boolean busy = isBusy();

        if (spinButton != null) {
            spinButton.active = !busy;
            spinButton.setForcedPressed(busy);
        }

        if (betButton != null) betButton.active = !busy;
        if (menuButton != null) menuButton.active = !busy;
        if (withdrawButton != null) withdrawButton.active = !busy;

        if (betButton != null) betButton.setFakePressed(busy);
        if (menuButton != null) menuButton.setFakePressed(busy);
        if (withdrawButton != null) withdrawButton.setFakePressed(busy);

    }

    // === HELPERS: TEXT ===
    private void drawBetAmount(GuiGraphics context) {
        String formatted = TextUtils.formatCompact(betAmount);

        int width = font.width(formatted);
        int drawX = Math.max(146 - width, 120);

        context.drawString(font, formatted, drawX, 36, betAmount > balance ? 0xFF5555 : 0x00FF00, true);
        context.drawString(font, "Bet Amount", 60, 36, 0xFFFFFF, true);
    }

    private void drawBalanceAmount(GuiGraphics context) {
        String formatted = TextUtils.formatLarge(balance);

        int width = font.width(formatted);
        int drawX = Math.max(64 - width, 17);

        context.drawString(font, formatted, drawX, 186,0x00FF00, true);
    }

    private void drawWinAmount(GuiGraphics context) {
        String formatted = TextUtils.formatLarge(lastWinAmount);

        int width = font.width(formatted);
        int drawX = Math.max(188 - width, 141);

        context.drawString(font, formatted, drawX, 186, 0x00FF00, true);
    }

    private void debugCheckSpin(SlotSymbol[][] matrix, int stop1, int stop2, int stop3) {
        if (!net.andrespr.casinorocket.games.slot.SlotClientSynced.DEBUG) return;
        if (minecraft == null || minecraft.player == null) return;

        int[] stops = { stop1, stop2, stop3 };

        boolean ok = true;
        StringBuilder sb = new StringBuilder();

        for (int col = 0; col < 3; col++) {
            SlotSymbol[] strip = SlotReels.STRIPS[col];
            int len = strip.length;

            int stop = stops[col];

            // What should be according to the client strip (synced)
            SlotSymbol expTop = strip[Math.floorMod(stop - 1, len)];
            SlotSymbol expMid = strip[Math.floorMod(stop, len)];
            SlotSymbol expBot = strip[Math.floorMod(stop + 1, len)];

            // What the server claimed came out (payload matrix)
            SlotSymbol srvTop = matrix[0][col];
            SlotSymbol srvMid = matrix[1][col];
            SlotSymbol srvBot = matrix[2][col];

            boolean colOk = (expTop == srvTop && expMid == srvMid && expBot == srvBot);
            ok &= colOk;

            if (!colOk) {
                sb.append("Col ").append(col)
                        .append(" stop=").append(stop)
                        .append(" expected=[").append(expTop).append(",").append(expMid).append(",").append(expBot).append("]")
                        .append(" server=[").append(srvTop).append(",").append(srvMid).append(",").append(srvBot).append("]  ");
            }
        }

        if (ok) {
            minecraft.player.displayClientMessage(net.minecraft.network.chat.Component.literal("§a[SlotDebug] OK (no desync)"), true); // actionbar
        } else {
            minecraft.player.displayClientMessage(net.minecraft.network.chat.Component.literal("§c[SlotDebug] DESYNC! " + sb), false); // chat
        }
    }

    // GETTERS
    public boolean isSpinning() {
        return this.isSpinning;
    }

}

