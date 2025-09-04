package org.gneisscode.improvedmapcolors.fabric.mixin;

import net.neoforged.fml.config.ConfigTracker;
import net.neoforged.fml.config.ModConfig;
import org.gneisscode.improvedmapcolors.fabric.compat.ConfigTrackerAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Mixin(value = ConfigTracker.class, remap = false)
public abstract class JustLoadMyConfigAlready implements ConfigTrackerAccessor {

    @Shadow
    static void loadConfig(ModConfig par1, Path par2, Consumer<ModConfig> par3) {}

    @Shadow
    static void openConfig(ModConfig config, Path configBasePath, @Nullable Path configOverrideBasePath) {}

    @Shadow
    @Final
    private ConcurrentHashMap<String, ModConfig> fileMap;

    @Override
    public void loadConfigAccessor(ModConfig modConfig, Path path, Consumer<ModConfig> eventConstructor) {
        loadConfig(modConfig, path, eventConstructor);
    }

    @Override
    public void openConfigAccessor(ModConfig config, Path configBasePath, @Nullable Path configOverrideBasePath) {
        openConfig(config, configBasePath, configOverrideBasePath);
    }

    @Override
    public ModConfig getConfig(String modid) {
        return this.fileMap.get(modid);
    }
}
