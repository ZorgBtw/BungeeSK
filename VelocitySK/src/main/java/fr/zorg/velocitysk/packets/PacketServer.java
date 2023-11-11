package fr.zorg.velocitysk.packets;

import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.utils.BungeeConfig;
import fr.zorg.velocitysk.utils.Debug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketServer {

    private static ServerSocket serverSocket;
    private static List<SocketServer> clientSockets;
    private static Thread serverThread;
    private static List<String> whitelist;

    public static void start() {
        try {
            clientSockets = new ArrayList<>();
            serverThread = new Thread(PacketServer::listenForClients);
            if (BungeeConfig.WHITELIST_IP$WHITELIST.get() == null)
                whitelist = new ArrayList<>();
            else if (BungeeConfig.WHITELIST_IP$WHITELIST.get() instanceof String[])
                whitelist = Arrays.asList(BungeeConfig.WHITELIST_IP$WHITELIST.get());
            else
                whitelist = new ArrayList<>(BungeeConfig.WHITELIST_IP$WHITELIST.get());
            serverSocket = new ServerSocket(BungeeConfig.PORT.get());
            Debug.log("PacketServer started on port " + BungeeConfig.PORT.get());
            serverThread.start();
            Debug.log("Now listening for clients connecting...");
        } catch (IOException e) {
            BungeeSK.getLogger().atError().log("An error occurred during the server's launching process. \n" +
                    "Is the port opened ? Is the port available and not occupied by another process ?");
        }
    }

    private static void listenForClients() {
        while (!serverSocket.isClosed()) {
            try {
                final Socket socketClient = serverSocket.accept();
                if (BungeeConfig.WHITELIST_IP$ENABLE.get()) {
                    if (!whitelist.contains(socketClient.getInetAddress().getHostAddress()))
                        socketClient.close();
                }
                final SocketServer clientSocket = new SocketServer(socketClient);
                clientSockets.add(clientSocket);
            } catch (IOException ignored) {
                stop();
            }
        }
    }

    public static void broadcastPacket(BungeeSKPacket packet) {
        clientSockets.forEach(client -> {
            if (client.getSocket().isConnected())
                client.sendPacket(packet);
        });
    }

    public static void stop() {
        new ArrayList<>(clientSockets).forEach(SocketServer::disconnect);
        serverThread.interrupt();
        clientSockets.clear();
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
    }

    public static boolean isConnected() {
        return !serverSocket.isClosed() && serverThread.isAlive();
    }

    public static ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static List<SocketServer> getClientSockets() {
        return clientSockets;
    }
}