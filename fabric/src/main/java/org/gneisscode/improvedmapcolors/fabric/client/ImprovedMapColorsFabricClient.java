package org.gneisscode.improvedmapcolors.fabric.client;

import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.gneisscode.improvedmapcolors.client.ImprovedMapColorsClient;
import org.gneisscode.improvedmapcolors.networking.ColorListSyncPayload;
import org.gneisscode.improvedmapcolors.networking.PresetSyncS2CPacket;

public final class ImprovedMapColorsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        ImprovedMapColorsClient.clientInit();

        ConfigScreenFactoryRegistry.INSTANCE.register(ImprovedMapColors.MOD_ID, (string, screen) -> {
            return new ConfigurationScreen(string, screen, (configScreen, type, config, title) -> {
                return new CustomConfigScreen(screen, type, config, title);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ColorListSyncPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                Minecraft.getInstance().getMapTextureManager().maps.forEach((id, instance) -> {
                    instance.forceUpload();
                });

                ColorListManager.handleSyncPayload(payload);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(PresetSyncS2CPacket.ID, (pl, ctx) -> {
            Minecraft.getInstance().getMapTextureManager().maps.forEach((id, instance) -> {
                instance.forceUpload();
            });

            PresetManager.selectedPreset = pl.chosenPreset();




        });

//        ClientPlayConnectionEvents.DISCONNECT.register((dc, mc) -> {
//            ColorListManager.onDisconnect();
//        });


    }
}
