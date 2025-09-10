package org.gneisscode.improvedmapcolors.resourcestuff;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;

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

        profilerFiller.push("Map Color States Reload");
        profilerFiller.push("Phase One: states");
        HashMap<BlockState, Integer> map = new HashMap<>();
        holderMap.forEach((rl, holder) -> holder.addStatesToMap(map));
        profilerFiller.popPush("Phase Two: tracked states");
        HashMap<BlockState, List<String>> trackedMap = new HashMap<>();
        holderMap.forEach((rl, sh) -> sh.addTrackedPropertiesToMap(trackedMap));
        profilerFiller.pop();
        profilerFiller.pop();
        LogUtils.getLogger().info("Reloaded map states!");




    }
}
