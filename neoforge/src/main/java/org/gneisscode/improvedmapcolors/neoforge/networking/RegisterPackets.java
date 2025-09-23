package org.gneisscode.improvedmapcolors.neoforge.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.gneisscode.improvedmapcolors.networking.ColorListSyncPayload;
import org.gneisscode.improvedmapcolors.networking.PresetSyncS2CPacket;
import org.gneisscode.improvedmapcolors.networking.SelectPresetC2SPayload;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class RegisterPackets {

    @SubscribeEvent
    public static void registerPacketPayloads(RegisterPayloadHandlersEvent evt){

        final PayloadRegistrar registrar = evt.registrar("1");
        registrar.commonToClient(
                ColorListSyncPayload.ID,
                ColorListSyncPayload.NEO_CODEC,
                (payload, ctx) -> {
//                    Minecraft.getInstance().getMapTextureManager().maps.forEach((id, instance) -> {
//                        instance.forceUpload();
//                    });

                    ColorListManager.handleSyncPayload(payload);
                }
        );

        registrar.commonToServer(
                SelectPresetC2SPayload.ID,
                SelectPresetC2SPayload.NEO_PAYLOAD_CODEC,
                (payload, ctx) ->{
                    if(ctx.player() instanceof ServerPlayer player){
                        PresetManager.handleSetPresetPacket(payload, player.getServer(), player);
                    }
                }
                );

        registrar.commonToClient(
                PresetSyncS2CPacket.ID,
                PresetSyncS2CPacket.NEO_PAYLOAD_CODEC,
                (payload, ctx) -> {
//                    Minecraft.getInstance().getMapTextureManager().maps.forEach((id, instance) -> {
//                        instance.();
//                    });

                    PresetManager.selectedPreset = payload.chosenPreset();

                }
                );

    }

}
