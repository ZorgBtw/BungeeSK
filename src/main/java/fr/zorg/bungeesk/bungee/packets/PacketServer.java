package fr.zorg.bungeesk.bungee.packets;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import fr.zorg.bungeesk.bungee.BungeeConfig;
import fr.zorg.bungeesk.common.entities.BungeeServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PacketServer {

    private static Server server;
    private static Map<Connection, BungeeServer> serversMap = new HashMap<>();

    public static void start() {
        try {
            server = new Server();
            server.start();
            server.bind(BungeeConfig.PORT.get());
            server.addListener(new MainListener());
        } catch (IOException e) {
            System.err.println("An error appeared during the server's launching process. \n" +
                    "Is the port opened ? Is the port available and not occupied by another process ?");
        }
    }

    public static Server getServer() {
        return server;
    }

    public static Map<Connection, BungeeServer> getServersMap() {
        return serversMap;
    }

}