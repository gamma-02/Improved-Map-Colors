package org.gneisscode.improvedmapcolors;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.function.Consumer;

import static org.gneisscode.improvedmapcolors.MapColorBlock.MAP_COLOR;

public class MapColorBlockItem extends BlockItem {
    public MapColorBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        consumer.accept(Component.translatable("item.improvedmapcolors.map_block_item.tooltip", Component.keybind("key.use"), Component.keybind("key.use"), Component.keybind("key.sneak")));
        super.appendHoverText(itemStack, tooltipContext, tooltipDisplay, consumer, tooltipFlag);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();

        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);

        if(blockState.getBlock() != ImprovedMapColors.MAP_BLOCK.get()) return super.useOn(useOnContext);

        if (!level.isClientSide && player != null && player.canUseGameMasterBlocks()) {


            BlockState nextState = cycleState(blockState, player.isSecondaryUseActive());
            level.setBlock(blockPos, nextState, 2);
            message(player, Component.translatable("item.improvedmapcolors.map_block_item.update_color", blockState.getValue(MAP_COLOR) - 1, nextState.getValue(MAP_COLOR) - 1));
            return InteractionResult.SUCCESS_SERVER;
        } else {
            return InteractionResult.CONSUME;
        }


    }

    private static BlockState cycleState(BlockState blockState, boolean bl) {
        return blockState.setValue( MapColorBlock.MAP_COLOR, getRelative((MapColorBlock.MAP_COLOR).getPossibleValues(), blockState.getValue( MapColorBlock.MAP_COLOR), bl));
    }

    private static int getRelative(List<Integer> iterable, Integer object, boolean bl) {
        if(object.equals(iterable.getFirst()) && bl) return object; //prevent underflow
        return bl ? Util.findPreviousInIterable(iterable, object) : Util.findNextInIterable(iterable, object);
    }

    private static void message(Player player, Component component) {
        ((ServerPlayer) player).sendSystemMessage(component, true);
    }
}
