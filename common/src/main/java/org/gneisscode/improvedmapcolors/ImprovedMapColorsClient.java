package org.gneisscode.improvedmapcolors;

import dev.architectury.event.events.client.ClientPlayerEvent;

public class ImprovedMapColorsClient {


    public static void clientInit(){
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register((a) -> {
            ColorListManager.onDisconnect();
        });
    }

    
}
