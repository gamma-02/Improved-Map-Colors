package org.gneisscode.improvedmapcolors.neoforge.networking;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.networking.ColorListSyncPayload;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.COLOR_LIST_SYNC_PAYLOAD_ID;
import static org.gneisscode.improvedmapcolors.ImprovedMapColors.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class RegisterPackets {

    @SubscribeEvent
    public static void registerPacketPayloads(RegisterPayloadHandlersEvent evt){

        final PayloadRegistrar registrar = evt.registrar("1");
        registrar.commonToClient(
                ColorListSyncPayload.ID,
                ColorListSyncPayload.NEO_CODEC,
                (payload, ctx) -> ColorListManager.handleSyncPayload(payload)
        );

    }

}
