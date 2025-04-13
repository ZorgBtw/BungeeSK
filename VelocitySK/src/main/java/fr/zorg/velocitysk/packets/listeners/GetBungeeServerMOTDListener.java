package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.server.ServerInfo;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeeServerMOTDPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.UUID;

public class GetBungeeServerMOTDListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetBungeeServerMOTDPacket) {
            final GetBungeeServerMOTDPacket getBungeeServerMOTDPacket = (GetBungeeServerMOTDPacket) packet;
            final BungeeServer bungeeServer = getBungeeServerMOTDPacket.getBungeeServer();
            final ServerInfo serverInfo = VelocityUtils.getServerInfo(bungeeServer);
            if (serverInfo == null)
                return new EmptyFutureResponse();
            return "";
        }
        return null;
    }

}