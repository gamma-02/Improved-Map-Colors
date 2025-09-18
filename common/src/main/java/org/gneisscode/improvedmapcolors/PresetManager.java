package org.gneisscode.improvedmapcolors;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.LevelResource;
import org.gneisscode.improvedmapcolors.networking.SelectPresetC2SPayload;
import org.gneisscode.improvedmapcolors.presets.PresetDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.gneisscode.improvedmapcolors.ImprovedMapColors.resource;

public class PresetManager {

    public static StreamCodec<RegistryFriendlyByteBuf, Preset> PRESET_PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Preset::getSerializedName,
            Preset::getFromSeiralizedName
    );

    public static StreamCodec<ByteBuf, Preset> NEO_PRESET_PACKET_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, Preset::getSerializedName,
            Preset::getFromSeiralizedName
    );

    public static final Map<Preset, Resource> packDefaults = new HashMap<>();

    public static Resource addPack(Preset p, Resource r){
        return packDefaults.put(p, r);
    }

    public static void clearPackDefaults(){
        packDefaults.clear();
    }

    public static void loadPreset(CompoundTag worldData){

    }



    public static final Preset DEFAULT = Preset.VANILLA;

    //used on the client to sync server-side preset
    @Nullable
    public static Preset selectedPreset = null;

    public static String getSelectedPresetName(){
        return Objects.requireNonNullElse(selectedPreset, DEFAULT).getSerializedName();
    }

    //called on client
    @ExpectPlatform
    public static void setAndSendSelectedPreset(Preset preset) { }

    //called on the server
    @ExpectPlatform
    public static void syncPresetOnPlayerJoin(ServerPlayer p) { }


    public static void loadWorldPreset(String mapColorPreset) {

        Preset loadedPreset = Preset.getFromSeiralizedName(mapColorPreset);

        setMapPreset(loadedPreset);

    }


    public static Preset getServerPreset(MinecraftServer server){

        if(server == null) return selectedPreset;

        for(ServerLevel l : server.getAllLevels()){
            if(l.getLevelData() instanceof PresetDataContainer c){
                return c.getPreset();
            }
        }
        return null;
    }

    public static void setServerPreset(MinecraftServer server, Preset set){
        if(server == null) return;

        for(ServerLevel l : server.getAllLevels()){
            if(l.getLevelData() instanceof PresetDataContainer c){
                c.setPreset(set);
            }
        }

    }

    public static void handleSetPresetPacket(SelectPresetC2SPayload selectPresetPayload, MinecraftServer server, ServerPlayer player){

        if(player.getPermissionLevel() < 3) return;

        Preset preset = selectPresetPayload.preset();
        if(!preset.enabled) return;

        Preset old = setMapPreset(preset, server);

        if(server == null) return;


        loadAndReplacePresetPack(server, preset, old);

        for(ServerLevel l : server.getAllLevels()){
            if(l.getLevelData() instanceof PresetDataContainer c){
                c.setPreset(preset);
            }
        }



    }

    private static Preset setMapPreset(Preset preset, MinecraftServer server) {
        Preset oldPreset = getServerPreset(server);

        if(oldPreset == null) oldPreset = selectedPreset;

        setServerPreset(server, preset);
        selectedPreset = preset;

        return oldPreset;

    }

    public static void checkAndEnsurePresetPackLoaded(ServerLevel serverLevel) {
        MinecraftServer loadedServer = serverLevel.getServer();

        Preset preset;


        if(serverLevel.getLevelData() instanceof PresetDataContainer c){
            preset = c.getPreset();
        }else{
            Preset sp = getServerPreset(loadedServer);
            if(sp != null)
                preset = sp;
            else
                preset = selectedPreset;
        }

//        if(preset == null){
//            preset = getServerPreset(loadedServer);
//        }

        if(preset == null || preset == DEFAULT) return;

//        @NotNull
//        Preset preset;
//
//        if(serverLevel.getLevelData() instanceof PresetDataContainer c){
//            preset = c.getPreset();
//        }else{
//            preset = selectedPreset;
//        }

//        Path packDir = loadedServer.getWorldPath(LevelResource.DATAPACK_DIR);
//
//        Path selectedPreset = packDir.resolve(preset.presetName)

        PackRepository repo = loadedServer.getPackRepository();

        if(repo.getAvailableIds().stream().anyMatch((id) -> id.contains(preset.presetName))){
            //Preset datapack already loaded
            return;
        }

        loadAndReplacePresetPack(loadedServer, preset, null);

    }

    private static void loadAndReplacePresetPack(MinecraftServer server, @NotNull Preset newPreset, @Nullable Preset oldPreset) {
        Path packDir = server.getWorldPath(LevelResource.DATAPACK_DIR);



        Path oldPack = oldPreset != null ? packDir.resolve(oldPreset.presetName + ".zip") : null;
        Path newPack = packDir.resolve(newPreset.presetName + ".zip");

        Logger logger = LogUtils.getLogger();

        logger.info("Selecting preset {} at {}", newPreset.presetName, newPack);
        if(oldPreset != null) {
            //OHHHHHHH Fuck i don't disable and reload the pack. -_-.
//            server.getPackRepository().setSelected();
//            server.getPackRepository().reload();
            server.reloadResources(
                    server.getPackRepository()
                            .getSelectedIds()
                            .stream()
                            .filter((s) -> !s.contains(oldPreset.presetName))
                            .toList())
                    .join();
            logger.info("Removing preset {} at {}", oldPreset.presetName, oldPack);

            try {
                Files.deleteIfExists(oldPack);
            } catch (IOException ignored) {
                logger.error("oh, that removal failed.....");
            }//old pack wasn't found
        }

        if(newPreset == DEFAULT){
            server.getPackRepository().reload();
            return;
        }

        Resource newPackResource = packDefaults.get(newPreset);

        try {
            InputStream packResources = newPackResource.open();

            Files.copy(packResources, newPack, StandardCopyOption.REPLACE_EXISTING);

            packResources.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Successfully loaded preset datapack!");

        server.getPackRepository().reload();

        Pack pack = getPack(server, "file/" + newPreset.presetName + ".zip");

        enablePack(server, pack);
    }

    private static Pack getPack(MinecraftServer server, String packName) {
        PackRepository packRepository = server.getPackRepository();
        Pack pack = packRepository.getPack(packName);
        if (pack == null) {
            LogUtils.getLogger().error("Preset {} pack not found!", packName);
        } else {
            boolean bl2 = packRepository.getSelectedPacks().contains(pack);
            if (bl2) {
                LogUtils.getLogger().error("Preset {} pack alread enabled!", packName);
            } else {
                return pack;
            }
        }

        return null;
    }


    private static void enablePack(MinecraftServer server, Pack pack) {
        PackRepository packRepository = server.getPackRepository();
        List<Pack> list = Lists.newArrayList(packRepository.getSelectedPacks());
        Pack.Position.BOTTOM.insert(list, pack, Pack::selectionConfig, false);
        server.reloadResources(list.stream().map(Pack::getId).collect(Collectors.toList()));
    }




    private static Preset setMapPreset(Preset preset){
        if(preset == null) System.out.println("gotchya");
        Preset oldPreset = selectedPreset;
        selectedPreset = preset;

        return oldPreset;
    }






    //DONE: store/load preset datapacks
    //1. define presets
    //1.1 make sure we can load defined presets -> new resource reload listener
    //2. store current preset choices
    //2.1 per-world presets
    //done: 3. make sure datapack is loaded if it isn't there already



    //done: create a screen that syncs a preset choice to the server

    public enum Preset implements StringRepresentable {
        VANILLA("vanilla", resource("preset/vanilla")),
        VANILLA_FIXED("vanilla_fixed", resource("preset/vanilla_fixed"), false),
        GNEISS_PREFERED("gneiss", resource("preset/gneiss"), false),
        SEPIA("sepia", resource("preset/sepia")),
        BW("black_and_white", resource("preset/blacknwhite"), false),
        REDISTRIBUTED_COLORS("redistributed", resource("preset/redistributed"), false);

        public final String presetName;
        public final ResourceLocation presetLocation;
        public final boolean enabled;

        Preset(String presetName, ResourceLocation presetLocation){
            this(presetName, presetLocation, true);
        }

        Preset(String presetName, ResourceLocation presetLocation, boolean enabled){
            this.presetName = presetName;
            this.presetLocation = presetLocation;
            this.enabled = enabled;
        }



        @Override
        public @NotNull String getSerializedName() {
            return this.presetName;
        }

        public static Preset getFromSeiralizedName(String name){
            for(Preset p : Preset.values()){
                if(p.presetName.equals(name))
                    return p;
            }
            return VANILLA;
        }


    }
}
