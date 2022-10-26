package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.bungee.utils.FutureUtils;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetRequestFromOtherServerPacket;

import java.util.UUID;

public class GetRequestFromOtherServerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetRequestFromOtherServerPacket) {
            final GetRequestFromOtherServerPacket getRequestFromOtherServerPacket = (GetRequestFromOtherServerPacket) packet;
            getRequestFromOtherServerPacket.setFrom(BungeeUtils.getServerFromSocket(socketServer));
            final SocketServer otherServer = BungeeUtils.getSocketFromBungeeServer(getRequestFromOtherServerPacket.getServer());
            final Object response = FutureUtils.generateFuture(otherServer, getRequestFromOtherServerPacket);

            return response == null ? new EmptyFutureResponse() : response;
        }
        return null;
    }

}