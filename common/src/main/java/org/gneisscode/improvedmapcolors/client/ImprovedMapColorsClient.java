package org.gneisscode.improvedmapcolors.client;

import dev.architectury.event.events.client.ClientPlayerEvent;
import org.gneisscode.improvedmapcolors.ColorListManager;
import org.gneisscode.improvedmapcolors.PresetManager;

public class ImprovedMapColorsClient {


    public static void clientInit(){
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((a) -> {

            ColorListManager.onDisconnect();
            //????? why is this nessecary?? why is this called when you're joining???
            if(a == null) return;
            PresetManager.selectedPreset = null;
        });
    }

    
}
