package org.gneisscode.improvedmapcolors.resourcestuff;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import org.gneisscode.improvedmapcolors.ColorStateMapManager;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateMapResourceReloadListener extends SimpleJsonResourceReloadListener<StateMapHolder> {

    public static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("mapcolors/states");
    public static final StateMapResourceReloadListener LISTENER = new StateMapResourceReloadListener();

    protected StateMapResourceReloadListener() {
        super(StateMapHolder.STATE_HOLDER_CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(
         Map<ResourceLocation, StateMapHolder> holderMap,
         ResourceManager resourceManager,
         ProfilerFiller profilerFiller
        ) {

        if(!CommonConfig.CONFIG.statesConfigMode.get().hasDatapack())
            return;

//        if(CommonConfig.CONFIG.statesConfigMode.get().hasModConfig())
//            ColorStateMapManager.setConfigStateTrackerMap(
//                    CommonConfig.loadBlockStateListFromConfig()
//            );

        profilerFiller.push("Map Color States Reload");

        HashMap<BlockState, ColorStateMapManager.BlockStatePropertyTracker> stateMap = new HashMap<>();

        holderMap.forEach((rsl, smh) ->
                smh.addTrackersToMap(stateMap)
        );

        profilerFiller.pop();

        LogUtils.getLogger().info("Reloaded map states!");

        ColorStateMapManager.setDatapackStateTrackerMap(stateMap);






    }
}
