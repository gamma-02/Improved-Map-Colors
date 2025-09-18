package org.gneisscode.improvedmapcolors.presets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.world.level.block.state.BlockState;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.ColorStateMapManager;
import org.gneisscode.improvedmapcolors.resourcestuff.ColorListHolder;
import org.gneisscode.improvedmapcolors.resourcestuff.StateMapHolder;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class PresetDatapackExportTool {

    public static final Gson json = new GsonBuilder().setPrettyPrinting().create();

    /**
     *
     * @param outFile
     * @param packName id a valid Minecraft datapack namespace, only [a-z0-9/._-]
     * @throws IOException
     */
    public static void exportDataPackPresetFromCSV(File outFile, @Nullable String packName) throws IOException {



        try(ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFile))){
            List<Color> csvColorList = ColorListManager.overridingColorList();

            if(csvColorList != null) {
                csvColorList = csvColorList.stream().toList();
                ColorListHolder holder = new ColorListHolder(csvColorList);

                var colorListResult = ColorListHolder.COLOR_LIST_CODEC.encode(holder, JsonOps.INSTANCE, new JsonObject());

                JsonElement colorList = colorListResult.getOrThrow();

                ZipEntry colors = new ZipEntry("data/" + packName + "/mapcolors/colors/preset_color_list.json");

                out.putNextEntry(colors);

                String outText = json.toJson(colorList);

                byte[] data = outText.getBytes(StandardCharsets.UTF_8);

                out.write(data, 0, data.length);
                out.closeEntry();

            }

            var stateColorMap = ColorStateMapManager.getOverridingStateTrackerMap();

            if(stateColorMap != null){

                StateMapHolder holder = new StateMapHolder(stateColorMap.values().stream().toList());

                var stateListResult = StateMapHolder.STATE_HOLDER_CODEC.encode(holder, JsonOps.INSTANCE, new JsonObject());

                JsonElement stateList = stateListResult.getOrThrow();

                ZipEntry states = new ZipEntry("data/" + packName + "/mapcolors/states/preset_state_list.json");

                out.putNextEntry(states);

                String outText = json.toJson(stateList);

                byte[] data = outText.getBytes(StandardCharsets.UTF_8);

                out.write(data, 0, data.length);
                out.closeEntry();

            }

            ZipEntry pack_mcmeta = new ZipEntry("pack.mcmeta");
            out.putNextEntry(pack_mcmeta);

            JsonObject root = new JsonObject();

            JsonObject pack = new JsonObject();

            pack.addProperty("pack_format", 81);

            JsonObject description = new JsonObject();

            description.addProperty("text", packName + " preset for map colors");

            pack.add("description", description);

            root.add("pack", pack);

            String packMcmetaString = json.toJson(root);

            byte[] packMcmetaData = packMcmetaString.getBytes(StandardCharsets.UTF_8);

            out.write(packMcmetaData, 0, packMcmetaData.length);
            out.closeEntry();

            out.close();


        }


    }

}
