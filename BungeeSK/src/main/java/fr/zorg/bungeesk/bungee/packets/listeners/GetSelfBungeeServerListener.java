package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetSelfBungeeServerPacket;

import java.util.UUID;

public class GetSelfBungeeServerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetSelfBungeeServerPacket) {
            final BungeeServer bungeeServer = BungeeUtils.getServerFromSocket(socketServer);
            return bungeeServer == null ? new EmptyFutureResponse() : bungeeServer;
        }
        return null;
    }

}