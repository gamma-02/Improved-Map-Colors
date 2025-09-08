package org.gneisscode.improvedmapcolors.neoforge;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;

@EventBusSubscriber(modid = ImprovedMapColors.MOD_ID)
public class ConfigEvents {

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading loading){
        LogUtils.getLogger().info("Loading Colors and Config!");

        CommonConfig.loadColorList();
        CommonConfig.loadBlockStateList();

    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading reloading){
        LogUtils.getLogger().info("Reloading Colors!");

        CommonConfig.loadColorList();
        CommonConfig.loadBlockStateList();

    }
}
