package org.gneisscode.improvedmapcolors.client;

import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.gneisscode.improvedmapcolors.PresetManager;

public class PresetOptionsScreen extends OptionsSubScreen {



    public PresetOptionsScreen(Screen screen, Options options, Component component) {
        super(screen, options, component);
    }

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
                }).build();

                final StringWidget label = new StringWidget(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT, Component.translatable("improvedmapcolors.configuration.preset." + p.getSerializedName()), font).alignLeft();
                label.setTooltip(Tooltip.create(Component.translatable("improvedmapcolors.configuration.preset." + p.getSerializedName() + ".tooltip")));
                list.addSmall(label, b);

            }
        }
    }


}
