package org.gneisscode.improvedmapcolors;

import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class ColorListManager {
    @Nullable
    private static List<@Nullable Color>
            configColorList = new ArrayList<>(),
            datapackColorList = new ArrayList<>(),
            overridingColorList = new ArrayList<>();

    public static final Supplier<Boolean>
            runConfigColors = () -> (
                    CommonConfig.CONFIG.colorConfigMode.get().hasModConfig() &&
                    configColorList != null &&
                    configColorList.size() == MapColor.MATERIAL_COLORS.length
            ),
            runDatapackColors = () -> (
                    CommonConfig.CONFIG.colorConfigMode.get().hasDatapack() &&
                    datapackColorList != null &&
                    datapackColorList.size() == MapColor.MATERIAL_COLORS.length
            ),
            runOverrideColors = () -> (
                    CommonConfig.CONFIG.colorConfigMode.get().hasCSV() &&
                    overridingColorList != null &&
                    overridingColorList.size() == MapColor.MATERIAL_COLORS.length
            );

    public static @Nullable List<@Nullable Color> getConfigColorList() {
        return configColorList;
    }
    public static @Nullable List<@Nullable Color> configColorList(){
        return configColorList;
    }

    public static void setConfigColorList(@Nullable List<@Nullable Color> configColorList) {

        ColorListManager.configColorList = configColorList;

        reloadMapColorList();

    }

    public static @Nullable List<@Nullable Color> getDatapackColorList() {
        return datapackColorList;
    }

    public static @Nullable List<@Nullable Color> datapackColorList() {
        return datapackColorList;
    }

    public static void setDatapackColorList(@Nullable List<@Nullable Color> datapackColorList) {

        ColorListManager.datapackColorList = datapackColorList;

        reloadMapColorList();

    }

    public static @Nullable List<@Nullable Color> getOverridingColorList() {
        return overridingColorList;
    }
    public static @Nullable List<@Nullable Color> overridingColorList() {
        return overridingColorList;
    }

    public static void setOverridingColorList(@Nullable List<@Nullable Color> overridingColorList) {

        ColorListManager.overridingColorList = overridingColorList;

        reloadMapColorList();

    }


    
    
    
    private static void reloadMapColorList(){
        //checking conditions

        if(!CommonConfig.CONFIG.statesConfigMode.get().hasCSV()){
            overridingColorList = new ArrayList<>();
        }

        if(!CommonConfig.CONFIG.statesConfigMode.get().hasDatapack()){
            datapackColorList = new ArrayList<>();
        }

        if(!CommonConfig.CONFIG.statesConfigMode.get().hasModConfig()){
            configColorList = new ArrayList<>();
        }

        
        for(int i = 0; i < MapColor.MATERIAL_COLORS.length; i++){
            MapColor c = MapColor.MATERIAL_COLORS[i];
            
            if(c == null) c = new MapColor(i, 0);

            if(runOverrideColors.get()){
                if(setMapColorFrom(c, Objects.requireNonNull(overridingColorList)))
                    continue;
            }

            if(runDatapackColors.get()){
                if(setMapColorFrom(c, Objects.requireNonNull(datapackColorList)))
                    continue;
            }

            if(runConfigColors.get()){
                setMapColorFrom(c, Objects.requireNonNull(configColorList));
                continue;//
            }
            



        }

    }

    private static boolean setMapColorFrom(MapColor c, List<Color> referenceList) {
        Color pc = referenceList.get(c.id);

        if(pc != null){
            c.col = pc.getRGB();
            return true;
        }

        return false;

    }

//    private enum ReloadLevel{
//        ALL,
//        DATAPACK,
//        CONFIG;
//
//        ReloadLevel()
//    }
}
