package org.gneisscode.improvedmapcolors;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class MapColorBlock extends Block implements SimpleWaterloggedBlock {

    public static final IntegerProperty MAP_COLOR = IntegerProperty.create("map_color", 1, 64);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public MapColorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(MAP_COLOR, 1));
    }

    public static Function<BlockState, MapColor> getMapColorFunction(){
        return (state) -> MapColor.MATERIAL_COLORS[state.getValue(MAP_COLOR) - 1];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MAP_COLOR).add(WATERLOGGED);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState arg) {
        return arg.getFluidState().isEmpty();
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState arg) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected float getShadeBrightness(BlockState arg, BlockGetter arg2, BlockPos arg3) {
        return 1.0F;
    }

    @Override
    protected @NotNull BlockState updateShape(
            BlockState arg, LevelReader arg2, ScheduledTickAccess arg3, BlockPos arg4, Direction arg5, BlockPos arg6, BlockState arg7, RandomSource arg8
    ) {
        if ((Boolean)arg.getValue(WATERLOGGED)) {
            arg3.scheduleTick(arg4, Fluids.WATER, Fluids.WATER.getTickDelay(arg2));
        }

        return super.updateShape(arg, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState arg) {
        return arg.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(arg);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext arg) {
        return this.defaultBlockState().setValue(WATERLOGGED, arg.getLevel().getFluidState(arg.getClickedPos()).getType() == Fluids.WATER);
    }


    @Override
    public @NotNull ItemStack pickupBlock(@Nullable LivingEntity arg, LevelAccessor arg2, BlockPos arg3, BlockState arg4) {
        return arg instanceof Player player && player.isCreative() ? SimpleWaterloggedBlock.super.pickupBlock(arg, arg2, arg3, arg4) : ItemStack.EMPTY;
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity arg, BlockGetter arg2, BlockPos arg3, BlockState arg4, Fluid arg5) {
        return arg instanceof Player player && player.isCreative() && SimpleWaterloggedBlock.super.canPlaceLiquid(arg, arg2, arg3, arg4, arg5);
    }

//    @Override
//    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
//        if (!level.isClientSide && player.canUseGameMasterBlocks()) {
//            BlockState nextState = cycleState(blockState, MAP_COLOR, player.isSecondaryUseActive());
//            level.setBlock(blockPos, nextState, 2);
//            message(player, Component.translatable("item.improvedmapcolors.map_block_item.update_color", blockState.getValue(MAP_COLOR), nextState.getValue(MAP_COLOR)));
//            return InteractionResult.SUCCESS_SERVER;
//        } else {
//            return InteractionResult.CONSUME;
//        }
//    }



    @Override
    protected ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return setLightOnStack(super.getCloneItemStack(levelReader, blockPos, blockState, bl), (Integer)blockState.getValue(MAP_COLOR));
    }

    public static ItemStack setLightOnStack(ItemStack itemStack, int i) {
        itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(MAP_COLOR, i));
        return itemStack;
    }

    private static void message(Player player, Component component) {
        ((ServerPlayer)player).sendSystemMessage(component, true);
    }
}
