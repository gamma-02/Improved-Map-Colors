package org.gneisscode.improvedmapcolors.resourcestuff;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.apache.commons.compress.utils.Lists;
import org.gneisscode.improvedmapcolors.ColorStateMapManager.BlockStatePropertyTracker;
import org.gneisscode.improvedmapcolors.ColorStateMapManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StateMapHolder {

//    public static final Codec<BlockState>

    public static final Codec<StateMapHolder> STATE_HOLDER_CODEC = RecordCodecBuilder.create(
            (instance) ->
                    instance.group(
                            Codec.list(BlockStatePropertyTracker.PROPERTY_TRACKER_CODEC)
                                    .fieldOf("stateColorIDList")
                                    .forGetter((f) -> f.stateColorIDMap)
                            ).apply(instance, StateMapHolder::new)

    );


    public List<BlockStatePropertyTracker> stateColorIDMap;


    public StateMapHolder(List<BlockStatePropertyTracker> stateColorMap){
        this.stateColorIDMap = stateColorMap;

    }

    public void addTrackersToMap(@NotNull Map<BlockState, BlockStatePropertyTracker> trackerMap){
        for(BlockStatePropertyTracker t : this.stateColorIDMap){
            trackerMap.put(t.state(), t);
        }
    }

    public void addStatesToMap(Map<BlockState, Integer> stateColorIDMap){
        for(BlockStatePropertyTracker tracker : this.stateColorIDMap){
            BlockState defaultState = tracker.state().getBlock().defaultBlockState();

            for(Property<?> p : tracker.state().getProperties()){

                if(p instanceof IntegerProperty ip)
                    defaultState = setProperty(defaultState, ip, ip.getName(tracker.state().getValue(ip)));
                else if(p instanceof BooleanProperty bp)
                    defaultState = setProperty(defaultState, bp, bp.getName(tracker.state().getValue(bp)));
                else if(p instanceof EnumProperty<?> ep) {
                    defaultState = setProperty(defaultState, ep, tracker.state().getValue(ep).getSerializedName());
                }

            }
            stateColorIDMap.put(tracker.state(), tracker.colorID());
        }
    }

    public static <T extends Comparable<T>> BlockState setProperty(BlockState state, Property<T> property, String value){
        Optional<T> val = property.getValue(value);
        return val.map(t -> state.setValue(property, t)).orElse(state);
    }

    public void addTrackedPropertiesToMap(Map<BlockState, List<String>> statePropertyListMap){
        for(BlockStatePropertyTracker tracker : this.stateColorIDMap){
            if(tracker.trackedProperties() != null) statePropertyListMap.put(tracker.state(), tracker.trackedProperties());
        }
    }



}
