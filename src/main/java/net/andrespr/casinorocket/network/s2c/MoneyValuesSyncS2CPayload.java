package net.andrespr.casinorocket.network.s2c;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.item.ModItems;
import net.andrespr.casinorocket.item.custom.ChipItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.LinkedHashMap;
import java.util.Map;

public record MoneyValuesSyncS2CPayload(Map<String, Long> chipValues) implements CustomPacketPayload {

    public static final ResourceLocation ID_RAW = ResourceLocation.fromNamespaceAndPath(CasinoRocket.MOD_ID, "money_values_sync");
    public static final Type<MoneyValuesSyncS2CPayload> ID = new Type<>(ID_RAW);

    public static final StreamCodec<RegistryFriendlyByteBuf, MoneyValuesSyncS2CPayload> CODEC =
            StreamCodec.ofMember(MoneyValuesSyncS2CPayload::write, MoneyValuesSyncS2CPayload::read);

    public MoneyValuesSyncS2CPayload {
        chipValues = Map.copyOf(chipValues);
    }

    private static void write(MoneyValuesSyncS2CPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(payload.chipValues().size());
        for (Map.Entry<String, Long> entry : payload.chipValues().entrySet()) {
            buf.writeUtf(entry.getKey());
            buf.writeLong(entry.getValue());
        }
    }

    private static MoneyValuesSyncS2CPayload read(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<String, Long> values = new LinkedHashMap<>(size);
        for (int i = 0; i < size; i++) {
            values.put(buf.readUtf(), buf.readLong());
        }
        return new MoneyValuesSyncS2CPayload(values);
    }

    public static MoneyValuesSyncS2CPayload fromServer() {
        Map<String, Long> values = new LinkedHashMap<>();
        for (Item item : ModItems.ALL_CHIP_ITEMS) {
            if (item instanceof ChipItem chip) {
                values.put(BuiltInRegistries.ITEM.getKey(item).toString(), chip.getValue());
            }
        }
        return new MoneyValuesSyncS2CPayload(values);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
