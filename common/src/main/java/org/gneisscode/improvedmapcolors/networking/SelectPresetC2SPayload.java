package org.gneisscode.improvedmapcolors.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.jetbrains.annotations.NotNull;

public record SelectPresetC2SPayload(PresetManager.Preset preset) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SelectPresetC2SPayload> ID = new CustomPacketPayload.Type<>(ImprovedMapColors.CHOOSE_PRESET_PAYLOAD_ID);

    public static StreamCodec<RegistryFriendlyByteBuf, SelectPresetC2SPayload> PAYLOAD_CODEC = StreamCodec.composite(
            PresetManager.PRESET_PACKET_CODEC, SelectPresetC2SPayload::preset,
            SelectPresetC2SPayload::new
    );
    public static StreamCodec<ByteBuf, SelectPresetC2SPayload> NEO_PAYLOAD_CODEC = StreamCodec.composite(
            PresetManager.NEO_PRESET_PACKET_CODEC, SelectPresetC2SPayload::preset,
            SelectPresetC2SPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
