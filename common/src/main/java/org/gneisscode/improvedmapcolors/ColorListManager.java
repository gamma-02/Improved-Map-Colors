package org.gneisscode.improvedmapcolors;

import net.minecraft.world.level.material.MapColor;
import org.gneisscode.improvedmapcolors.networking.ColorListSyncPayload;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.*;
import java.util.function.Supplier;

public class ColorListManager {


    @Nullable
    private static List<@Nullable Color>
            configColorList = new ArrayList<>(),
            datapackColorList = new ArrayList<>(),
            overridingColorList = new ArrayList<>();

    @Nullable
    private static Color[] serverColors = null;



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

    public static void setOverridingColorList(@Nullable List<@Nullable Color> please) {

        ColorListManager.overridingColorList = please != null
                ? new ArrayList<>(please)
                : new ArrayList<>();

        reloadMapColorList();

    }

    public static void onDisconnect(){
        serverColors = null;
        reloadMapColorList();
    }

    public static void handleSyncPayload(ColorListSyncPayload payload){

//        System.out.println("sync payload recieved");
       serverColors = Arrays.copyOf(payload.colors(), payload.colors().length);
       reloadMapColorList();
    }

    /*
     Divide + Conquer
      L Divide and Conquer and Merge
     1. Take input of size n
     2. divide X "down the middle"
     3. Assume correct solutions for left and right for free/magic
     4. Turn solutions for L&R into solution for X
     */



    
    
    
    private static void reloadMapColorList(){





        //checking conditions

        ensureListsExist();


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
//                continue;//
            }
            



        }

        //moved down here so that the other config options will serve as a backup or fallback
        if(serverColors != null){
            for (int i = 0; i < serverColors.length; i++) {
                Color serverColor = serverColors[i];
                if (serverColor != null && serverColor.getRGB() != -1){
                    MapColor.MATERIAL_COLORS[i].col = serverColor.getRGB();
                }
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

    public static Color[] buildSyncList() {

        Color[] list = new Color[MapColor.MATERIAL_COLORS.length];

        ensureListsExist();


        for(int i = 0; i < list.length; i++){

            if(runConfigColors.get()){
                list[i] = Objects.requireNonNull(configColorList).get(i);
            }

            if(runDatapackColors.get()){
                list[i] = Objects.requireNonNull(datapackColorList).get(i);
            }

            if(runOverrideColors.get()){
                list[i] = Objects.requireNonNull(overridingColorList).get(i);
            }




        }

        return list;
    }

    public static void ensureListsExist() {
        if(!CommonConfig.CONFIG.colorConfigMode.get().hasCSV()){
            overridingColorList = new ArrayList<>();
        }

        if(!CommonConfig.CONFIG.colorConfigMode.get().hasDatapack()){
            datapackColorList = new ArrayList<>();
        }

        if(!CommonConfig.CONFIG.colorConfigMode.get().hasModConfig()){
            configColorList = new ArrayList<>();
        }
    }

//    private enum ReloadLevel{
//        ALL,
//        DATAPACK,
//        CONFIG;
//
//        ReloadLevel()
//    }
}
