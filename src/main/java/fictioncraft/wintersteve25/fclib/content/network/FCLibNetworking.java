package fictioncraft.wintersteve25.fclib.content.network;

import fictioncraft.wintersteve25.fclib.FCLibCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class FCLibNetworking {
    private static final SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    static {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(FCLibCore.MOD_ID, "networking"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(version -> true)
                .serverAcceptedVersions(version -> true)
                .simpleChannel();
    }

    public static void registerMessages() {
        INSTANCE.messageBuilder(PacketUpdateServerBE.class, nextID())
                .encoder(PacketUpdateServerBE::encode)
                .decoder(PacketUpdateServerBE::new)
                .consumer(PacketUpdateServerBE::handle)
                .add();

        INSTANCE.messageBuilder(PacketUpdateClientBE.class, nextID())
                .encoder(PacketUpdateClientBE::encode)
                .decoder(PacketUpdateClientBE::new)
                .consumer(PacketUpdateClientBE::handle)
                .add();
    }

    public static SimpleChannel getInstance() {
        return INSTANCE;
    }

    public static void sendToClient(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
