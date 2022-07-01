package fictioncraft.wintersteve25.fclib.content.network;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import fictioncraft.wintersteve25.fclib.content.base.blocks.FCLibBE;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IForceStoppable;
import fictioncraft.wintersteve25.fclib.content.base.interfaces.IHasRedstoneOutput;
import fictioncraft.wintersteve25.fclib.utils.FCLibConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateServerBE {

    private final BlockPos pos;
    private final byte packetType;
    private final int thresholdValue;

    public PacketUpdateServerBE(BlockEntity teIn, byte packetType) {
        this.pos = teIn.getBlockPos();
        this.packetType = packetType;
        this.thresholdValue = 0;
    }

    public PacketUpdateServerBE(BlockEntity teIn, byte packetType, int thresholdValueIn) {
        this.pos = teIn.getBlockPos();
        this.packetType = packetType;
        this.thresholdValue = thresholdValueIn;
    }

    public PacketUpdateServerBE(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.packetType = buffer.readByte();
        this.thresholdValue = buffer.readInt();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeByte(packetType);
        buffer.writeInt(thresholdValue);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (pos != null) {
                ServerPlayer player = ctx.get().getSender();
                FCLibBE te = (FCLibBE) player.getCommandSenderWorld().getBlockEntity(pos);
                if (te != null) {
                    switch (packetType) {
                        case FCLibConstants.PacketType.REDSTONE_INPUT:
                            if (te instanceof IForceStoppable forceStoppable) {
                                forceStoppable.toggleInverted();
                            }
                            break;
                        case FCLibConstants.PacketType.REDSTONE_OUTPUT_LOW:
                            if (te instanceof IHasRedstoneOutput tile) {
                                tile.setLowThreshold(thresholdValue);
                            } else {
                                FCLibCore.LOGGER.warn("Sent redstone output packet but tile does not support redstone output, Pos: {}", pos);
                            }
                            break;
                        case FCLibConstants.PacketType.REDSTONE_OUTPUT_HIGH:
                            if (te instanceof IHasRedstoneOutput tile) {
                                tile.setHighThreshold(thresholdValue);
                            } else {
                                FCLibCore.LOGGER.warn("Sent redstone output packet but tile does not support redstone output, Pos: {}", pos);
                            }
                            break;
                    }
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}
