package org.gneisscode.improvedmapcolors;

import com.google.common.collect.Lists;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommonConfig /*extends MidnightConfig*/ {

    public static final CommonConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private CommonConfig(ModConfigSpec.Builder builder){
//        builder.push("General");

        configColorList = builder
                .comment("List of colors to replace vanilla map colors, defaulting to Vanilla colors (gneiss colors)")
                .defineList("colorlist", defaultColorList, () -> new String(), CommonConfig::validateColor);

        useFile = builder.comment("Use a list of colors from the CSV defined below").define("use_file", false);

        colorCsvPath = builder
                .comment("Path to a CSV list of files like: |hex color|[optional id]|")
                .comment("If no ID is supplied, the id will be the column number in the file")
                .define("color_file", "", CommonConfig::fileValidator);







    }

    public final ModConfigSpec.ConfigValue<List<? extends String>> configColorList;
    public final ModConfigSpec.BooleanValue useFile;
    public final ModConfigSpec.ConfigValue<String> colorCsvPath;
    public List<Color> indexIdColorList;


    public static final ArrayList<String> defaultColorList = Lists.newArrayList(
            "#000000",
            "#7fb238",
            "#f7e9a3",
            "#c7c7c7",
            "#ff0000",
            "#a0a0ff",
            "#a7a7a7",
            "#007c00",
            "#ffffff",
            "#a4a8b8",
            "#976d4d",
            "#707070",
            "#4040ff",
            "#8f7748",
            "#fffcf5",
            "#d87f33",
            "#b24cd8",
            "#6699d8",
            "#e5e533",
            "#7fcc19",
            "#f27fa5",
            "#4c4c4c",
            "#999999",
            "#4c7f99",
            "#7f3fb2",
            "#334cb2",
            "#664c33",
            "#667f33",
            "#993333",
            "#191919",
            "#faee4d",
            "#5cdbd5",
            "#4a80ff",
            "#00d93a",
            "#815631",
            "#700200",
            "#d1b1a1",
            "#9f5224",
            "#95576c",
            "#706c8a",
            "#ba8524",
            "#677535",
            "#a04d4e",
            "#392923",
            "#876b62",
            "#575c5c",
            "#7a4958",
            "#4c3e5c",
            "#4c3223",
            "#4c522a",
            "#8e3c2e",
            "#251610",
            "#bd3031",
            "#943f61",
            "#5c191d",
            "#167e86",
            "#3a8e8c",
            "#562c3e",
            "#14b485",
            "#646464",
            "#d8af93",
            "#7fa796"
    );


    static{
        Pair<CommonConfig, ModConfigSpec> configPair =
                new ModConfigSpec.Builder().configure(CommonConfig::new);

        CONFIG = configPair.getLeft();
        CONFIG_SPEC = configPair.getRight();


    }

    public static boolean validateColor(Object o){
        if(!(o instanceof String s)){
            return false;
        }
        try{
            Color c = Color.decode(s);
        }catch (NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public static boolean fileValidator(Object o){
        if(!(o instanceof String s)){
            return false;
        }

        if(s.isEmpty()){
            return true;
        }

        Path p = null;
        try {
            p = Paths.get(s);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }

        File f = p.toFile();

        return f.exists() && f.isFile() && s.endsWith(".csv");
    }


    public static void initIndexIdColorList() {
        if(CommonConfig.CONFIG.useFile.get()){
            //handle using the file and committing that to the typeCorrect
            CommonConfig.CONFIG.indexIdColorList = new ArrayList<>();
        }else{
            CommonConfig.CONFIG.indexIdColorList = CommonConfig.CONFIG.configColorList.get().stream().map(Color::decode).toList();
        }


    }




}
