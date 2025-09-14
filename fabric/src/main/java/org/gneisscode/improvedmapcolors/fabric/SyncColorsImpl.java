package org.gneisscode.improvedmapcolors.fabric;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.networking.ColorListSyncPayload;

import java.awt.*;

public class SyncColorsImpl {

    public static void syncColors(ServerLevel level){


        System.out.println("SYNCING COLORS!!!!!!");


        Color[] colors = ColorListManager.buildSyncList();

        ColorListSyncPayload payload = new ColorListSyncPayload(colors);
        for(ServerPlayer p : level.players())
            ServerPlayNetworking.send(p, payload);


    }

    public static void syncColors(ServerPlayer p){

        System.out.println("SYNCING COLORS!!!!!!");

        Color[] colors = ColorListManager.buildSyncList();

        ColorListSyncPayload payload = new ColorListSyncPayload(colors);

        ServerPlayNetworking.send(p, payload);
    }
}
