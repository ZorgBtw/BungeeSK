package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetRequestFromOtherServerPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.FutureUtils;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.UUID;

public class GetRequestFromOtherServerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetRequestFromOtherServerPacket) {
            final GetRequestFromOtherServerPacket getRequestFromOtherServerPacket = (GetRequestFromOtherServerPacket) packet;
            getRequestFromOtherServerPacket.setFrom(VelocityUtils.getServerFromSocket(socketServer));
            final SocketServer otherServer = VelocityUtils.getSocketFromBungeeServer(getRequestFromOtherServerPacket.getServer());
            final Object response = FutureUtils.generateFuture(otherServer, getRequestFromOtherServerPacket);

            return response == null ? new EmptyFutureResponse() : response;
        }
        return null;
    }

}