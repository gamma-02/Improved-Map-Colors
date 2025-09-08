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

        ImprovedMapColors.init();

    }
}
