package org.gneisscode.improvedmapcolors.neoforge.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.presets.PresetDatapackExportTool;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class CustomConfigScreen extends ConfigurationScreen.ConfigurationSectionScreen {
    private String packExportPath = "";
    private String packName = "";

    public CustomConfigScreen(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title) {
        super(parent, type, modConfig, title);
    }

    @Override
    protected Collection<? extends ConfigurationScreen.ConfigurationSectionScreen.Element> createSyntheticValues() {
//        return super.createSyntheticValues();


        ConfigurationScreen.ConfigurationSectionScreen.Element packPath = createStringValue("packExportPath", CommonConfig::zipFileValidator, this::getPackExportPath, this::setPackExportPath);
        ConfigurationScreen.ConfigurationSectionScreen.Element packName = createStringValue("packName", ResourceLocation::isValidNamespace, this::getPackName, this::setPackName);

        Button b = Button.builder(Component.translatable("improvedmapcolors.configuration.exportPack.buttonLabel"), (button) -> {
            File zipFile = new File(packExportPath);

            try{
                PresetDatapackExportTool.exportDataPackPresetFromCSV(zipFile, this.packName);
            } catch (IOException e) {
                LogUtils.getLogger().error("Could not export path!");
                LogUtils.getLogger().error(e.getLocalizedMessage());
            }

        }).build();

        ConfigurationScreen.ConfigurationSectionScreen.Element exportPack = new ConfigurationScreen.ConfigurationSectionScreen.Element(getTranslationComponent("exportPack"), getTooltipComponent("exportPack", null), b, false);


        if(packPath == null || packName == null) return super.createSyntheticValues();

        return List.of(packName, packPath, exportPack);
    }

    public void setPackExportPath(String packExportPath) {
        this.packExportPath = packExportPath;
    }

    public String getPackExportPath() {
        return packExportPath;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }
}
