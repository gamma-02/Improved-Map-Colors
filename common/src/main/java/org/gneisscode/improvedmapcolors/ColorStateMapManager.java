package org.gneisscode.improvedmapcolors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class ColorStateMapManager {

    //Map instanceof HashMap please !!!
    @Nullable
    private static Map<BlockState, @NotNull BlockStatePropertyTracker>
            configStateTrackerMap = new HashMap<>(),
            datapackStateTrackerMap = new HashMap<>(),
            overridingStateTrackerMap = new HashMap<>();

    public static @Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> getConfigStateTrackerMap() {
        return configStateTrackerMap;
    }
    public static @Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> configStateTrackerMap() {
        return configStateTrackerMap;
    }

    public static void setConfigStateTrackerMap(@Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> configStateTrackerMap) {
        ColorStateMapManager.configStateTrackerMap = configStateTrackerMap;
    }

    public static @Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> getDatapackStateTrackerMap() {
        return datapackStateTrackerMap;
    }
    public static @Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> datapackStateTrackerMap() {
        return datapackStateTrackerMap;
    }

    public static void setDatapackStateTrackerMap(@Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> datapackStateTrackerMap) {
        ColorStateMapManager.datapackStateTrackerMap = datapackStateTrackerMap;
    }

    public static @Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> getOverridingStateTrackerMap() {
        return overridingStateTrackerMap;
    }
    public static @Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> overridingStateTrackerMap() {
        return overridingStateTrackerMap;
    }

    public static void setOverridingStateTrackerMap(@Nullable Map<BlockState, @NotNull BlockStatePropertyTracker> overridingStateTrackerMap) {
        ColorStateMapManager.overridingStateTrackerMap = overridingStateTrackerMap;
    }

    public static final Supplier<Boolean>
            runConfigColors = () -> (
                    CommonConfig.CONFIG.statesConfigMode.get().hasModConfig() &&
                    configStateTrackerMap != null
            ),
            runDatapackColors = () -> (
                    CommonConfig.CONFIG.statesConfigMode.get().hasDatapack() &&
                    datapackStateTrackerMap != null
            ),
            runOverrideColors = () -> (
                    CommonConfig.CONFIG.statesConfigMode.get().hasCSV() &&
                    overridingStateTrackerMap != null
            );

    public static MapColor getMapColorFromBlockState(BlockState instance, MapColor m) {

        int color = getColorIDFromState(instance);
        if(color != -1){
            m = MapColor.MATERIAL_COLORS[color];
        }
        return m;
    }
    
    public static void unloadStateColors(){
        if(!CommonConfig.CONFIG.statesConfigMode.get().hasCSV()){
            overridingStateTrackerMap = new HashMap<>();
        }

        if(!CommonConfig.CONFIG.statesConfigMode.get().hasDatapack()){
            datapackStateTrackerMap = new HashMap<>();
        }

        if(!CommonConfig.CONFIG.statesConfigMode.get().hasModConfig()){
            configStateTrackerMap = new HashMap<>();
        }
    }

    /**
     *
     * @param instance instance of BlockState to check against the maps
     * @return the colorID from the map hierarchy from the instance, or -1 iff the instance is in none of the maps or none of the maps are enabled.
     */
    public static int getColorIDFromState(BlockState instance){

        int tempColorID = -1;

        

        if(
                runOverrideColors.get()
            ){
            tempColorID = getColorIDFromMap(instance, Objects.requireNonNull(overridingStateTrackerMap));
        }

        if(
                tempColorID == -1 &&
                runDatapackColors.get()
            ){
            tempColorID = getColorIDFromMap(instance, Objects.requireNonNull(datapackStateTrackerMap));
        }

        if(
                tempColorID == -1 &&
                runConfigColors.get()
            ){

            tempColorID = getColorIDFromMap(instance, Objects.requireNonNull(configStateTrackerMap));
        }
        return tempColorID;
    }



    private static int getColorIDFromMap(BlockState instance, Map<BlockState, BlockStatePropertyTracker> targetMap){


        HashMap<Property<?>, Boolean> hasPropertyMap;
        Block owner = instance.getBlock();
        BlockState defaultState = owner.defaultBlockState();

        BlockStatePropertyTracker defaultTracker = targetMap.getOrDefault(instance, BlockStatePropertyTracker.DEFAULT);



//        if(defaultTracker == BlockStatePropertyTracker.DEFAULT){
//            return -1;
//        }

//        if(owner == Blocks.BLACK_BED && instance.getValue(BedBlock.FACING) == Direction.SOUTH){
//            System.out.println("Java moment");
//        }


        if(instance.equals(defaultState)
                && defaultTracker.trackedProperties != null
                && !defaultTracker.trackedProperties.isEmpty()
        ){
            hasPropertyMap = populateHasPropertyMapFromValueStateMap(defaultState, defaultTracker.trackedProperties);
        } else {
            hasPropertyMap = populateHasPropertyMap(instance, defaultState);
        }

        //no properties to look for
        if(hasPropertyMap.values().stream().noneMatch((b) -> b)){
            return targetMap.getOrDefault(defaultState, BlockStatePropertyTracker.DEFAULT).colorID;
        }

        Set<BlockState> keys = targetMap.keySet();

        List<BlockState> stateMatches = collectStateMatches(instance, keys, owner, hasPropertyMap, targetMap);


        if(stateMatches.isEmpty())
            return -1;

        stateMatches.sort(ColorStateMapManager::compareStateValues);

        return targetMap.getOrDefault(stateMatches.getFirst(), BlockStatePropertyTracker.DEFAULT).colorID;

    }

    private static HashMap<Property<?>, Boolean> populateHasPropertyMapFromValueStateMap(BlockState defaultState, List<String> strings) {

        HashMap<Property<?>, Boolean> hasPropertyMap = new HashMap<>();

        for(Property<?> p : defaultState.getProperties()){
            if(strings.contains(p.getName())){
                hasPropertyMap.put(p, true);
            } else {
                hasPropertyMap.put(p, false);
            }
        }

        return hasPropertyMap;
    }

    private static HashMap<Property<?>, Boolean> populateHasPropertyMap(BlockState instance, BlockState defaultState) {

        HashMap<Property<?>, Boolean> hasPropertyMap = new HashMap<>();
        for (Property<?> p : instance.getProperties()) {
            if (instance.getValue(p).equals(defaultState.getValue(p))) {
                hasPropertyMap.put(p, false);
            } else {
                hasPropertyMap.put(p, true);
            }
        }

        return hasPropertyMap;
    }

    private static ArrayList<BlockState> collectStateMatches(BlockState instance, Set<BlockState> keys, Block owner, HashMap<Property<?>, Boolean> hasProperty, Map<BlockState, BlockStatePropertyTracker> targetMap) {
        ArrayList<BlockState> stateMatches = new ArrayList<>();
        for(BlockState state : keys){
            if(state.getBlock() != owner) continue;

            List<String> definedStateProperties = targetMap.getOrDefault(state, BlockStatePropertyTracker.DEFAULT).trackedProperties;

            if(definedStateProperties == null) {
                stateMatches.add(state);
                continue;
            }

            boolean matches = true;

            for(Property<?> otherP : state.getProperties()){

                if(!hasProperty.containsKey(otherP)){
                    continue;//we can filter for specific states later
                }

                if(
                        !state.getValue(otherP).equals(instance.getValue(otherP))
                                && definedStateProperties.contains(otherP.getName())
                ){
                    matches = false;
                }

            }

            if(matches){
                stateMatches.add(state);
            }
        }

        return stateMatches;
    }

    public static int getNumChangedProperties(BlockState state){
        BlockState defaultState = state.getBlock().defaultBlockState();
        int numChanged = 0;
        for(Property<?> p : state.getProperties()){
            if(state.getValue(p).equals(defaultState.getValue(p))){
                numChanged++;
            }
        }

        return numChanged;
    }

    public static int compareStateValues(BlockState state1, BlockState state2){
        int state1Changed = getNumChangedProperties(state1);
        int state2Changed = getNumChangedProperties(state2);

        return Integer.compare(state1Changed, state2Changed);
    }





    public record BlockStatePropertyTracker(BlockState state, int colorID, @Nullable List<String> trackedProperties){
        public static final Codec<BlockStatePropertyTracker> PROPERTY_TRACKER_CODEC = RecordCodecBuilder.create(
                (instance) ->
                        instance.group(
                                BlockState.CODEC.fieldOf("state").forGetter(BlockStatePropertyTracker::state),
                                Codec.intRange(0, 63).fieldOf("colorID").forGetter(BlockStatePropertyTracker::colorID),
                                Codec.list(Codec.STRING).optionalFieldOf("trackedProperties").forGetter((inst) -> inst.trackedProperties() == null ? Optional.empty() : Optional.of(inst.trackedProperties()))
                        ).apply(instance, (state, id, op) -> new BlockStatePropertyTracker(state, id, op.orElse(null)))
        );

        public static final BlockStatePropertyTracker DEFAULT = new BlockStatePropertyTracker(Blocks.AIR.defaultBlockState(), -1, null);

        @Override
        public boolean equals(Object obj) {

            if(obj == this){
                return true;
            }

            if(!(obj instanceof BlockStatePropertyTracker(BlockState state1, int id, List<String> properties))) return false;

            if(colorID != id) return false;

            if((this.trackedProperties == null) != (properties == null)) return false;

            if(this.trackedProperties != null
                    /* implied: && o.trackedProperties != null*/
                    && !this.trackedProperties.equals(properties))
                return false;

            return blockStatesEqual(state, state1);

        }
    }

    public static boolean blockStatesEqual(BlockState s1, BlockState s2){

        for(Property<?> p : s1.getProperties()){
            if(!s2.hasProperty(p)) return false;

            if(!s1.getValue(p).equals(s2.getValue(p))) return false;
        }

        return true;
    }

}
