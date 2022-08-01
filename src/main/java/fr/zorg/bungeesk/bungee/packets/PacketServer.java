package fr.zorg.bungeesk.bungee.packets;

import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.bungee.Debug;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketServer {

    private static ServerSocket serverSocket;
    private static final List<SocketServer> clientSockets = new ArrayList<>();
    private static final Thread serverThread = new Thread(PacketServer::listenForClients);
    private static List<String> whitelist;

    public static void start() {
        try {
            if (BungeeConfig.WHITELIST_IP$WHITELIST.get() == null)
                whitelist = new ArrayList<>();
            else
                whitelist = Arrays.asList(BungeeConfig.WHITELIST_IP$WHITELIST.get());
            serverSocket = new ServerSocket(BungeeConfig.PORT.get());
            Debug.log("PacketServer started on port " + BungeeConfig.PORT.get());
            serverThread.start();
            Debug.log("Now listening for clients connecting...");
        } catch (IOException e) {
            System.err.println("An error occurred during the server's launching process. \n" +
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
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void sendPacket(InetAddress address, BungeeSKPacket packet) {
        clientSockets.stream().filter(client -> client.getSocket().getInetAddress().equals(address)).findFirst().ifPresent(client -> client.sendPacket(packet));
    }

    public static void broadcastPacket(BungeeSKPacket packet) {
        clientSockets.forEach(client -> client.sendPacket(packet));
    }

    public static void stop() {
        clientSockets.forEach(SocketServer::disconnect);
        serverThread.interrupt();
        clientSockets.clear();
        try {
            serverSocket.close();
        } catch (IOException ignored) {
        }
    }

    public static List<SocketServer> getClientSockets() {
        return clientSockets;
    }
}