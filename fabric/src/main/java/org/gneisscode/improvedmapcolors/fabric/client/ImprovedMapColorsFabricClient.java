package org.gneisscode.improvedmapcolors.fabric.client;

import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;

public final class ImprovedMapColorsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        ConfigScreenFactoryRegistry.INSTANCE.register(ImprovedMapColors.MOD_ID, ConfigurationScreen::new);
    }
}
