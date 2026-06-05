package net.andrespr.casinorocket.screen.custom.ledger;

import net.andrespr.casinorocket.screen.ModGuiTextures;
import net.andrespr.casinorocket.screen.opening.CasinoLedgerOpenData;
import net.andrespr.casinorocket.screen.widget.CommonButton;
import net.andrespr.casinorocket.util.TextUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class CasinoLedgerScreen extends AbstractContainerScreen<CasinoLedgerScreenHandler> {

    private static final int RIGHT_PANEL_X = 100;
    private static final int RIGHT_PANEL_WIDTH = 138;
    private static final int LEADERBOARD_TITLE_Y = 115;
    private static final int LEADERBOARD_TITLE_LINE_HEIGHT = 10;
    private static final int LEADERBOARD_TITLE_GAP = 5;
    private static final int LEADERBOARD_BOTTOM = 198;
    private static final int LEADERBOARD_ROW_HEIGHT = 12;
    private static final int LEADERBOARD_SCROLLBAR_X = 233;
    private static final int LEADERBOARD_VALUE_RIGHT_X = LEADERBOARD_SCROLLBAR_X - 5;
    private static final int LEADERBOARD_CLIP_RIGHT_X = LEADERBOARD_SCROLLBAR_X - 3;

    private LedgerGame selectedGame = LedgerGame.SLOTS;
    private LedgerStat selectedStat = LedgerStat.HIGHEST_WIN;
    private int leaderboardScroll = 0;

    private CommonButton slotsButton;
    private CommonButton blackjackButton;
    private CommonButton highestWinButton;
    private CommonButton totalWonButton;
    private CommonButton totalLostButton;

    public CasinoLedgerScreen(CasinoLedgerScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = 248;
        this.imageHeight = 210;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 10;
        this.titleLabelY = 8;

        int x = this.leftPos;
        int y = this.topPos;

        this.slotsButton = new CommonButton(x + 10, y + 38, 82, 12, ModGuiTextures.BTN_LARGE,
                button -> setGame(LedgerGame.SLOTS), Component.translatable("gui.casinorocket.casino_ledger.slots"));
        this.blackjackButton = new CommonButton(x + 10, y + 53, 82, 12, ModGuiTextures.BTN_LARGE,
                button -> setGame(LedgerGame.BLACKJACK), Component.translatable("gui.casinorocket.casino_ledger.blackjack"));

        this.highestWinButton = new CommonButton(x + 10, y + 86, 82, 12, ModGuiTextures.BTN_LARGE,
                button -> setStat(LedgerStat.HIGHEST_WIN), Component.translatable("gui.casinorocket.casino_ledger.highest_win"));
        this.totalWonButton = new CommonButton(x + 10, y + 101, 82, 12, ModGuiTextures.BTN_LARGE,
                button -> setStat(LedgerStat.TOTAL_WON), Component.translatable("gui.casinorocket.casino_ledger.total_won"));
        this.totalLostButton = new CommonButton(x + 10, y + 116, 82, 12, ModGuiTextures.BTN_LARGE,
                button -> setStat(LedgerStat.TOTAL_LOST), Component.translatable("gui.casinorocket.casino_ledger.total_lost"));

        addRenderableWidget(this.slotsButton);
        addRenderableWidget(this.blackjackButton);
        addRenderableWidget(this.highestWinButton);
        addRenderableWidget(this.totalWonButton);
        addRenderableWidget(this.totalLostButton);

        updateButtonStates();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics context, float delta, int mouseX, int mouseY) {
        int x = this.leftPos;
        int y = this.topPos;

        context.fill(x, y, x + imageWidth, y + imageHeight, 0xF0181010);
        context.fill(x + 3, y + 3, x + imageWidth - 3, y + imageHeight - 3, 0xFF201515);
        context.fill(x + 5, y + 5, x + imageWidth - 5, y + 18, 0xFF4B0F14);
        context.fill(x + 5, y + imageHeight - 6, x + imageWidth - 5, y + imageHeight - 4, 0xFFFFD96A);
        context.fill(x + RIGHT_PANEL_X, y + 24, x + imageWidth - 10, y + 101, 0xDD0D0D12);
        context.fill(x + RIGHT_PANEL_X, y + 107, x + imageWidth - 10, y + imageHeight - 12, 0xDD0D0D12);
        context.fill(x + RIGHT_PANEL_X, y + 24, x + imageWidth - 10, y + 25, 0xAAFFD96A);
        context.fill(x + RIGHT_PANEL_X, y + 107, x + imageWidth - 10, y + 108, 0xAAFFD96A);
    }

    @Override
    protected void renderLabels(GuiGraphics context, int mouseX, int mouseY) {
        CasinoLedgerOpenData data = this.menu.getData();
        CasinoLedgerOpenData.GameStats gameStats = selectedGame.stats(data);
        CasinoLedgerOpenData.PlayerStats playerStats = gameStats.playerStats();
        List<CasinoLedgerOpenData.LeaderboardRow> rows = selectedStat.rows(gameStats);

        context.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0xFFD96A, false);
        context.drawString(this.font, Component.translatable("gui.casinorocket.casino_ledger.balance_label"),
                108, 31, 0xFFD96A, false);
        drawRightAligned(context, TextUtils.formatWithCommas(data.balance()) + " chips", 235, 42, 0xFFFFFF);

        context.drawString(this.font, Component.translatable("gui.casinorocket.casino_ledger.game"), 10, 28, 0xFFD96A, false);
        context.drawString(this.font, Component.translatable("gui.casinorocket.casino_ledger.record"), 10, 76, 0xFFD96A, false);

        context.drawString(this.font, Component.translatable("gui.casinorocket.casino_ledger.your_stats"), 108, 56, 0xFFD96A, false);
        drawValue(context, "gui.casinorocket.casino_ledger.highest_win", playerStats.highestWin(), 108, 68, false);
        drawValue(context, "gui.casinorocket.casino_ledger.total_won", playerStats.totalWon(), 108, 79, false);
        drawValue(context, "gui.casinorocket.casino_ledger.total_lost", playerStats.totalLost(), 108, 90, true);

        List<FormattedCharSequence> titleLines = leaderboardTitleLines();
        int titleY = LEADERBOARD_TITLE_Y;
        for (FormattedCharSequence line : titleLines) {
            context.drawString(this.font, line, 108, titleY, 0xFFD96A, false);
            titleY += LEADERBOARD_TITLE_LINE_HEIGHT;
        }

        int leaderboardTop = leaderboardTop(titleLines);

        if (rows.isEmpty()) {
            context.drawString(this.font, Component.translatable("gui.casinorocket.casino_ledger.no_records"),
                    108, leaderboardTop, 0xA0A0A0, false);
            return;
        }

        clampScroll(rows, leaderboardTop);
        int visibleRows = visibleRows(leaderboardTop);
        int end = Math.min(rows.size(), this.leaderboardScroll + visibleRows);

        context.enableScissor(this.leftPos + 104, this.topPos + leaderboardTop - 2,
                this.leftPos + LEADERBOARD_CLIP_RIGHT_X, this.topPos + LEADERBOARD_BOTTOM + 2);

        int rowY = leaderboardTop;
        for (int i = this.leaderboardScroll; i < end; i++) {
            CasinoLedgerOpenData.LeaderboardRow row = rows.get(i);
            int rankColor = switch (i) {
                case 0 -> 0xFFFFD96A;
                case 1 -> 0xFF8FE3FF;
                case 2 -> 0xFF7CFF8C;
                default -> 0xFFFFFFFF;
            };
            context.drawString(this.font, (i + 1) + ".", 108, rowY, rankColor, false);
            context.drawString(this.font, trim(row.playerName(), 62), 124, rowY, 0xFFFFFF, false);
            drawRightAligned(context, formatValue(row.value(), selectedStat == LedgerStat.TOTAL_LOST),
                    LEADERBOARD_VALUE_RIGHT_X, rowY, selectedStat == LedgerStat.TOTAL_LOST ? 0xFFFF6666 : 0xFF66FF88);
            rowY += LEADERBOARD_ROW_HEIGHT;
        }

        context.disableScissor();
        renderScrollbar(context, rows.size(), leaderboardTop);
    }

    private void drawValue(GuiGraphics context, String labelKey, long value, int x, int y, boolean signed) {
        context.drawString(this.font, Component.translatable(labelKey).getString() + ":", x, y, 0xA0A0A0, false);
        drawRightAligned(context, formatValue(value, signed), 235, y, signed && value < 0 ? 0xFFFF6666 : 0xFF66FF88);
    }

    private void drawRightAligned(GuiGraphics context, String text, int rightX, int y, int color) {
        String visible = trim(text, RIGHT_PANEL_WIDTH - 8);
        context.drawString(this.font, visible, rightX - this.font.width(visible), y, color, false);
    }

    private String formatValue(long value, boolean signed) {
        if (signed && value < 0) return "-" + TextUtils.formatCompactNoDecimal(-value);
        return TextUtils.formatCompactNoDecimal(value);
    }

    private String trim(String value, int width) {
        return this.font.plainSubstrByWidth(value, width);
    }

    private List<FormattedCharSequence> leaderboardTitleLines() {
        return this.font.split(Component.translatable("gui.casinorocket.casino_ledger.top",
                selectedGame.label(), selectedStat.label()), RIGHT_PANEL_WIDTH - 8);
    }

    private int leaderboardTop() {
        return leaderboardTop(leaderboardTitleLines());
    }

    private int leaderboardTop(List<FormattedCharSequence> titleLines) {
        int top = LEADERBOARD_TITLE_Y + titleLines.size() * LEADERBOARD_TITLE_LINE_HEIGHT + LEADERBOARD_TITLE_GAP;
        return Math.min(LEADERBOARD_BOTTOM - LEADERBOARD_ROW_HEIGHT, top);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!isOverLeaderboard(mouseX, mouseY)) {
            return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        }

        List<CasinoLedgerOpenData.LeaderboardRow> rows = selectedStat.rows(selectedGame.stats(this.menu.getData()));
        int maxScroll = Math.max(0, rows.size() - visibleRows(leaderboardTop()));
        if (maxScroll <= 0) return true;

        this.leaderboardScroll = Math.max(0, Math.min(maxScroll, this.leaderboardScroll - (int) Math.signum(scrollY)));
        return true;
    }

    private boolean isOverLeaderboard(double mouseX, double mouseY) {
        return mouseX >= this.leftPos + 104
                && mouseX <= this.leftPos + imageWidth - 12
                && mouseY >= this.topPos + leaderboardTop()
                && mouseY <= this.topPos + LEADERBOARD_BOTTOM;
    }

    private int visibleRows(int leaderboardTop) {
        return Math.max(1, (LEADERBOARD_BOTTOM - leaderboardTop) / LEADERBOARD_ROW_HEIGHT);
    }

    private void clampScroll(List<CasinoLedgerOpenData.LeaderboardRow> rows, int leaderboardTop) {
        int maxScroll = Math.max(0, rows.size() - visibleRows(leaderboardTop));
        this.leaderboardScroll = Math.max(0, Math.min(maxScroll, this.leaderboardScroll));
    }

    private void renderScrollbar(GuiGraphics context, int rowCount, int leaderboardTop) {
        int visibleRows = visibleRows(leaderboardTop);
        if (rowCount <= visibleRows) return;

        int trackX = LEADERBOARD_SCROLLBAR_X;
        int trackTop = leaderboardTop;
        int trackBottom = LEADERBOARD_BOTTOM;
        int trackHeight = trackBottom - trackTop;
        int thumbHeight = Math.max(12, trackHeight * visibleRows / rowCount);
        int maxScroll = Math.max(1, rowCount - visibleRows);
        int thumbY = trackTop + (trackHeight - thumbHeight) * this.leaderboardScroll / maxScroll;

        context.fill(trackX, trackTop, trackX + 2, trackBottom, 0xAA3C3030);
        context.fill(trackX - 1, thumbY, trackX + 3, thumbY + thumbHeight, 0xFFFFD96A);
    }

    private void setGame(LedgerGame game) {
        this.selectedGame = game;
        this.leaderboardScroll = 0;
        updateButtonStates();
    }

    private void setStat(LedgerStat stat) {
        this.selectedStat = stat;
        this.leaderboardScroll = 0;
        updateButtonStates();
    }

    private void updateButtonStates() {
        if (this.slotsButton == null) return;
        this.slotsButton.active = this.selectedGame != LedgerGame.SLOTS;
        this.blackjackButton.active = this.selectedGame != LedgerGame.BLACKJACK;
        this.highestWinButton.active = this.selectedStat != LedgerStat.HIGHEST_WIN;
        this.totalWonButton.active = this.selectedStat != LedgerStat.TOTAL_WON;
        this.totalLostButton.active = this.selectedStat != LedgerStat.TOTAL_LOST;
    }

    private enum LedgerGame {
        SLOTS("Slots") {
            @Override
            CasinoLedgerOpenData.GameStats stats(CasinoLedgerOpenData data) {
                return data.slots();
            }
        },
        BLACKJACK("Blackjack") {
            @Override
            CasinoLedgerOpenData.GameStats stats(CasinoLedgerOpenData data) {
                return data.blackjack();
            }
        };

        private final String label;

        LedgerGame(String label) {
            this.label = label;
        }

        String label() {
            return this.label;
        }

        abstract CasinoLedgerOpenData.GameStats stats(CasinoLedgerOpenData data);
    }

    private enum LedgerStat {
        HIGHEST_WIN("Highest Win") {
            @Override
            List<CasinoLedgerOpenData.LeaderboardRow> rows(CasinoLedgerOpenData.GameStats stats) {
                return stats.highestWin();
            }
        },
        TOTAL_WON("Total Won") {
            @Override
            List<CasinoLedgerOpenData.LeaderboardRow> rows(CasinoLedgerOpenData.GameStats stats) {
                return stats.totalWon();
            }
        },
        TOTAL_LOST("Total Lost") {
            @Override
            List<CasinoLedgerOpenData.LeaderboardRow> rows(CasinoLedgerOpenData.GameStats stats) {
                return stats.totalLost();
            }
        };

        private final String label;

        LedgerStat(String label) {
            this.label = label;
        }

        String label() {
            return this.label;
        }

        abstract List<CasinoLedgerOpenData.LeaderboardRow> rows(CasinoLedgerOpenData.GameStats stats);
    }
}
