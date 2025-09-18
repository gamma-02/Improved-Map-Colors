package org.gneisscode.improvedmapcolors.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.gneisscode.improvedmapcolors.client.ImprovedMapColorsClient;

@Mod(value = ImprovedMapColors.MOD_ID, dist = Dist.CLIENT)
//long name lol
public class ImprovedMapColorsNeoForgeClient {

    public ImprovedMapColorsNeoForgeClient(ModContainer container){

        ImprovedMapColorsClient.clientInit();

        container.registerExtensionPoint(IConfigScreenFactory.class, (string, screen) -> {
            return new ConfigurationScreen(string, screen, (configScreen, type, config, title) -> {
                return new CustomConfigScreen(screen, type, config, title);
            });
        });


    }
}
