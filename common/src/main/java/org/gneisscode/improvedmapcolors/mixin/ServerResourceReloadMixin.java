package org.gneisscode.improvedmapcolors.mixin;

import net.minecraft.server.MinecraftServer;
import org.gneisscode.improvedmapcolors.SyncColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class ServerResourceReloadMixin {

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        cir.getReturnValue().handleAsync((value, throwable) -> {
            // Hook into fail
//            ServerLifecycleEvents.END_DATA_PACK_RELOAD.invoker().endDataPackReload((MinecraftServer) (Object) this, this.resourceManagerHolder.comp_352(), throwable == null);
            ((MinecraftServer) (Object) this).getAllLevels().forEach(SyncColors::syncColors);
            return value;
        }, (MinecraftServer) (Object) this);
    }
}
