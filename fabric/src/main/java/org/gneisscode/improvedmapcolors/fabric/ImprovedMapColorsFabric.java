package org.gneisscode.improvedmapcolors.fabric;

import com.mojang.logging.LogUtils;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.neoforged.fml.config.ModConfig;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import net.fabricmc.api.ModInitializer;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.MOD_ID;

public final class ImprovedMapColorsFabric implements ModInitializer {


    public ImprovedMapColorsFabric(){
        init();
    }

    public static void init(){
        ConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);
        
        //Config loading event is broken, moved loading to mod init

        fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents.reloading(MOD_ID).register((l) -> {

            CommonConfig.reloadConfig();

        });
    }

//    public static MapColorBlock MAP_COLOR_BLOCK =
//            Registry.register(
//                    BuiltInRegistries.BLOCK,
//                    BLOCK_KEY,
//                    new MapColorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARRIER).mapColor(MapColorBlock.getMapColorFunction()).setId(BLOCK_KEY)));




    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        ImprovedMapColors.init();

        if(CommonConfig.CONFIG_SPEC.isLoaded()){

            CommonConfig.reloadConfig();
            
        }


    }
}
