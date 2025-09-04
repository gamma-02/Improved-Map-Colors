package org.gneisscode.improvedmapcolors;

import eu.midnightdust.lib.config.MidnightConfig;

public final class ImprovedMapColors {
    public static final String MOD_ID = "improvedmapcolors";

    //todo: saving the config properly
    //todo: runtime swapping of colors, possibly replacing the method of calculating the colors on screen
    //todo: changing the block state registered to a color

    //far future todo: redoing the map storage system

    public static void init() {
        // Write common init code here.
        MidnightConfig.init(MOD_ID, CommonConfig.class);
    }
}
