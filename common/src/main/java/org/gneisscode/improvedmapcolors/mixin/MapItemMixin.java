package org.gneisscode.improvedmapcolors.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.gneisscode.improvedmapcolors.ColorStateMapManager;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MapItem.class)
public class MapItemMixin {

    @WrapOperation(
            method = "update",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getMapColor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/MapColor;")
    )
    public MapColor wrapMapColor(BlockState instance, BlockGetter blockGetter, BlockPos blockPos, Operation<MapColor> original){

        if(!CommonConfig.CONFIG_SPEC.isLoaded()){
            return original.call(instance, blockGetter, blockPos);
        }

        MapColor m = null;

        m = ColorStateMapManager.getMapColorFromBlockState(instance, m);

        if(m != null){
            return m;
        }

        return original.call(instance, blockGetter, blockPos);

    }


}
