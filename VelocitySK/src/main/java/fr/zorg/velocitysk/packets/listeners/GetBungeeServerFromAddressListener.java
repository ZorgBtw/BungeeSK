package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeeServerFromAddressPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.UUID;

public class GetBungeeServerFromAddressListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetBungeeServerFromAddressPacket) {
            final GetBungeeServerFromAddressPacket getBungeeServerFromAddressPacket = (GetBungeeServerFromAddressPacket) packet;
            final String address1 = getBungeeServerFromAddressPacket.getAddress();
            final int port = getBungeeServerFromAddressPacket.getPort();
            final BungeeServer bungeeServer = VelocityUtils.getServerFromAddress(address1, port);
            return bungeeServer == null ? new EmptyFutureResponse() : bungeeServer;
        }
        return null;
    }

}