package org.gneisscode.improvedmapcolors.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.material.MapColor;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record ColorListSyncPayload(Color[] colors) implements CustomPacketPayload {

    public static CustomPacketPayload.Type<ColorListSyncPayload> ID = new CustomPacketPayload.Type<>(ImprovedMapColors.COLOR_LIST_SYNC_PAYLOAD_ID);

    public static StreamCodec<RegistryFriendlyByteBuf, Color> COLOR_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, (c) -> c != null ? c.getRGB() : 0,
            Color::new
    );
    public static StreamCodec<ByteBuf, Color> NEO_COLOR_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, (c) -> c != null ? c.getRGB() : 0,
            Color::new
    );

    public static StreamCodec<RegistryFriendlyByteBuf, ColorListSyncPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.<RegistryFriendlyByteBuf, Color>list().apply(COLOR_CODEC), ColorListSyncPayload::getColorsAsList,
            ColorListSyncPayload::new
    );

    //WHY WHY WHY
    public static StreamCodec<ByteBuf, ColorListSyncPayload> NEO_CODEC = StreamCodec.composite(
            ByteBufCodecs.<ByteBuf, Color>list().apply(NEO_COLOR_CODEC), ColorListSyncPayload::getColorsAsList,
            ColorListSyncPayload::new
    );

    ColorListSyncPayload(List<Color> colorsList){
        this(colorsList != null ? colorsList.toArray(new Color[0]) : new Color[MapColor.MATERIAL_COLORS.length]);
    }

    public void setColor(int i, Color c){
        this.colors[i] = c;
    }

    public List<Color> getColorsAsList(){
        return new ArrayList<>(Arrays.stream(this.colors).toList());
    }

    public ColorListSyncPayload setColors(List<Color> colors){
        for(int i = 0; i < colors.size(); i++){
            if(i >= this.colors.length) return this;

            this.colors[i] = colors.get(i);
        }
        return this;
    }


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
