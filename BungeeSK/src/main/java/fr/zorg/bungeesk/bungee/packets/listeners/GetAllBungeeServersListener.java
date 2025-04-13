package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetAllBungeeServersPacket;

import java.util.ArrayList;
import java.util.UUID;

public class GetAllBungeeServersListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetAllBungeeServersPacket) {
            final ArrayList<BungeeServer> servers = new ArrayList<>();
            BungeeSK.getInstance().getProxy().getServers().values().forEach(server -> {
                servers.add(new BungeeServer(server.getAddress().getAddress(), server.getAddress().getPort(), server.getName()));
            });
            return servers;
        }
        return null;
    }

}