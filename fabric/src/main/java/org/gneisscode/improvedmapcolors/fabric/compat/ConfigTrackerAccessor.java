package org.gneisscode.improvedmapcolors.fabric.compat;

import net.neoforged.fml.config.ModConfig;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.Consumer;

public interface ConfigTrackerAccessor {

    void loadConfigAccessor(ModConfig modConfig, Path path, Consumer<ModConfig> eventConstructor);
    void openConfigAccessor(ModConfig config, Path configBasePath, @Nullable Path configOverrideBasePath);

    ModConfig getConfig(String modid);
}
