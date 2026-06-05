package net.andrespr.casinorocket.screen.opening;

import net.andrespr.casinorocket.games.chip_table.ChipTableExchange;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;

import java.util.ArrayList;
import java.util.List;

public record ChipTableOpenData(BlockPos pos, boolean walletMode, InteractionHand walletHand,
                                List<ChipTableExchange.CurrencyValue> currencyValues,
                                boolean cobbledollarsAvailable) {

    public ChipTableOpenData(BlockPos pos, boolean walletMode) {
        this(pos, walletMode, InteractionHand.MAIN_HAND, List.of(), false);
    }

    public ChipTableOpenData(BlockPos pos, boolean walletMode, InteractionHand walletHand) {
        this(pos, walletMode, walletHand, List.of(), false);
    }

    public ChipTableOpenData(BlockPos pos, boolean walletMode, List<ChipTableExchange.CurrencyValue> currencyValues,
                             boolean cobbledollarsAvailable) {
        this(pos, walletMode, InteractionHand.MAIN_HAND, currencyValues, cobbledollarsAvailable);
    }

    public ChipTableOpenData {
        if (walletHand == null) walletHand = InteractionHand.MAIN_HAND;
        currencyValues = List.copyOf(currencyValues);
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ChipTableOpenData> CODEC =
            StreamCodec.ofMember(ChipTableOpenData::write, ChipTableOpenData::read);

    private static void write(ChipTableOpenData data, RegistryFriendlyByteBuf buf) {
        buf.writeBlockPos(data.pos());
        buf.writeBoolean(data.walletMode());
        buf.writeEnum(data.walletHand());
        buf.writeBoolean(data.cobbledollarsAvailable());
        buf.writeVarInt(data.currencyValues().size());
        for (ChipTableExchange.CurrencyValue value : data.currencyValues()) {
            buf.writeUtf(value.itemId());
            buf.writeLong(value.value());
        }
    }

    private static ChipTableOpenData read(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        boolean walletMode = buf.readBoolean();
        InteractionHand walletHand = buf.readEnum(InteractionHand.class);
        boolean cobbledollarsAvailable = buf.readBoolean();
        int valueCount = buf.readVarInt();
        List<ChipTableExchange.CurrencyValue> currencyValues = new ArrayList<>(valueCount);
        for (int i = 0; i < valueCount; i++) {
            currencyValues.add(new ChipTableExchange.CurrencyValue(buf.readUtf(), buf.readLong()));
        }
        return new ChipTableOpenData(pos, walletMode, walletHand, currencyValues, cobbledollarsAvailable);
    }

}
