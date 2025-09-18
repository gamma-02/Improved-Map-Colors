package org.gneisscode.improvedmapcolors.presets;

import org.gneisscode.improvedmapcolors.PresetManager;

public interface PresetDataContainer {

    PresetManager.Preset getPreset();

    void setPreset(PresetManager.Preset preset);
}
