package org.gneisscode.improvedmapcolors.fabric.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.architectury.event.events.common.ExplosionEvent;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import org.gneisscode.improvedmapcolors.resourcestuff.ColorListResourceReloadListener;
import org.gneisscode.improvedmapcolors.resourcestuff.StateMapResourceReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ReloadableServerResources.class)
public class RegisterServerReloadListeners {

    @WrapOperation(
            method = "method_58296",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/SimpleReloadInstance;create(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Z)Lnet/minecraft/server/packs/resources/ReloadInstance;")
    )
    private static ReloadInstance addOurReloadListeners(ResourceManager arg, List<PreparableReloadListener> list, Executor executor, Executor executor2, CompletableFuture<Unit> completableFuture, boolean bl, Operation<ReloadInstance> original){
        List<PreparableReloadListener> preparableReloadListeners = new ArrayList<>(list);
        preparableReloadListeners.add(ColorListResourceReloadListener.LISTENER);
        preparableReloadListeners.add(StateMapResourceReloadListener.LISTENER);

        return original.call(arg, Collections.unmodifiableList(preparableReloadListeners), executor, executor2, completableFuture, bl);
    }
}
