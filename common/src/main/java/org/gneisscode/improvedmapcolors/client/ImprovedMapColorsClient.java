package org.gneisscode.improvedmapcolors.client;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.saveddata.maps.MapId;
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


        ClientCommandRegistrationEvent.EVENT.register(((dispatcher, context) -> {
            dispatcher.register(LiteralArgumentBuilder.
                    <ClientCommandRegistrationEvent.ClientCommandSourceStack>literal("reloadMaps")
                    .executes((o) -> {
                        Minecraft.getInstance().getMapTextureManager().maps.forEach((id, instance) -> {
                            instance.forceUpload();
                        });

                        return 1;
            }));
        }));
    }

    
}
