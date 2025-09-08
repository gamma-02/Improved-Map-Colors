package org.gneisscode.improvedmapcolors.fabric;

import com.mojang.logging.LogUtils;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.config.ModConfig;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import net.fabricmc.api.ModInitializer;

import java.awt.*;
import java.util.ArrayList;

public final class ImprovedMapColorsFabric implements ModInitializer {


    public ImprovedMapColorsFabric(){
        init();
    }

    public static void init(){
        ConfigRegistry.INSTANCE.register(ImprovedMapColors.MOD_ID, ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);
        
        //Config loading event is broken, moved loading to mod init

        fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents.reloading(ImprovedMapColors.MOD_ID).register((l) -> {

            LogUtils.getLogger().info("RELOADING CONFIG");

            CommonConfig.loadColorList();

            CommonConfig.loadBlockStateList();

        });
    }




    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ImprovedMapColors.init();

        if(CommonConfig.CONFIG_SPEC.isLoaded()){
            LogUtils.getLogger().info("Loading Colors and Config!");
            
            CommonConfig.loadColorList();

            CommonConfig.loadBlockStateList();
            
        }


    }
}
