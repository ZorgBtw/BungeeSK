package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.PacketServer;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeeServerOnlineStatusPacket;

import java.util.UUID;

public class GetBungeeServerOnlineStatusListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetBungeeServerOnlineStatusPacket) {
            final GetBungeeServerOnlineStatusPacket getBungeeServerOnlineStatusPacket = (GetBungeeServerOnlineStatusPacket) packet;
            final BungeeServer bungeeServer = getBungeeServerOnlineStatusPacket.getServer();
            final SocketServer server = BungeeUtils.getSocketFromBungeeServer(bungeeServer);

            if (server == null)
                return new EmptyFutureResponse();

            return PacketServer.getClientSockets().contains(server);
        }
        return null;
    }

}