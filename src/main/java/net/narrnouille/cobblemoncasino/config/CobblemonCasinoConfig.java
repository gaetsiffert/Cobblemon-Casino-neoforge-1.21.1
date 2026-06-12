package net.narrnouille.cobblemoncasino.config;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.narrnouille.cobblemoncasino.config.gachapon.ItemGachaponConfig;
import net.narrnouille.cobblemoncasino.config.gachapon.PlushiesGachaponConfig;
import net.narrnouille.cobblemoncasino.config.gachapon.PokemonGachaponConfig;
import net.narrnouille.cobblemoncasino.config.machines.BlackjackTableConfig;
import net.narrnouille.cobblemoncasino.config.machines.ChipTableConfig;
import net.narrnouille.cobblemoncasino.config.machines.GachaMachinesConfig;
import net.narrnouille.cobblemoncasino.config.machines.SlotMachineConfig;
import net.narrnouille.cobblemoncasino.config.npc.CobbledollarsDealerNpcConfig;
import net.narrnouille.cobblemoncasino.config.npc.ExchangerNpcConfig;
import net.narrnouille.cobblemoncasino.config.npc.PrizeDealerNpcConfig;

@Config(name = "cobblemoncasino")
public class CobblemonCasinoConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("general_config")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralConfig generalConfig = new GeneralConfig();

    @ConfigEntry.Category("item_gachapon")
    @ConfigEntry.Gui.TransitiveObject
    public ItemGachaponConfig itemGachapon = new ItemGachaponConfig();

    @ConfigEntry.Category("pokemon_gachapon")
    @ConfigEntry.Gui.TransitiveObject
    public PokemonGachaponConfig pokemonGachapon = new PokemonGachaponConfig();

    @ConfigEntry.Category("gacha_machines")
    @ConfigEntry.Gui.TransitiveObject
    public GachaMachinesConfig gachaMachines = new GachaMachinesConfig();

    @ConfigEntry.Category("plushies_gachapon")
    @ConfigEntry.Gui.TransitiveObject
    public PlushiesGachaponConfig plushiesGachapon = new PlushiesGachaponConfig();

    @ConfigEntry.Category("slot_machine")
    @ConfigEntry.Gui.TransitiveObject
    public SlotMachineConfig slotMachine = new SlotMachineConfig();

    @ConfigEntry.Category("blackjack_table")
    @ConfigEntry.Gui.TransitiveObject
    public BlackjackTableConfig blackjackTable = new BlackjackTableConfig();

    @ConfigEntry.Category("chip_table")
    @ConfigEntry.Gui.TransitiveObject
    public ChipTableConfig chipTable = new ChipTableConfig();

    @ConfigEntry.Category("npc/exchanger")
    @ConfigEntry.Gui.TransitiveObject
    public ExchangerNpcConfig exchangerNpc = new ExchangerNpcConfig();

    @ConfigEntry.Category("npc/prize_dealer")
    @ConfigEntry.Gui.TransitiveObject
    public PrizeDealerNpcConfig prizeDealerNpc = new PrizeDealerNpcConfig();

    @ConfigEntry.Category("npc/cobbledollars_dealer")
    @ConfigEntry.Gui.TransitiveObject
    public CobbledollarsDealerNpcConfig cobbledollarsDealerNpc = new CobbledollarsDealerNpcConfig();

}

