package org.gneisscode.improvedmapcolors.resourcestuff;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.MapColor;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class ColorListResourceReloadListener extends SimpleJsonResourceReloadListener<ColorListHolder> {

    public static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("mapcolors/colors");
    public static final ColorListResourceReloadListener LISTENER = new ColorListResourceReloadListener();


    protected ColorListResourceReloadListener() {
        super(ColorListHolder.COLOR_LIST_CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(
            Map<ResourceLocation, ColorListHolder> holderMap,
            ResourceManager resourceManager,
            ProfilerFiller profilerFiller
        ) {

        //don't do anything if the feature is off
        if(!CommonConfig.CONFIG.colorConfigMode.get().hasDatapack()) return;

        List<Color> colorList = new ArrayList<>(64);
        profilerFiller.push("Map Color List Colors Reload");
        holderMap.forEach((rl, holder) -> {
            holder.addColorsToList(colorList);
        });
        profilerFiller.pop();
        LogUtils.getLogger().info("Reloaded map colors!");

        for(int i = 0; i < 64; i++){
            Color datapackColor = colorList.get(i);
            Color configColor = ImprovedMapColors.indexIdColorList.get(i);
            MapColor c = MapColor.MATERIAL_COLORS[i];

            if(configColor != null && datapackColor != null &&
                    CommonConfig.CONFIG.colorConfigMode.get().hasModConfig()

            ){

                c.col = configColor.getRGB();
            }else if(configColor == null && datapackColor != null
                    && CommonConfig.CONFIG.colorConfigMode.get().hasModConfig()
            ){
                c.col = datapackColor.getRGB();
            }


        }
    }
}
