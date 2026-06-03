package net.andrespr.casinorocket.mixin;

import net.andrespr.casinorocket.inventory.ChipBankInventory;
import net.andrespr.casinorocket.util.IChipBankHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityChipBankMixin implements IChipBankHolder {

    @Unique
    private static final String CHIP_BANK_KEY = "CasinoRocketChipBank";

    @Unique
    private final ChipBankInventory casinorocket$chipBankInventory = new ChipBankInventory();

    @Override
    public ChipBankInventory casinorocket$getChipBankInventory() {
        return casinorocket$chipBankInventory;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void casinorocket$writeChipBank(CompoundTag nbt, CallbackInfo ci) {
        Player self = (Player) (Object) this;
        nbt.put(CHIP_BANK_KEY, casinorocket$chipBankInventory.createTag(self.registryAccess()));
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void casinorocket$readChipBank(CompoundTag nbt, CallbackInfo ci) {
        Player self = (Player) (Object) this;
        if (nbt.contains(CHIP_BANK_KEY, Tag.TAG_LIST)) {
            casinorocket$chipBankInventory.fromTag(
                    nbt.getList(CHIP_BANK_KEY, Tag.TAG_COMPOUND),
                    self.registryAccess()
            );
        }
    }

}

