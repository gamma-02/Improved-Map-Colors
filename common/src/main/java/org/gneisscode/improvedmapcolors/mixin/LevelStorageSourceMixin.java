package org.gneisscode.improvedmapcolors.mixin;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.gneisscode.improvedmapcolors.presets.PresetDataContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LevelStorageSource.class)
public class LevelStorageSourceMixin {

    @Inject(method = "getLevelDataAndDimensions", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void setColorPreset(Dynamic<?> dynamic, WorldDataConfiguration worldDataConfiguration, Registry<LevelStem> registry, HolderLookup.Provider provider, CallbackInfoReturnable<LevelDataAndDimensions> cir, Dynamic dynamic2, Dynamic dynamic3, WorldGenSettings worldGenSettings, LevelSettings levelSettings, WorldDimensions.Complete complete, Lifecycle lifecycle, PrimaryLevelData primaryLevelData){
        ((PresetDataContainer) primaryLevelData).setPreset(PresetManager.Preset.getFromSeiralizedName(dynamic.get("mapColorPreset").asString(PresetManager.DEFAULT.getSerializedName())));
    }

}
