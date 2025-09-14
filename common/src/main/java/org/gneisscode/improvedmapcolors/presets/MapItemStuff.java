package org.gneisscode.improvedmapcolors.presets;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapItemStuff {

//    public void update(Level level, Entity entity, MapItemSavedData mapItemSavedData) {
//        if (level.dimension() == mapItemSavedData.dimension && entity instanceof Player) {
//            int scaleDivisor = 1 << mapItemSavedData.scale;
//            int centerX = mapItemSavedData.centerX;
//            int centerZ = mapItemSavedData.centerZ;
//            int regionXCenter = Mth.floor(entity.getX() - centerX) / scaleDivisor + 64;
//            int regionZCenter = Mth.floor(entity.getZ() - centerZ) / scaleDivisor + 64;
//            int mappingRegionRadius = 128 / scaleDivisor;
//            if (level.dimensionType().hasCeiling()) {
//                mappingRegionRadius /= 2;
//            }
//
//            MapItemSavedData.HoldingPlayer holdingPlayer = mapItemSavedData.getHoldingPlayer((Player)entity);
//            holdingPlayer.step++;
//
//            BlockPos.MutableBlockPos currentBlockPos = new BlockPos.MutableBlockPos();
//            BlockPos.MutableBlockPos mutableBlockPos2 = new BlockPos.MutableBlockPos();
//
//            boolean bl = false;
//
//            for (int mappingRegionXIter = regionXCenter - mappingRegionRadius + 1; mappingRegionXIter < regionXCenter + mappingRegionRadius; mappingRegionXIter++) {
//                if ((mappingRegionXIter & 15) == (holdingPlayer.step & 15) || bl) {
//                    bl = false;
//                    double d = 0.0;
//
//                    for (int mappingRegionZIter = regionZCenter - mappingRegionRadius - 1; mappingRegionZIter < regionZCenter + mappingRegionRadius; mappingRegionZIter++) {
//
//                        if (mappingRegionXIter >= 0 && mappingRegionZIter >= -1 && mappingRegionXIter < 128 && mappingRegionZIter < 128) {
//
//                            int distSqrFromRegionCenter = Mth.square(mappingRegionXIter - regionXCenter) + Mth.square(mappingRegionZIter - regionZCenter);
//
//                            boolean inRegion = distSqrFromRegionCenter > (mappingRegionRadius - 2) * (mappingRegionRadius - 2);
//
//                            int mappingRegionCenterXScaled = (centerX / scaleDivisor + mappingRegionXIter - 64) * scaleDivisor;
//                            int mappingRegionCenterZScaled = (centerZ / scaleDivisor + mappingRegionZIter - 64) * scaleDivisor;
//                            Multiset<MapColor> mapColorMultiSet = LinkedHashMultiset.create();
//                            LevelChunk mapChunk = level.getChunk(SectionPos.blockToSectionCoord(mappingRegionCenterXScaled), SectionPos.blockToSectionCoord(mappingRegionCenterZScaled));
//                            if (!mapChunk.isEmpty()) {
//                                int t = 0;
//                                double e = 0.0;
//                                //just stone everywhere
//                                if (level.dimensionType().hasCeiling()) {
//                                    //woah
//                                    //0b00000000000000111000100110111111
//                                    //0x000389BF
//                                    int u = mappingRegionCenterXScaled + mappingRegionCenterZScaled * 231871;
//
//                                    //even more woah, wtf
//                                    //0b00000001110111010110011101010001
//                                    //0x01DD6751
//                                    u = u * u * 31287121 + u * 11;
//
//                                    if ((u >> 20 & 1) == 0) {
//                                        mapColorMultiSet.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
//                                    } else {
//                                        mapColorMultiSet.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
//                                    }
//
//                                    e = 100.0;
//                                } else {
//                                    for (int chunkXIter = 0; chunkXIter < scaleDivisor; chunkXIter++) {
//                                        for (int chunkZIter = 0; chunkZIter < scaleDivisor; chunkZIter++) {
//                                            currentBlockPos.set(mappingRegionCenterXScaled + chunkXIter, 0, mappingRegionCenterZScaled + chunkZIter);
//                                            int columnHeight = mapChunk.getHeight(Heightmap.Types.WORLD_SURFACE, currentBlockPos.getX(), currentBlockPos.getZ()) + 1;
//                                            BlockState blockState;
//                                            if (columnHeight <= level.getMinY()) {
//                                                blockState = Blocks.BEDROCK.defaultBlockState();
//                                            } else {
//                                                //move the mutableBlockPos down until we find a block
//                                                do {
//                                                    currentBlockPos.setY(--columnHeight);
//                                                    blockState = mapChunk.getBlockState(currentBlockPos);
//                                                } while (blockState.getMapColor(level, currentBlockPos) == MapColor.NONE
//                                                        && columnHeight > level.getMinY());
//
//
//                                                if (columnHeight > level.getMinY() && !blockState.getFluidState().isEmpty()) {
//                                                    int fluidY = columnHeight - 1;
//                                                    mutableBlockPos2.set(currentBlockPos);
//
//                                                    BlockState blockState2;
//                                                    do {
//                                                        mutableBlockPos2.setY(fluidY--);
//                                                        blockState2 = mapChunk.getBlockState(mutableBlockPos2);
//                                                        t++;
//                                                    } while (fluidY > level.getMinY() && !blockState2.getFluidState().isEmpty());
//
//                                                    blockState = this.getCorrectStateForFluidBlock(level, blockState, currentBlockPos);
//                                                }
//                                            }
//
//                                            mapItemSavedData.checkBanners(level, currentBlockPos.getX(), currentBlockPos.getZ());
//                                            e += (double)columnHeight / (scaleDivisor * scaleDivisor);
//                                            mapColorMultiSet.add(blockState.getMapColor(level, currentBlockPos));
//                                        }
//                                    }
//                                }
//
//                                t /= scaleDivisor * scaleDivisor;
//                                MapColor mapColor = Iterables.getFirst(Multisets.copyHighestCountFirst(mapColorMultiSet), MapColor.NONE);
//                                MapColor.Brightness brightness;
//
//                                int mappingRegionTerm = (mappingRegionXIter + mappingRegionZIter & 1);
//
//                                if (mapColor == MapColor.WATER) {
//                                    //highlight underwater terrain
//                                    double f = t * 0.1 + mappingRegionTerm * 0.2;
//                                    if (f < 0.5) {
//                                        brightness = MapColor.Brightness.HIGH;
//                                    } else if (f > 0.9) {
//                                        brightness = MapColor.Brightness.LOW;
//                                    } else {
//                                        brightness = MapColor.Brightness.NORMAL;
//                                    }
//                                } else {
//                                    //highlight terrain
//                                    double f = (e - d) * 4.0 / (scaleDivisor + 4) + (mappingRegionTerm - 0.5) * 0.4;
//                                    if (f > 0.6) {
//                                        brightness = MapColor.Brightness.HIGH;
//                                    } else if (f < -0.6) {
//                                        brightness = MapColor.Brightness.LOW;
//                                    } else {
//                                        brightness = MapColor.Brightness.NORMAL;
//                                    }
//                                }
//
//                                d = e;
//                                if (mappingRegionZIter >= 0 && distSqrFromRegionCenter < mappingRegionRadius * mappingRegionRadius && (!inRegion || (mappingRegionXIter + mappingRegionZIter & 1) != 0)) {
//                                    bl |= mapItemSavedData.updateColor(mappingRegionXIter, mappingRegionZIter, mapColor.getPackedId(brightness));
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
}
