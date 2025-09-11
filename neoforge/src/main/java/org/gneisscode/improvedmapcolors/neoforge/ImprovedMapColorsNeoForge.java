package org.gneisscode.improvedmapcolors.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import net.neoforged.fml.common.Mod;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.MOD_ID;

@Mod(MOD_ID)
public final class ImprovedMapColorsNeoForge {



    public ImprovedMapColorsNeoForge(ModContainer container){

        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);

        ImprovedMapColors.init();

    }
}
