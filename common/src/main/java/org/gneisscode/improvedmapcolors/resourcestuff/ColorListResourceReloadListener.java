package org.gneisscode.improvedmapcolors.resourcestuff;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.material.MapColor;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        @NotNull
        List<@Nullable Color> colorList = new ArrayList<>(MapColor.MATERIAL_COLORS.length);

        for(int i = 0; i < MapColor.MATERIAL_COLORS.length; i++){
            colorList.add(null);
        }

        profilerFiller.push("Map Color List Colors Reload");

        holderMap.forEach((rl, holder) -> {
            holder.addColorsToList(colorList);
        });

        profilerFiller.pop();

        LogUtils.getLogger().info("Reloaded map colors!");

        ColorListManager.setDatapackColorList(colorList);
    }
}
