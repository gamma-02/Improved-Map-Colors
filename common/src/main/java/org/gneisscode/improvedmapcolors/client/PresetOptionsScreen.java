package org.gneisscode.improvedmapcolors.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.gneisscode.improvedmapcolors.PresetManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PresetOptionsScreen extends OptionsSubScreen {



    public PresetOptionsScreen(Screen screen, Options options, Component component) {
        super(screen, options, component);
    }

    private Map<PresetManager.Preset, Button> buttons = new Object2ObjectArrayMap<>();

    @Override
    protected void addOptions() {
        if(list != null){

            for(PresetManager.Preset p : PresetManager.Preset.values()){

                if(!p.enabled) continue;

                /*
                DDRC |= (1 << DDC5);
                PORTC |= (1 << PC5);
                 */
                Button b = Button.builder(Component.translatable("improvedmapcolors.configuration.preset.button." + p.getSerializedName()), (s) -> {
                    PresetManager.setAndSendSelectedPreset(p);
                    refreshButtons();
                }).build();

                buttons.put(p, b);

                final StringWidget label = new StringWidget(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.translatable("improvedmapcolors.configuration.preset." + p.getSerializedName()), font).alignLeft();
                label.setTooltip(Tooltip.create(Component.translatable("improvedmapcolors.configuration.preset.%s.tooltip".formatted(p.getSerializedName()))));
                list.addSmall(label, b);

            }

            refreshButtons();
        }
    }

//    @Override
//    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
//        refreshButtons();
//        super.render(guiGraphics, i, j, f);
//    }

    public void refreshButtons(){
        for(PresetManager.Preset p : buttons.keySet()){
            buttons.get(p).active = PresetManager.selectedPreset != p;
        }
    }


}
