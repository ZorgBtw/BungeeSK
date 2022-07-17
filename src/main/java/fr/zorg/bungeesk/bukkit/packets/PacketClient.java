package fr.zorg.bungeesk.bukkit.packets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import fr.zorg.bungeesk.bukkit.utils.ClientBuilder;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.Socket;

public class PacketClient {

    private static ClientBuilder builder;
    private static Socket socket;
    private static SocketClient client;

    public static void start(ClientBuilder builder) {
        PacketClient.builder = builder;
        Bukkit.getScheduler().runTaskAsynchronously(BungeeSK.getInstance(), () -> {
            if (socket != null && socket.isConnected())
                client.disconnect();
            try {
                socket = new Socket(builder.getAddress(), builder.getPort());
                client = new SocketClient(socket);
            } catch (IOException ignored) {
                System.err.println("An error occurred during the server's launching process. \n" +
                        "Is the port opened ? Is the port available and not occupied by another process ?");
            }
        });
    }

    public static SocketClient getClient() {
        return client;
    }

    public static ClientBuilder getBuilder() {
        return builder;
    }

}