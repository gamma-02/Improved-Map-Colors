package org.gneisscode.improvedmapcolors.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.gneisscode.improvedmapcolors.networking.PresetSyncS2CPacket;
import org.gneisscode.improvedmapcolors.networking.SelectPresetC2SPayload;

public class PresetManagerImpl {

    public static void setAndSendSelectedPreset(PresetManager.Preset preset) {
        PresetManager.selectedPreset = preset;
        ClientPlayNetworking.send(new SelectPresetC2SPayload(preset));
    }

    public static void syncPresetOnPlayerJoin(ServerPlayer p) {
        PresetManager.Preset preset = PresetManager.getServerPreset(p.getServer());
        if(preset == null && PresetManager.selectedPreset == null) return;
        ServerPlayNetworking.send(p, new PresetSyncS2CPacket(preset == null ? PresetManager.selectedPreset : preset));
    }

}
