package org.gneisscode.improvedmapcolors.neoforge;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.gneisscode.improvedmapcolors.networking.PresetSyncS2CPacket;
import org.gneisscode.improvedmapcolors.networking.SelectPresetC2SPayload;

public class PresetManagerImpl {

    public static void setAndSendSelectedPreset(PresetManager.Preset preset) {
        PresetManager.selectedPreset = preset;
        ClientPacketDistributor.sendToServer(new SelectPresetC2SPayload(preset));
    }

    public static void syncPresetOnPlayerJoin(ServerPlayer p) {
        PresetManager.Preset preset = PresetManager.getServerPreset(p.getServer());
        if(preset == null && PresetManager.selectedPreset == null) return;
        PacketDistributor.sendToPlayer(p, new PresetSyncS2CPacket(preset == null ? PresetManager.selectedPreset : preset));
    }


}
