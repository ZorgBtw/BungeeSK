package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetAllBungeeServersPacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;

import java.util.ArrayList;
import java.util.UUID;

public class GetAllBungeeServersListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetAllBungeeServersPacket) {
            final ArrayList<BungeeServer> servers = new ArrayList<>();
            BungeeSK.getServer().getAllServers().forEach(server -> {
                servers.add(new BungeeServer(server.getServerInfo().getAddress().getAddress(), server.getServerInfo().getAddress().getPort(), server.getServerInfo().getName()));
            });
            return servers;
        }
        return null;
    }

}