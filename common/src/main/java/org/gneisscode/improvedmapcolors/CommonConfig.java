package org.gneisscode.improvedmapcolors;

import com.electronwill.nightconfig.core.EnumGetMethod;
import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class CommonConfig /*extends MidnightConfig*/ {

    public static final CommonConfig CONFIG;
    public static final ModConfigSpec CONFIG_SPEC;

    private CommonConfig(ModConfigSpec.Builder builder){
//        builder.push("General");

        configColorList = builder
                .comment("List of colors to replace vanilla map colors, defaulting to Vanilla colors (gneiss colors)")
                .defineList("colorlist", defaultColorList, String::new, CommonConfig::validateColor);

//        useFile = builder.comment("Use a list of colors from the CSV defined below").define("use_file", false);

        colorConfigMode = builder.comment("What mode to use when loading color values")
                .comment("Config values will override a datapack, and CSV values will override both datapacks and the config")
                .defineEnum("colorConfigMode",
                ConfigMode.CONFIG_DATAPACK,
                EnumGetMethod.NAME_IGNORECASE,
                Lists.newArrayList(
                        ConfigMode.CONFIG_DATAPACK,
                        ConfigMode.ALL,
                        ConfigMode.CONFIG,
                        ConfigMode.DATAPACK,
                        ConfigMode.CSV_FILE
                        ));

        colorCsvPath = builder
                .comment("Path to a CSV list of colors like: |hex color|[optional id]|")
                .comment("If no ID is supplied, the id will be the column number in the file")
                .define("color_file", "", CommonConfig::fileValidator);

        configBlockStateList = builder
                .comment("List of BlockStates:ColorID to change from Vanilla BlockStates (will default to Gneiss states)")
                .defineList("state_list", defaultStateList, String::new, CommonConfig::validateBlockStateListEntry);

//        useBlockStateFile = builder
//                .comment("Use a list of BlockStates and colorIDs from the CSV defined below")
//                .define("use_blockstate_file", false);

        statesConfigMode = builder.comment("What mode to use when loading BlockState values")
                .comment("On conflicts, config values will override datapacks', and CSV values will override both datapacks and the config")
                .defineEnum("statesConfigMode",
                        ConfigMode.CONFIG_DATAPACK,
                        EnumGetMethod.NAME_IGNORECASE,
                        Lists.newArrayList(
                                ConfigMode.CONFIG_DATAPACK,
                                ConfigMode.ALL,
                                ConfigMode.CONFIG,
                                ConfigMode.DATAPACK,
                                ConfigMode.CSV_FILE
                        ));


        blockStateCsvPath = builder
                .comment("Path to a CSV list of states like:")
                .comment("|BlockID[statedata]|colorID<required>| OR:")
                .comment("|BlockID|[statedata]|colorID<required>|")
                .define("blockstate_file", "", CommonConfig::fileValidator);




    }

    //colors!
    public final ModConfigSpec.ConfigValue<List<? extends String>> configColorList;
//    public final ModConfigSpec.BooleanValue useFile;
    public final ModConfigSpec.EnumValue<ConfigMode> colorConfigMode;
    public final ModConfigSpec.ConfigValue<String> colorCsvPath;

    //block states!
    public final ModConfigSpec.ConfigValue<List<? extends String>> configBlockStateList;
//    public final ModConfigSpec.BooleanValue useBlockStateFile;
    public final ModConfigSpec.EnumValue<ConfigMode> statesConfigMode;
    public final ModConfigSpec.ConfigValue<String> blockStateCsvPath;

    //todo: add a few examples and tests
    public static final ArrayList<String> defaultStateList = Lists.newArrayList(
            "minecraft:poppy:28",
            "allium:16",
            "azure_bluet:2",
            "blue_orchid:17",
            "dandelion:18",
            "lily_of_the_valley:8",
            "oxeye_daisy:14",
            "torchflower:40",
            "orange_tulip:15",
            "red_tulip:52",
            "white_tulip:22",
            "pink_tulip:16",
            "wither_rose:21"
    );

    //todo: move out of the way asap
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
        //todo: add JSON compat -> get ready for allowing datapacks
        return f.exists() && f.isFile() && (s.endsWith(".csv") || s.endsWith(".json"));
    }

    public static boolean validateBlockStateListEntry(Object o){
        if (!(o instanceof String s)) {
            return false;
        }

        if (!s.contains(":")) {
            return false;
        }
//
//        String[] split = s.split(":");
//
//        if(split.length < 2){
//            return false;
//        }
//
//        try{
//            Integer.parseInt(split[split.length - 1]);
//        }catch(NumberFormatException e){
//            return false;
//        }
//
//        if(split.length < 3){
//            String id = split[0].split("\\[")[0];
//            return BuiltInRegistries.BLOCK.containsKey(ResourceLocation.withDefaultNamespace(id));
//        }else{
//            return BuiltInRegistries.BLOCK.containsKey(ResourceLocation.fromNamespaceAndPath(split[0], split[1].split("\\[")[0]));
//        }
        return true;

    }


    public static void initIndexIdColorList() {
        if(CommonConfig.CONFIG.colorConfigMode.get().hasCSV() && !CommonConfig.CONFIG.statesConfigMode.get().hasModConfig()){
            //todo: handle using the file and committing that to the list
            ImprovedMapColors.indexIdColorList = new ArrayList<>();

            loadColorListCSV();
            return;
        }else{
            ImprovedMapColors.indexIdColorList = CommonConfig.CONFIG.configColorList.get().stream().map(Color::decode).toList();
        }

        if(
                CommonConfig.CONFIG.statesConfigMode.get().hasCSV() &&
                CommonConfig.CONFIG.statesConfigMode.get().hasModConfig()
        ){
            loadColorListCSV();
        }


    }

    public static void loadColorListCSV(){
        Path p = null;
        try {
            p = Paths.get(CONFIG.colorCsvPath.get());
        } catch (InvalidPathException | NullPointerException ex) {
            return;
        }

        File f = p.toFile();

        Reader reader = null;
        try {
            reader = Files.newBufferedReader(p);
        } catch (IOException e) {
            LogUtils.getLogger().error("CSV could not be found!");
            return;
        }

        CSVReader csvReader = new CSVReader(reader);

        List<String[]> entries = null;
        try {
            entries = csvReader.readAll();
        } catch (IOException | CsvException e) {
            LogUtils.getLogger().error("CSV Could not be read!");
            throw new RuntimeException(e);
        }


        for(int i = 0; i < entries.size(); i++){
            String[] entry = entries.get(i);

            Color c = null;

            try{
                c = Color.decode(entry[0]);
            }catch( NumberFormatException nfe){
                continue;
            }

            if(entry.length == 1){
                ImprovedMapColors.indexIdColorList.set(i, c);
            }else{
                try{
                    ImprovedMapColors.indexIdColorList.set(Integer.parseInt(entry[1]), c);
                }catch (NumberFormatException nfe){
                    continue;
                }
            }
        }
    }


    public static void loadColorList() {
        CommonConfig.initIndexIdColorList();

        for(MapColor c : MapColor.MATERIAL_COLORS){
            if(c == null) continue;
            c.col = ImprovedMapColors.indexIdColorList.get(c.id).getRGB();
        }
    }

    public static void loadBlockStateList() {
        if(CommonConfig.CONFIG.statesConfigMode.get().hasCSV() && !CONFIG.statesConfigMode.get().hasModConfig()){

            ImprovedMapColors.valueStateMap = new HashMap<>();
            ImprovedMapColors.blockStateIdMap = new HashMap<>();

            parseBlockStateDataCSV();

            return;
        }





        HashMap<BlockState, Integer> stateColorMap = new HashMap<>();
        HashMap<BlockState, List<String>> statePropertyStringMap = new HashMap<>();

        for(String s : CommonConfig.CONFIG.configBlockStateList.get()){

            if(s.isEmpty()) continue;

            String[] split = s.split(":");

            if(split.length < 2){
                continue;
            }

            boolean hasNamespace = split.length == 3;

            int colorID = hasNamespace
                    ? Integer.parseInt(split[2])
                    : Integer.parseInt(split[1]);

            String[] pathAndState = (hasNamespace ? split[1] : split[0]).split("\\[");
            String blockStateData = pathAndState.length < 2 ? null : pathAndState[1].replace("]", "");
            String path = pathAndState[0];

            ArrayList<String> stateStrings = new ArrayList<>();

            BlockState state = parseBlockStateData(blockStateData, path, hasNamespace ? split[0] : null, stateStrings);

            if(state.getBlock() == Blocks.AIR && !path.contains("air")){
                continue;
            }

            stateColorMap.put(state, colorID);
            statePropertyStringMap.put(state, stateStrings);

        }


        ImprovedMapColors.valueStateMap = statePropertyStringMap;
        ImprovedMapColors.blockStateIdMap = stateColorMap;

        if(CONFIG.statesConfigMode.get().hasCSV() && CONFIG.statesConfigMode.get().hasModConfig()){
            parseBlockStateDataCSV();
        }
    }

    private static void parseBlockStateDataCSV() {

        Path p = null;
        try {
            p = Paths.get(CONFIG.blockStateCsvPath.get());
        } catch (InvalidPathException | NullPointerException ex) {
            return;
        }

        File f = p.toFile();

        Reader reader = null;
        try {
            reader = Files.newBufferedReader(p);
        } catch (IOException e) {
            LogUtils.getLogger().error("CSV Path could not be found!");
            return;
        }

        CSVReader csvReader = new CSVReader(reader);

        List<String[]> entries = null;
        try {
            entries = csvReader.readAll();
        } catch (IOException | CsvException e) {
            LogUtils.getLogger().error("CSV Could not be read!");
            throw new RuntimeException(e);
        }

        HashMap<BlockState, Integer> stateColorMap = ImprovedMapColors.blockStateIdMap;
        HashMap<BlockState, List<String>> statePropertyStringMap = ImprovedMapColors.valueStateMap;



        for(String[] entry : entries){
            var id = entry[0];
            //has namespace
            String path = null;
            String namespace = null;
            if(id.contains(":")){
                namespace = id.split(":")[0];
                path = id.split(":")[1];
            }else{
                path = id;
            }

            //state data baked in to the id
            String blockStateData = null;
            if(entry.length == 2){
                String[] pathAndState = (path).split("\\[");
                blockStateData = pathAndState.length < 2 ? null : pathAndState[1].replace("]", "");
                path = pathAndState[0];

                //state data seperate from the id
            }else if(entry.length == 3){

                blockStateData = entry[1].replace("[", "").replace("]", "");

            }else{//malformed. do not process
                continue;
            }

            var stateStrings = new ArrayList<String>();

            BlockState state = parseBlockStateData(blockStateData, path, namespace, stateStrings);

            if(state.getBlock() == Blocks.AIR && !path.contains("air")){
                continue;
            }

            int colorID;
            try{
                colorID = Integer.parseInt(entry.length == 2 ? entry[1] : entry[2]);
            } catch (NumberFormatException e) {
                LogUtils.getLogger().error("colorID in state CSV Malformed!");
                continue;
            }

            stateColorMap.put(state, colorID);
            statePropertyStringMap.put(state, stateStrings);

        }

        ImprovedMapColors.valueStateMap = statePropertyStringMap;
        ImprovedMapColors.blockStateIdMap = stateColorMap;



    }

    @NotNull
    public static BlockState parseBlockStateData(@Nullable String blockStateData, String path, @Nullable String namespace, ArrayList<String> stateStrings){
        boolean hasNamespace = namespace != null;

        ResourceLocation ownerID = hasNamespace
                ? ResourceLocation.fromNamespaceAndPath(namespace, path)
                : ResourceLocation.withDefaultNamespace(path);

        Block owner = BuiltInRegistries.BLOCK.getValue(ownerID);

        if(owner == Blocks.AIR || blockStateData == null){
            return owner.defaultBlockState();
        }



        String[] states = blockStateData.split(",");

        BlockState defaultState = owner.defaultBlockState();



        for(String state : states){
            String name = String.valueOf(state.subSequence(0, state.indexOf("="))).replace("=", "");
            String value = String.valueOf(state.substring(state.indexOf("="))).replace("=", "");

            Property<?> prop = owner.getStateDefinition().getProperty(name);

            if(prop == null){
                continue;
            }

            defaultState = setProperty(defaultState, prop, value);
            stateStrings.add(name);//add this name as something to override the default blockstate

        }

        return defaultState;


    }

    public static <T extends Comparable<T>> BlockState setProperty(BlockState state, Property<T> property, String value){
        Optional<T> val = property.getValue(value);
        return val.map(t -> state.setValue(property, t)).orElse(state);
    }


    /**
     * bit structure:<br>
     * 0b <br>
     * x config lists (GUI)<br>
     * x datapack<br>
     * x CSV file<br>
     * * empty<br>
     * *<br>
     * *<br>
     * *<br>
     * *
     */
    public static enum ConfigMode{
        CONFIG(1), //2
        DATAPACK(2), //3
        CSV_FILE(4), //4
        CONFIG_DATAPACK(3), // default
        CONFIG_CSV(5),
        CSV_DATAPACK(6),//why
        ALL(7); //1

        public final byte mode;
        ConfigMode(int fileFlag){
            mode = (byte) fileFlag;
        }

        public boolean hasCSV(){
            return (this.mode & 4) == 4;
        }

        public boolean hasModConfig(){
            return (this.mode & 1) == 1;
        }

        public boolean hasDatapack(){
            return (this.mode & 2) == 2;
        }
    }

}
