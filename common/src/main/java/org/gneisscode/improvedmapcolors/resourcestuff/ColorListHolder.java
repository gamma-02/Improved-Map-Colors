package org.gneisscode.improvedmapcolors.resourcestuff;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorListHolder {

    public static Codec<ColorListHolder> COLOR_LIST_CODEC = RecordCodecBuilder.create(
            (instance) ->
                instance.group(
                        Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("colorList").forGetter((f) -> {
                            Map<String, String> colorMap = new HashMap<>();
                            for(int i = 0; i < f.colorList.size(); i++){
                                Color color = f.colorList.get(i);

                                if(color == null){
                                    continue;
                                }

                                colorMap.put(Integer.toString(i), String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
                            }
                            return colorMap;
                        })
                ).apply(instance, ColorListHolder::new)
    );


    public List<Color> colorList;

    public ColorListHolder(Map<String, String> colorIndexMap){
        this.colorList = Lists.newArrayListWithCapacity(64);//should change when map format changes
        for(int i = 0; i < 64; i++){
            String s = colorIndexMap.get(Integer.toString(i));
            if(s == null){
                this.colorList.add(i, null);
                continue;
            }

            try {
                this.colorList.add(i, Color.decode(s));
            } catch (NumberFormatException e) {
                LogUtils.getLogger().error("Color unable to be read!");
            }
        }
    }

    public ColorListHolder(List<Color> colorList){
        this.colorList = colorList;
    }

    public void addColorsToList(List<Color> inputList){
        for(int i = 0; i < inputList.size(); i++){
            Color ourColor = this.colorList.get(i);
            if (ourColor != null) {
                inputList.set(i, ourColor);
            }
        }
    }
}
