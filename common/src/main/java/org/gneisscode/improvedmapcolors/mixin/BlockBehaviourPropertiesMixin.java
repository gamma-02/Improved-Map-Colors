package org.gneisscode.improvedmapcolors.mixin;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(BlockBehaviour.Properties.class)
public class BlockBehaviourPropertiesMixin {

//    @Shadow
//    private Function<BlockState, MapColor> mapColor;

//    @Inject(
//            method = "mapColor(Lnet/minecraft/world/item/DyeColor;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;",
//            at = @At("RETURN"),
//            cancellable = true
//    )
//    public void redirectMapDyeColor(DyeColor dyeColor, CallbackInfoReturnable<BlockBehaviour.Properties> cir){
//        mapColor =
//    }

}
