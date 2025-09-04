package org.gneisscode.improvedmapcolors;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CommonConfig extends MidnightConfig {

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
            "#ff0000",
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

    @Comment(category = "colors") public static Comment color_list_comment;
    @Entry(category = "colors", isColor = true, name = "New Color List") public static List<String> ColorList = new ArrayList<>(defaultColorList);
    public static List<Color> CorrectTypeColorList = ColorList.stream().map(Color::decode).toList();

    @Comment(category = "colors") public static Comment color_file_comment;
    @Entry(category = "colors",
            selectionMode = JFileChooser.FILES_ONLY,
            fileExtensions = {"csv"}, // Define valid file extensions
            fileChooserType = JFileChooser.SAVE_DIALOG,
            name = "Color List File")
    public static String ColorFile = "";

    @Comment(category = "colors") public static Comment use_color_file_comment;
    @Entry(category = "colors", name = "Use File") public static boolean UseColorFile = false;

    @Entry(category = "lists", name = "I am a string list!") public static List<String> stringList = Lists.newArrayList("String1", "String2"); // Array String Lists are also supported
    @Entry(category = "lists", isColor = true, name = "I am a color list!") public static List<String> colorList = Lists.newArrayList("#ac5f99", "#11aa44"); // Lists also support colors


    //block states

//    @Entry(category = "blocks")




}
