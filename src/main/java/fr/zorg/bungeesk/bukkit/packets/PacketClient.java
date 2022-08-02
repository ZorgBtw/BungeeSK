package fr.zorg.bungeesk.bukkit.packets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.io.IOException;
import java.net.Socket;

public class PacketClient {

    private static ClientBuilder builder;
    private static Socket socket;
    private static SocketClient client;

    public static void start(ClientBuilder builder) {
        PacketClient.builder = builder;
        BungeeSK.runAsync(() -> {
            if (socket != null && socket.isConnected())
                client.disconnect();
            try {
                socket = new Socket(builder.getAddress(), builder.getPort());
                client = new SocketClient(socket);
            } catch (IOException ignored) {
                BungeeSK.getInstance().getLogger().severe("An error occurred during the server's launching process. \n" +
                        "Is the port opened ? Is the port available and not occupied by another process ?");
            }
        });
    }

    public static boolean isConnected() {
        return socket != null && client != null && client.isConnected();
    }

    public static SocketClient getClient() {
        return client;
    }

    public static ClientBuilder getBuilder() {
        return builder;
    }

    public static void sendPacket(BungeeSKPacket packet) {
        client.send(packet);
    }

    public static void resetSocket() {
        socket = null;
        client = null;
    }

}