package net.narrnouille.cobblemoncasino.network.s2c;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.games.blackjack.BlackjackPhase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SendBlackjackStateS2CPayload(

        BlockPos pos,
        String machineKey,

        long balance,
        int betIndex,
        long[] betValues,
        long currentBet,

        BlackjackPhase phase,
        long winPayout,
        int resultSeq,

        int resultId,
        long resolvedBet,
        long resolvedPayout,

        boolean dealerHoleRevealed,
        int[] playerCards,
        int[] dealerCards,

        String playerValueText,
        String dealerValueText,

        boolean canPlay,
        boolean canHit,
        boolean canStand,
        boolean canDoubleDown,
        boolean canFinish,
        boolean canDoubleOrNothing

) implements CustomPacketPayload {

    public static final Type<SendBlackjackStateS2CPayload> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CobblemonCasino.MOD_ID, "blackjack_state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SendBlackjackStateS2CPayload> CODEC =
            StreamCodec.ofMember(SendBlackjackStateS2CPayload::write, SendBlackjackStateS2CPayload::read);

    private static void write(SendBlackjackStateS2CPayload p, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(p.pos());
        buf.writeUtf(p.machineKey());

        buf.writeLong(p.balance());
        buf.writeInt(p.betIndex());
        writeLongArray(buf, p.betValues());
        buf.writeLong(p.currentBet());

        buf.writeEnum(p.phase());
        buf.writeLong(p.winPayout());
        buf.writeVarInt(p.resultSeq());

        buf.writeVarInt(p.resultId());
        buf.writeLong(p.resolvedBet());
        buf.writeLong(p.resolvedPayout());

        buf.writeBoolean(p.dealerHoleRevealed());
        writeIntArray(buf, p.playerCards());
        writeIntArray(buf, p.dealerCards());

        buf.writeUtf(p.playerValueText());
        buf.writeUtf(p.dealerValueText());

        buf.writeBoolean(p.canPlay());
        buf.writeBoolean(p.canHit());
        buf.writeBoolean(p.canStand());
        buf.writeBoolean(p.canDoubleDown());
        buf.writeBoolean(p.canFinish());
        buf.writeBoolean(p.canDoubleOrNothing());
    }

    private static SendBlackjackStateS2CPayload read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        String key = buf.readUtf();

        long balance = buf.readLong();
        int betIndex = buf.readInt();
        long[] betValues = readLongArray(buf);
        long currentBet = buf.readLong();

        BlackjackPhase phase = buf.readEnum(BlackjackPhase.class);
        long winPayout = buf.readLong();
        int resultSeq = buf.readVarInt();

        int resultId = buf.readVarInt();
        long resolvedBet = buf.readLong();
        long resolvedPayout = buf.readLong();

        boolean revealed = buf.readBoolean();
        int[] playerCards = readIntArray(buf);
        int[] dealerCards = readIntArray(buf);

        String playerText = buf.readUtf();
        String dealerText = buf.readUtf();

        boolean canPlay = buf.readBoolean();
        boolean canHit = buf.readBoolean();
        boolean canStand = buf.readBoolean();
        boolean canDoubleDown = buf.readBoolean();
        boolean canFinish = buf.readBoolean();
        boolean canDoN = buf.readBoolean();

        return new SendBlackjackStateS2CPayload(
                pos, key,
                balance, betIndex, betValues, currentBet,
                phase, winPayout, resultSeq,
                resultId, resolvedBet, resolvedPayout,
                revealed, playerCards, dealerCards,
                playerText, dealerText,
                canPlay, canHit, canStand, canDoubleDown, canFinish, canDoN
        );
    }

    private static void writeIntArray(RegistryFriendlyByteBuf buf, int[] arr) {
        buf.writeVarInt(arr.length);
        for (int v : arr) buf.writeVarInt(v);
    }

    private static int[] readIntArray(RegistryFriendlyByteBuf buf) {
        int n = buf.readVarInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = buf.readVarInt();
        return arr;
    }

    private static void writeLongArray(RegistryFriendlyByteBuf buf, long[] arr) {
        buf.writeVarInt(arr.length);
        for (long value : arr) buf.writeLong(value);
    }

    private static long[] readLongArray(RegistryFriendlyByteBuf buf) {
        int n = buf.readVarInt();
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) arr[i] = buf.readLong();
        return arr;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

}

