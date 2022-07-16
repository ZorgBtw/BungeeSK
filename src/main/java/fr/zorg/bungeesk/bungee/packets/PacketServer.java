package fr.zorg.bungeesk.bungee.packets;

import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.bungee.Debug;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PacketServer {

    private static ServerSocket serverSocket;
    private static final List<SocketServer> clientSockets = new ArrayList<>();
    private static final Thread serverThread = new Thread(PacketServer::listenForClients);

    public static void start() {
        try {
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
                final SocketServer clientSocket = new SocketServer(socketClient);
                clientSockets.add(clientSocket);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void stop() {
        serverThread.interrupt();
        clientSockets.forEach(SocketServer::disconnect);
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