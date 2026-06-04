package net.andrespr.casinorocket.network;

import net.andrespr.casinorocket.CasinoRocket;
import net.andrespr.casinorocket.network.c2s.blackjack.BlackjackActionC2SPayload;
import net.andrespr.casinorocket.network.c2s.blackjack.ChangeBlackjackBetIndexC2SPayload;
import net.andrespr.casinorocket.network.c2s.chip_table.ChipTableConvertC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.DoBetC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.DoWithdrawC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.OpenBetScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.OpenMenuScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.OpenWithdrawScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.common.ReturnToMachineScreenC2SPayload;
import net.andrespr.casinorocket.network.c2s.slots.ChangeBetBaseC2SPayload;
import net.andrespr.casinorocket.network.c2s.slots.ChangeLinesModeC2SPayload;
import net.andrespr.casinorocket.network.c2s.slots.DoSpinC2SPayload;
import net.andrespr.casinorocket.network.c2s_handlers.blackjack.BlackjackActionReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.blackjack.ChangeBlackjackBetIndexReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.chip_table.ChipTableConvertReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.common.BetScreenReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.common.DoBetReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.common.DoWithdrawReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.common.MenuScreenReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.common.ReturnToMachineScreenReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.common.WithdrawScreenReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.slots.ChangeBetBaseReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.slots.ChangeLinesModeReceiver;
import net.andrespr.casinorocket.network.c2s_handlers.slots.DoSpinReceiver;
import net.andrespr.casinorocket.network.s2c.SendBlackjackStateS2CPayload;
import net.andrespr.casinorocket.network.s2c.SendMachineBalanceS2CPayload;
import net.andrespr.casinorocket.network.s2c.SendMenuSettingsS2CPayload;
import net.andrespr.casinorocket.network.s2c.SendSpinResultS2CPayload;
import net.andrespr.casinorocket.network.s2c.SlotConfigSyncS2CPayload;
import net.andrespr.casinorocket.network.s2c_handlers.BlackjackStateReceiver;
import net.andrespr.casinorocket.network.s2c_handlers.MachineBalanceReceiver;
import net.andrespr.casinorocket.network.s2c_handlers.MenuScreenSettingsReceiver;
import net.andrespr.casinorocket.network.s2c_handlers.SlotConfigSyncReceiver;
import net.andrespr.casinorocket.network.s2c_handlers.SpinResultReceiver;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class CasinoRocketPackets {

    private CasinoRocketPackets() {}

    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(CasinoRocket.NETWORK_VERSION);

        registrar.playToServer(OpenBetScreenC2SPayload.ID, OpenBetScreenC2SPayload.CODEC, BetScreenReceiver::openBetScreen);
        registrar.playToServer(DoBetC2SPayload.ID, DoBetC2SPayload.CODEC, DoBetReceiver::handle);
        registrar.playToServer(OpenWithdrawScreenC2SPayload.ID, OpenWithdrawScreenC2SPayload.CODEC, WithdrawScreenReceiver::openWithdrawScreen);
        registrar.playToServer(DoWithdrawC2SPayload.ID, DoWithdrawC2SPayload.CODEC, DoWithdrawReceiver::handle);
        registrar.playToServer(OpenMenuScreenC2SPayload.ID, OpenMenuScreenC2SPayload.CODEC, MenuScreenReceiver::openMenuScreen);
        registrar.playToServer(ReturnToMachineScreenC2SPayload.ID, ReturnToMachineScreenC2SPayload.CODEC, ReturnToMachineScreenReceiver::handle);
        registrar.playToServer(DoSpinC2SPayload.ID, DoSpinC2SPayload.CODEC, DoSpinReceiver::handle);
        registrar.playToServer(ChangeBetBaseC2SPayload.ID, ChangeBetBaseC2SPayload.CODEC, ChangeBetBaseReceiver::handle);
        registrar.playToServer(ChangeLinesModeC2SPayload.ID, ChangeLinesModeC2SPayload.CODEC, ChangeLinesModeReceiver::handle);
        registrar.playToServer(BlackjackActionC2SPayload.ID, BlackjackActionC2SPayload.CODEC, BlackjackActionReceiver::handle);
        registrar.playToServer(ChangeBlackjackBetIndexC2SPayload.ID, ChangeBlackjackBetIndexC2SPayload.CODEC, ChangeBlackjackBetIndexReceiver::handle);
        registrar.playToServer(ChipTableConvertC2SPayload.ID, ChipTableConvertC2SPayload.CODEC, ChipTableConvertReceiver::handle);

        registrar.playToClient(SendMachineBalanceS2CPayload.ID, SendMachineBalanceS2CPayload.CODEC, MachineBalanceReceiver::handle);
        registrar.playToClient(SendMenuSettingsS2CPayload.ID, SendMenuSettingsS2CPayload.CODEC, MenuScreenSettingsReceiver::handle);
        registrar.playToClient(SendSpinResultS2CPayload.ID, SendSpinResultS2CPayload.CODEC, SpinResultReceiver::handle);
        registrar.playToClient(SendBlackjackStateS2CPayload.ID, SendBlackjackStateS2CPayload.CODEC, BlackjackStateReceiver::handle);
        registrar.playToClient(SlotConfigSyncS2CPayload.ID, SlotConfigSyncS2CPayload.CODEC, SlotConfigSyncReceiver::handle);
        registrar.playToClient(SuitSyncPayload.ID, SuitSyncPayload.CODEC, SuitSync::handleClientPayload);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }
}


