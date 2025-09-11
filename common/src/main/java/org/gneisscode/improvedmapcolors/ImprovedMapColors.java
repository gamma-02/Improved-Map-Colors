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

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
    public static final Registrar<Block> BLOCKS = MANAGER.get().get(Registries.BLOCK);
    public static final Registrar<Item> ITEMS = MANAGER.get().get(Registries.ITEM);


    public static ResourceKey<Block> MAP_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_color_block"));
    public static final RegistrySupplier<Block> MAP_BLOCK = BLOCKS.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_color_block"), () -> new MapColorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARRIER).mapColor(MapColorBlock.getMapColorFunction()).setId(MAP_BLOCK_KEY)));

    public static ResourceKey<Item> MAP_BLOCK_ITEM_KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_block_item"));
    public static final RegistrySupplier<Item> MAP_BLOCK_ITEM = ITEMS.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "map_block_item"), () -> new MapColorBlockItem(MAP_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).setId(MAP_BLOCK_ITEM_KEY)));


    //far future todo: redoing the map storage system


    public static void init() {

    }



}
