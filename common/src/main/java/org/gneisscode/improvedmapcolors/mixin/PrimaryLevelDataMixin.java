package org.gneisscode.improvedmapcolors.mixin;


import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.gneisscode.improvedmapcolors.presets.PresetDataContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin implements PresetDataContainer {

    @Unique
    public PresetManager.Preset improvedmapcolors$savedPreset;

    @Inject(method = "setTagData", at = @At("HEAD"))
    private void addCustomTagData(RegistryAccess registryAccess, CompoundTag compoundTag, CompoundTag compoundTag2, CallbackInfo ci){
        compoundTag.putString("mapColorPreset", this.improvedmapcolors$savedPreset.getSerializedName());
    }

//    @Inject(method = "parse", at = @At("HEAD"))
//    private static <T> void getCustomLevelData(
//            Dynamic<T> dynamic,
//            LevelSettings levelSettings,
//            PrimaryLevelData.SpecialWorldProperty specialWorldProperty,
//            WorldOptions worldOptions,
//            Lifecycle lifecycle,
//            CallbackInfoReturnable<PrimaryLevelData> cir){
//        PresetManager.loadWorldPreset(dynamic.get("mapColorPreset").asString(PresetManager.DEFAULT.getSerializedName()));
//        this.improvedmapcolors$savedPreset = PresetManager.Preset.getFromSeiralizedName(dynamic.get("mapColorPreset").asString(PresetManager.DEFAULT.getSerializedName()));
    //done: mixin to level storage source and set ^
//    }

    @Override
    @Unique
    public PresetManager.Preset getPreset() {
        return improvedmapcolors$savedPreset;
    }

    @Override
    @Unique
    public void setPreset(PresetManager.Preset preset) {
        PresetManager.selectedPreset = preset;
        this.improvedmapcolors$savedPreset = preset;
    }
}
