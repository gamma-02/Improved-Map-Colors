package org.gneisscode.improvedmapcolors.neoforge;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.gneisscode.improvedmapcolors.resourcestuff.ColorListResourceReloadListener;
import org.gneisscode.improvedmapcolors.resourcestuff.StateMapResourceReloadListener;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
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

    @SubscribeEvent
    public static void reloadConfigListener(AddServerReloadListenersEvent reloadListenersEvent){
        reloadListenersEvent.addListener(ResourceLocation.fromNamespaceAndPath(MOD_ID, "color_list_reload_listener"), ColorListResourceReloadListener.LISTENER);
        reloadListenersEvent.addListener(ResourceLocation.fromNamespaceAndPath(MOD_ID, "state_map_reload_listener"), StateMapResourceReloadListener.LISTENER);
    }
}
