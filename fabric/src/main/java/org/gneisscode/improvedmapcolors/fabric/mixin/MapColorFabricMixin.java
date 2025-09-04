package org.gneisscode.improvedmapcolors.fabric.mixin;

import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.config.ConfigTracker;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.fabric.ImprovedMapColorsFabric;
import org.gneisscode.improvedmapcolors.fabric.compat.ConfigTrackerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.logging.Logger;

@Mixin(MapColor.class)
public class MapColorFabricMixin {

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void loadedNow(CallbackInfo ci){
        LogUtils.getLogger().info("Fabric Map Color clinit!");
//        ImprovedMapColorsFabric.init();
//        if(!CommonConfig.CONFIG_SPEC.isLoaded()){
//            ((ConfigTrackerAccessor)ConfigTracker.INSTANCE).openConfigAccessor(((ConfigTrackerAccessor)ConfigTracker.INSTANCE).getConfig("improvedmapcolors"), FabricLoader.getInstance().getConfigDir(), null);
//        }
//        CommonConfig.initIndexIdColorList();

    }
}
