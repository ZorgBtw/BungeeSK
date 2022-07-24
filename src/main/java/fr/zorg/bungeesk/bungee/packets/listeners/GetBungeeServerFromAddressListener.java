package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeeServerFromAddressPacket;

import java.net.InetAddress;
import java.util.UUID;

public class GetBungeeServerFromAddressListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GetBungeeServerFromAddressPacket) {
            final GetBungeeServerFromAddressPacket getBungeeServerFromAddressPacket = (GetBungeeServerFromAddressPacket) packet;
            final String address1 = getBungeeServerFromAddressPacket.getAddress();
            final int port = getBungeeServerFromAddressPacket.getPort();
            final BungeeServer bungeeServer = BungeeUtils.getServerFromAddress(address1, port);
            return bungeeServer == null ? new EmptyFutureResponse() : bungeeServer;
        }
        return null;
    }

}