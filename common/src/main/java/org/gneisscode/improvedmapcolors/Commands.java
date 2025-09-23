package org.gneisscode.improvedmapcolors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.MapItem;

public class Commands {


//    public static void refreshMap(Entity e){
//
//        if(e instanceof ItemFrame ie){
//            ie.get
//        }
//    }
//
//    public static void refreshMap(MapItem map){
//
//        map.update();
//    }



    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, net.minecraft.commands.Commands.CommandSelection commandSelection) {
//        dispatcher.register(
//                LiteralArgumentBuilder.<CommandSourceStack>literal("reloadMaps").requires(net.minecraft.commands.Commands.hasPermission(2)).executes((ctx) -> {
//                    var source = ctx.getSource();
//
//                    source.getLevel().getAllEntities().forEach(Commands::refreshMap);
//
//
//                    return 1;
//                })
//        )
    }
}
