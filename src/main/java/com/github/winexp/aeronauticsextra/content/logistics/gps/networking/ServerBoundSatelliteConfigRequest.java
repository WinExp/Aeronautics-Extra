package com.github.winexp.aeronauticsextra.content.logistics.gps.networking;

import com.github.winexp.aeronauticsextra.AeronauticsExtra;
import com.github.winexp.aeronauticsextra.content.blocks.geomatics.gps.satellite.GPSSatelliteBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public record ServerBoundSatelliteConfigRequest(BlockPos blockPos, Vec3 virtualPos) implements CustomPacketPayload {
    public static final Type<ServerBoundSatelliteConfigRequest> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AeronauticsExtra.MOD_ID, "gps_satellite_config"));

    public static final StreamCodec<ByteBuf, ServerBoundSatelliteConfigRequest> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ServerBoundSatelliteConfigRequest::blockPos,
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            ServerBoundSatelliteConfigRequest::virtualPos,
            ServerBoundSatelliteConfigRequest::new
    );

    @Override
    public Type<ServerBoundSatelliteConfigRequest> type() {
        return TYPE;
    }

    public static class RequestHandler implements IPayloadHandler<ServerBoundSatelliteConfigRequest> {
        @Override
        public void handle(ServerBoundSatelliteConfigRequest request, IPayloadContext context) {
            Player player = context.player();
            Level level = player.level();
            BlockPos blockPos = request.blockPos();
            if (!level.isLoaded(blockPos)) return;
            if (level.getBlockEntity(blockPos) instanceof GPSSatelliteBlockEntity satellite && satellite.canPlayerUse(player)) {
                satellite.setVirtualPos(request.virtualPos);
            }
        }
    }
}
