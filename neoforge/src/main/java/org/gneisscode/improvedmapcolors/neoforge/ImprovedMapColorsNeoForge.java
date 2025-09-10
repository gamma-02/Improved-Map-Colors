package org.gneisscode.improvedmapcolors.neoforge;

import com.mojang.logging.LogUtils;
import dev.architectury.neoforge.ArchitecturyNeoForge;
import dev.architectury.platform.hooks.EventBusesHooks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import net.neoforged.fml.common.Mod;
import org.gneisscode.improvedmapcolors.MapColorBlock;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.MOD_ID;

@Mod(MOD_ID)
public final class ImprovedMapColorsNeoForge {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    public static final DeferredHolder<Block, MapColorBlock> MAP_COLOR_BLOCK = BLOCKS.register(
            "map_color_block",
            (name) -> new MapColorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARRIER).mapColor(MapColorBlock.getMapColorFunction()).setId(ResourceKey.create(Registries.BLOCK, name)))
    );

    public ImprovedMapColorsNeoForge(ModContainer container){

        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG_SPEC);

        ImprovedMapColors.init();

        BLOCKS.register(container.getEventBus());

    }
}
