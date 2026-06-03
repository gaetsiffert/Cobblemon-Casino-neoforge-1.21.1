package net.andrespr.casinorocket.villager.shops;

import net.andrespr.casinorocket.villager.VillagerTradeHelper;
import net.minecraft.nbt.ListTag;

public final class TMInstructorShops implements IShop {

    @Override
    public VillagerTradeHelper.ShopData build() {
        ListTag shops = new ListTag();

        // ===== ATTACK TMs =====
        ListTag attackTMs = new ListTag();
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_ember", "1000"));
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_watergun", "1000"));
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_razorleaf", "1000"));
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_mudslap", "1000"));
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_thundershock", "1500"));
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_icywind", "2000"));
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_confusion", "3000"));
        attackTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_rockthrow", "3000"));
        VillagerTradeHelper.addShopCompound(shops, "Attack TM's", attackTMs);

        // ===== STATUS TMs =====
        ListTag statusTMs = new ListTag();
        statusTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_supersonic", "1500"));
        statusTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_protect", "2000"));
        statusTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_leechseed", "2500"));
        statusTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_thunderwave", "3000"));
        statusTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_substitute", "5000"));
        statusTMs.add(VillagerTradeHelper.makeOffer("tmcraft:tm_taunt", "5000"));
        VillagerTradeHelper.addShopCompound(shops, "Status TM's", statusTMs);

        return new VillagerTradeHelper.ShopData(shops,"cobbledollars:cobble_merchant",1, "cobblemon:display_case");
    }

    @Override
    public String getName() {
        return "TM Instructor";
    }

}

