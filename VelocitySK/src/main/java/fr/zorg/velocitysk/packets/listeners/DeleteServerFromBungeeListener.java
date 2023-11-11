package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.DeleteServerFromBungeePacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteServerFromBungeeListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof DeleteServerFromBungeePacket) {
            final DeleteServerFromBungeePacket deleteServerFromBungeePacket = (DeleteServerFromBungeePacket) packet;
            final List<RegisteredServer> servers = BungeeSK.getServer().getAllServers().stream().filter(registeredServer -> registeredServer.getServerInfo().getName().equalsIgnoreCase(deleteServerFromBungeePacket.getServerName())).collect(Collectors.toList());
            if (!servers.isEmpty())
                BungeeSK.getServer().getAllServers().removeAll(servers);
        }
    }

}