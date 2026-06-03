package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.games.slot.SlotSymbol;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import java.util.Arrays;

public record SlotConfigSyncS2CPayload(boolean debug, boolean useMoney, int reelSize, int[] reel1, int[] reel2, int[] reel3,
                                       int[] betValues, int mode1, int mode2, int mode3, long hash) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "slot_cfg_sync");
    public static final Type<SlotConfigSyncS2CPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, SlotConfigSyncS2CPayload> CODEC =
            StreamCodec.ofMember(SlotConfigSyncS2CPayload::write, SlotConfigSyncS2CPayload::read);

    private static void write(SlotConfigSyncS2CPayload p, RegistryFriendlyByteBuf buf) {
        buf.writeBoolean(p.debug());
        buf.writeBoolean(p.useMoney());
        buf.writeInt(p.reelSize());

        writeIntArray(buf, p.reel1());
        writeIntArray(buf, p.reel2());
        writeIntArray(buf, p.reel3());

        writeIntArray(buf, p.betValues());

        buf.writeInt(p.mode1());
        buf.writeInt(p.mode2());
        buf.writeInt(p.mode3());

        buf.writeLong(p.hash());
    }

    private static SlotConfigSyncS2CPayload read(RegistryFriendlyByteBuf buf) {
        boolean debug = buf.readBoolean();
        boolean useMoney = buf.readBoolean();
        int reelSize = buf.readInt();

        int[] r1 = readIntArray(buf);
        int[] r2 = readIntArray(buf);
        int[] r3 = readIntArray(buf);

        int[] betValues = readIntArray(buf);

        int mode1 = buf.readInt();
        int mode2 = buf.readInt();
        int mode3 = buf.readInt();

        long hash = buf.readLong();

        return new SlotConfigSyncS2CPayload(debug, useMoney, reelSize, r1, r2, r3, betValues, mode1, mode2, mode3, hash);
    }

    private static void writeIntArray(RegistryFriendlyByteBuf buf, int[] arr) {
        buf.writeInt(arr.length);
        for (int v : arr) buf.writeInt(v);
    }

    private static int[] readIntArray(RegistryFriendlyByteBuf buf) {
        int len = buf.readInt();
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) arr[i] = buf.readInt();
        return arr;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }

    // ===== Helpers to build from server state =====
    public static SlotConfigSyncS2CPayload fromServer() {
        var cfg = CasinoRocket.CONFIG.slotMachine;
        boolean useMoney = CasinoRocket.CONFIG.generalConfig.isCobbledollarsActive();

        SlotSymbol[][] strips = net.andrespr.casinorocket.games.slot.SlotReels.STRIPS;
        int[] r1 = toOrdinalArray(strips[0]);
        int[] r2 = toOrdinalArray(strips[1]);
        int[] r3 = toOrdinalArray(strips[2]);

        int[] bets = (useMoney ? cfg.bet_amounts_in_money : cfg.bet_amounts_in_items)
                .stream().mapToInt(Integer::intValue).toArray();

        long hash = Arrays.hashCode(r1) * 31L * 31L + Arrays.hashCode(r2) * 31L + Arrays.hashCode(r3);

        hash = hash * 31L + Arrays.hashCode(bets);
        hash = hash * 31L + cfg.bet_multipliers.mode1;
        hash = hash * 31L + cfg.bet_multipliers.mode2;
        hash = hash * 31L + cfg.bet_multipliers.mode3;
        hash = hash * 31L + cfg.reels.reelSize;

        return new SlotConfigSyncS2CPayload(
                cfg.debug,
                useMoney,
                cfg.reels.reelSize,
                r1, r2, r3, bets,
                cfg.bet_multipliers.mode1,
                cfg.bet_multipliers.mode2,
                cfg.bet_multipliers.mode3,
                hash
        );
    }

    private static int[] toOrdinalArray(SlotSymbol[] reel) {
        int[] out = new int[reel.length];
        for (int i = 0; i < reel.length; i++) out[i] = reel[i].ordinal();
        return out;
    }

}

