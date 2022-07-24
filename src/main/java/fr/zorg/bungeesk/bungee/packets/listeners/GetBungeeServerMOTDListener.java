package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeeServerMOTDPacket;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetAddress;
import java.util.UUID;

public class GetBungeeServerMOTDListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GetBungeeServerMOTDPacket) {
            final GetBungeeServerMOTDPacket getBungeeServerMOTDPacket = (GetBungeeServerMOTDPacket) packet;
            final BungeeServer bungeeServer = getBungeeServerMOTDPacket.getBungeeServer();
            final ServerInfo serverInfo = BungeeUtils.getServerInfo(bungeeServer);
            if (serverInfo == null)
                return new EmptyFutureResponse();
            return serverInfo.getMotd();
        }
        return null;
    }

}