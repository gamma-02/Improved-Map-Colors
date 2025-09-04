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
        fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents.loading(ImprovedMapColors.MOD_ID).register((l) -> {

            LogUtils.getLogger().info("LOADING CONFIG");

            CommonConfig.initIndexIdColorList();

            for(MapColor c : MapColor.MATERIAL_COLORS){
                c.col = CommonConfig.CONFIG.indexIdColorList.get(c.id).getRGB();
            }

        });

        fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents.reloading(ImprovedMapColors.MOD_ID).register((l) -> {

            LogUtils.getLogger().info("RELOADING CONFIG");

            CommonConfig.initIndexIdColorList();

            for(MapColor c : MapColor.MATERIAL_COLORS){
                if(c == null)
                    continue;
                c.col = CommonConfig.CONFIG.indexIdColorList.get(c.id).getRGB();
            }
        });
    }



    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ImprovedMapColors.init();


    }
}
