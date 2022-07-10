package fr.zorg.bungeesk.bukkit.packets;

import fr.zorg.bungeesk.bukkit.BungeeSK;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PacketClient {

    private static Socket socket;
    private static SocketClient client;

    public static void start(InetAddress address, int port) {
        Bukkit.getScheduler().runTaskAsynchronously(BungeeSK.getInstance(), () -> {
            try {
                socket = new Socket(address, port);
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

}