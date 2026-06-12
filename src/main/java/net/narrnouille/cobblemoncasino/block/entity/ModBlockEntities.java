package net.narrnouille.cobblemoncasino.block.entity;

import net.narrnouille.cobblemoncasino.CobblemonCasino;
import net.narrnouille.cobblemoncasino.block.ModBlocks;
import net.narrnouille.cobblemoncasino.block.entity.custom.BlackjackTableEntity;
import net.narrnouille.cobblemoncasino.block.entity.custom.ChipTableEntity;
import net.narrnouille.cobblemoncasino.block.entity.custom.SlotMachineEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CobblemonCasino.MOD_ID);

    public static BlockEntityType<SlotMachineEntity> SLOT_MACHINE_BE;
    public static BlockEntityType<BlackjackTableEntity> BLACKJACK_TABLE_BE;
    public static BlockEntityType<ChipTableEntity> CHIP_TABLE_BE;

    static {
        register("slot_machine_be", () -> SLOT_MACHINE_BE =
                BlockEntityType.Builder.of(SlotMachineEntity::new, ModBlocks.SLOT_MACHINE).build(null));
        register("blackjack_table_be", () -> BLACKJACK_TABLE_BE =
                BlockEntityType.Builder.of(BlackjackTableEntity::new, ModBlocks.BLACKJACK_TABLE).build(null));
        register("chip_table_be", () -> CHIP_TABLE_BE =
                BlockEntityType.Builder.of(ChipTableEntity::new, ModBlocks.CHIP_TABLE).build(null));
    }

    private static <T extends BlockEntityType<?>> void register(String name, Supplier<T> supplier) {
        BLOCK_ENTITY_TYPES.register(name, supplier);
    }

    public static void registerBlockEntities(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
        CobblemonCasino.LOGGER.info("Registering Block Entities for " + CobblemonCasino.MOD_ID);
    }
}
