package org.gneisscode.improvedmapcolors;


import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.MapColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public final class ImprovedMapColors {
    public static final String MOD_ID = "improvedmapcolors";

    //far future todo: redoing the map storage system


    public static void init() {

    }

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    public static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);

    public static ResourceKey<Block> BLOCK_KEY = ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_color_block"));
    public static final RegistrySupplier<Block> MAP_BLOCK = BLOCKS.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_color_block"), () -> new MapColorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARRIER).mapColor(MapColorBlock.getMapColorFunction()).setId(BLOCK_KEY)));

    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);
    public static ResourceKey<Item> ITEM_KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_block_item"));
    public static final RegistrySupplier<Item> MAP_BLOCK_ITEM = ITEMS.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_block_item"), () -> new MapColorBlockItem(MAP_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).setId(ITEM_KEY)));

    public static List<Color> indexIdColorList;

    public static HashMap<BlockState, Integer> blockStateIdMap;
    public static HashMap<BlockState, List<String>> valueStateMap = new HashMap<>();



    public static MapColor getMapColorFromBlockState(BlockState instance, MapColor m) {
        int color = containsBlockState(instance);
        if(color != -1){
            m = MapColor.MATERIAL_COLORS[color];
        }
        return m;
    }

    public static int containsBlockState(BlockState instance){

        Set<BlockState> keys = blockStateIdMap.keySet();
        HashMap<Property<?>, Boolean> hasProperty = new HashMap<>();
        Block owner = instance.getBlock();
        BlockState defaultState = owner.defaultBlockState();
        if(instance.equals(defaultState) && valueStateMap.containsKey(defaultState)){
            populateHasPropertyMapFromValueStateMap(defaultState, valueStateMap.get(defaultState), hasProperty);
        }else {
            populateHasPropertyMap(instance, defaultState, hasProperty);
        }

        if(hasProperty.values().stream().noneMatch((b) -> b)){

            return blockStateIdMap.getOrDefault(defaultState, -1);
        }

        //the issue with this bit is that it's overriding other block states
        //so we should maintain a list of what matches
        //and then filter it?
        //this problem (^w^) is hard


        ArrayList<BlockState> stateMatches = collectStateMatches(instance, keys, owner, hasProperty);

        if(stateMatches.isEmpty())
            return -1;

        stateMatches.sort(ImprovedMapColors::compareStateValues);

        if(owner == Blocks.BLACK_BED && instance.getValue(BlockStateProperties.BED_PART) == BedPart.FOOT && instance.getValue(BedBlock.FACING) == Direction.SOUTH){
            init();
        }

        return blockStateIdMap.getOrDefault(stateMatches.getFirst(), -1);
    }

    private static void populateHasPropertyMapFromValueStateMap(BlockState defaultState, List<String> strings, HashMap<Property<?>, Boolean> hasProperty) {
        for(Property<?> p : defaultState.getProperties()){
            if(strings.contains(p.getName())){
                hasProperty.put(p, true);
            } else {
                hasProperty.put(p, false);
            }
        }
    }

    private static void populateHasPropertyMap(BlockState instance, BlockState defaultState, HashMap<Property<?>, Boolean> hasProperty) {
        for (Property<?> p : instance.getProperties()) {
            if (instance.getValue(p).equals(defaultState.getValue(p))) {
                hasProperty.put(p, false);
            } else {
                hasProperty.put(p, true);
            }
        }
    }

    private static ArrayList<BlockState> collectStateMatches(BlockState instance, Set<BlockState> keys, Block owner, HashMap<Property<?>, Boolean> hasProperty) {
        ArrayList<BlockState> stateMatches = new ArrayList<>();
        for(BlockState state : keys){
            if(state.getBlock() != owner) continue;

            List<String> definedStateProperties = valueStateMap.get(state);

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
}
