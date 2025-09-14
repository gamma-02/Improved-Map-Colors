package org.gneisscode.improvedmapcolors.neoforge.client;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.gneisscode.improvedmapcolors.CommonConfig;
import org.gneisscode.improvedmapcolors.presets.PresetDatapackExportTool;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CustomConfigScreen extends ConfigurationScreen.ConfigurationSectionScreen {
    private String packExportPath = "";
    private String packName = "";

    public CustomConfigScreen(Screen parent, ModConfig.Type type, ModConfig modConfig, Component title) {
        super(parent, type, modConfig, title);
    }




    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected ConfigurationScreen.ConfigurationSectionScreen rebuild() {
        if (list != null) { // this may be called early, skip and wait for init() then
            list.children().clear();
            boolean hasUndoableElements = false;

            final List<@Nullable Element> elements = new ArrayList<>();

            Button b = Button.builder(Component.translatable("improvedmapcolors.configuration.presetsButton"), (button) -> {
//                File zipFile = new File(packExportPath);
//
//                try{
//                    PresetDatapackExportTool.exportDataPackPresetFromCSV(zipFile, this.packName);
//                } catch (IOException e) {
//                    LogUtils.getLogger().error("Could not export path!");
//                    LogUtils.getLogger().error(e.getMessage());
//                }
                System.out.println("Do open datapack selection screen here!");

            }).build();


            Element presets = new Element(getTranslationComponent("presets"), getTooltipComponent("presets", null), b, false);

            elements.add(presets);
            for (final UnmodifiableConfig.Entry entry : context.entries()) {
                final String key = entry.getKey();
                final Object rawValue = entry.getRawValue();
                switch (entry.getRawValue()) {
                    case ModConfigSpec.ConfigValue cv -> {
                        var valueSpec = getValueSpec(key);
                        var element = switch (valueSpec) {
                            case ModConfigSpec.ListValueSpec listValueSpec -> createList(key, listValueSpec, cv);
                            case ModConfigSpec.ValueSpec spec when cv.getClass() == ModConfigSpec.ConfigValue.class && spec.getDefault() instanceof String -> createStringValue(key, valueSpec::test, () -> (String) cv.getRaw(), cv::set);
                            case ModConfigSpec.ValueSpec spec when cv.getClass() == ModConfigSpec.ConfigValue.class && spec.getDefault() instanceof Integer -> createIntegerValue(key, valueSpec, () -> (Integer) cv.getRaw(), cv::set);
                            case ModConfigSpec.ValueSpec spec when cv.getClass() == ModConfigSpec.ConfigValue.class && spec.getDefault() instanceof Long -> createLongValue(key, valueSpec, () -> (Long) cv.getRaw(), cv::set);
                            case ModConfigSpec.ValueSpec spec when cv.getClass() == ModConfigSpec.ConfigValue.class && spec.getDefault() instanceof Double -> createDoubleValue(key, valueSpec, () -> (Double) cv.getRaw(), cv::set);
                            case ModConfigSpec.ValueSpec spec when cv.getClass() == ModConfigSpec.ConfigValue.class && spec.getDefault() instanceof Enum<?> -> createEnumValue(key, valueSpec, (Supplier) cv::getRaw, (Consumer) cv::set);
                            case null -> null;

                            default -> switch (cv) {
                                case ModConfigSpec.BooleanValue value -> createBooleanValue(key, valueSpec, value::getRaw, value::set);
                                case ModConfigSpec.IntValue value -> createIntegerValue(key, valueSpec, value::getRaw, value::set);
                                case ModConfigSpec.LongValue value -> createLongValue(key, valueSpec, value::getRaw, value::set);
                                case ModConfigSpec.DoubleValue value -> createDoubleValue(key, valueSpec, value::getRaw, value::set);
                                case ModConfigSpec.EnumValue value -> createEnumValue(key, valueSpec, (Supplier) value::getRaw, (Consumer) value::set);
                                default -> createOtherValue(key, cv);
                            };
                        };
                        elements.add(context.filter().filterEntry(context, key, element));
                    }
                    case UnmodifiableConfig subsection when context.valueSpecs().get(key) instanceof UnmodifiableConfig subconfig -> elements.add(createSection(key, subconfig, subsection));
                    default -> elements.add(context.filter().filterEntry(context, key, createOtherSection(key, rawValue)));
                }
            }
            elements.addAll(createSyntheticValues());

            for (final Element element : elements) {
                if (element != null) {
                    if (element.name() == null) {
                        list.addSmall(new StringWidget(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.empty(), font), element.getWidget(options));
                    } else {
                        final StringWidget label = new StringWidget(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, element.name(), font).alignLeft();
                        label.setTooltip(Tooltip.create(element.tooltip()));
                        list.addSmall(label, element.getWidget(options));
                    }
                    hasUndoableElements |= element.undoable();
                }
            }

            if (hasUndoableElements && undoButton == null) {
                createUndoButton();
                createResetButton();
            }
        }
        return this;
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
