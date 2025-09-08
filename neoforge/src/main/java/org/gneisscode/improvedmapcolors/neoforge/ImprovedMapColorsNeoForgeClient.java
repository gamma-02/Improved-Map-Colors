package org.gneisscode.improvedmapcolors.neoforge;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;

@Mod(value = ImprovedMapColors.MOD_ID, dist = Dist.CLIENT)
//long name lol
public class ImprovedMapColorsNeoForgeClient {

    public ImprovedMapColorsNeoForgeClient(ModContainer container){
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
