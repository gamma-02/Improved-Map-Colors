package org.gneisscode.improvedmapcolors.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.world.level.material.MapColor;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MapColor.class)
public class MapColorMixin {

//    @Inject(method = "<clinit>", at = @At("HEAD"))
//    private static void loadedNow(CallbackInfo ci){
//        LogUtils.getLogger().info("MapColor Common clinit");
//
//    }
//
//    @ModifyArgs(
//            method = "<clinit>",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/MapColor;<init>(II)V")
//    )
//    private static void injectNew(Args args){
//
//    }
}
