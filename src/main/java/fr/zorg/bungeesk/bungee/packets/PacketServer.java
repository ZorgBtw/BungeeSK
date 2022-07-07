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
    private static boolean running = false;
    private static final List<ClientSocket> clientSockets = new ArrayList<>();

    public static void start() {
        try {
            serverSocket = new ServerSocket(BungeeConfig.PORT.get());
            Debug.log("PacketServer started on port " + BungeeConfig.PORT.get());
            running = true;
            while (running) {
                final Socket socketClient = serverSocket.accept();
                final ClientSocket clientSocket = new ClientSocket(socketClient);
                clientSockets.add(clientSocket);
            }
            clientSockets.forEach(ClientSocket::disconnect);
            clientSockets.clear();
        } catch (IOException e) {
            System.err.println("An error occurred during the server's launching process. \n" +
                    "Is the port opened ? Is the port available and not occupied by another process ?");
        }
    }

    public static void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}