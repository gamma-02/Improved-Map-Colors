package org.gneisscode.improvedmapcolors.neoforge.networking;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.networking.ColorListSyncPayload;

import java.awt.*;

public class SyncColorsImpl {

    public static void syncColors(ServerLevel level){

        Color[] colors = ColorListManager.buildSyncList();

        ColorListSyncPayload payload = new ColorListSyncPayload(colors);

        PacketDistributor.sendToAllPlayers(payload);


    }

    public static void syncColors(ServerPlayer p){
        Color[] colors = ColorListManager.buildSyncList();

        ColorListSyncPayload payload = new ColorListSyncPayload(colors);

        PacketDistributor.sendToPlayer(p, payload);
    }
}
