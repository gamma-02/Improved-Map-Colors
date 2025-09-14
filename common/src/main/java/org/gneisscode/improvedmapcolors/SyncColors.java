package org.gneisscode.improvedmapcolors;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class SyncColors {


    @ExpectPlatform
    public static void syncColors(ServerLevel level){
        throw new AssertionError("Not implemented!");
    }

    @ExpectPlatform
    public static void syncColors(ServerPlayer p){
        throw new AssertionError("Not implemented!");

    }

    public static void reloadingResources(MinecraftServer reloading){
        reloading.getAllLevels().forEach(SyncColors::syncColors);
    }
}
