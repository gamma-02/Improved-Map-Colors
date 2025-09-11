package org.gneisscode.improvedmapcolors.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private static Set<Item> MARKER_PARTICLE_ITEMS;

    @Inject(method = "getMarkerParticleTarget", at = @At("RETURN"), cancellable = true)
    private void injectMarkerParticalTarget(CallbackInfoReturnable<Block> cir){

        if (this.minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            ItemStack itemStack = this.minecraft.player.getMainHandItem();
            Item item = itemStack.getItem() == ImprovedMapColors.MAP_BLOCK_ITEM.get()
                    ? itemStack.getItem()
                    : this.minecraft.player.getOffhandItem().getItem();
            if (item == ImprovedMapColors.MAP_BLOCK_ITEM.get() && item instanceof BlockItem blockItem) {
                cir.setReturnValue(blockItem.getBlock());
            }
        }
    }
}
