package org.gneisscode.improvedmapcolors.fabric;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.fabric.api.client.networking.v1.C2SPlayChannelEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.neoforged.fml.config.ModConfig;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import net.fabricmc.api.ModInitializer;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.gneisscode.improvedmapcolors.networking.ColorListSyncPayload;
import org.gneisscode.improvedmapcolors.networking.PresetSyncS2CPacket;
import org.gneisscode.improvedmapcolors.networking.SelectPresetC2SPayload;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.MOD_ID;

public final class ImprovedMapColorsFabric implements ModInitializer {


    public ImprovedMapColorsFabric(){
        init();
    }

    public static void init(){
        ConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);
        PayloadTypeRegistry.playS2C().register(ColorListSyncPayload.ID, ColorListSyncPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(SelectPresetC2SPayload.ID, SelectPresetC2SPayload.PAYLOAD_CODEC);
        PayloadTypeRegistry.playS2C().register(PresetSyncS2CPacket.ID, PresetSyncS2CPacket.PAYLOAD_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(SelectPresetC2SPayload.ID, (payload, ctx) ->{
            PresetManager.handleSetPresetPacket(payload, ctx.server(), ctx.player());
        });
        //Config loading event is broken, moved loading to mod init

        fuzs.forgeconfigapiport.fabric.api.v5.ModConfigEvents.reloading(MOD_ID).register((l) -> {

            CommonConfig.reloadConfig();

        });

//        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, manager, success) -> {
//            server.getAllLevels().forEach(SyncColors::syncColors);
//        });


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
