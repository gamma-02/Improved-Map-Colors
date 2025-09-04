package org.gneisscode.improvedmapcolors.neoforge;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import net.neoforged.fml.common.Mod;

@Mod(ImprovedMapColors.MOD_ID)
public final class ImprovedMapColorsNeoForge {

    public ImprovedMapColorsNeoForge(ModContainer container){
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);

        .loading(ImprovedMapColors.MOD_ID).register((l) -> {

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

    public ImprovedMapColorsNeoForge() {
        // Run our common setup.
        ImprovedMapColors.init();
    }
}
