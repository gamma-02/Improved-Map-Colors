package org.gneisscode.improvedmapcolors.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.gneisscode.improvedmapcolors.ImprovedMapColors;
import org.gneisscode.improvedmapcolors.PresetManager;
import org.jetbrains.annotations.NotNull;

public record PresetSyncS2CPacket(PresetManager.Preset chosenPreset) implements CustomPacketPayload{

    public static final CustomPacketPayload.Type<PresetSyncS2CPacket> ID = new CustomPacketPayload.Type<>(ImprovedMapColors.SYNC_PRESET_PAYLOAD_ID);

    public static StreamCodec<RegistryFriendlyByteBuf, PresetSyncS2CPacket> PAYLOAD_CODEC = StreamCodec.composite(
            PresetManager.PRESET_PACKET_CODEC, PresetSyncS2CPacket::chosenPreset,
            PresetSyncS2CPacket::new
    );
    public static StreamCodec<ByteBuf, PresetSyncS2CPacket> NEO_PAYLOAD_CODEC = StreamCodec.composite(
            PresetManager.NEO_PRESET_PACKET_CODEC, PresetSyncS2CPacket::chosenPreset,
            PresetSyncS2CPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
