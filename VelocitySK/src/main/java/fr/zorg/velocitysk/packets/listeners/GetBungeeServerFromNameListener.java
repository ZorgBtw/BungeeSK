package fr.zorg.velocitysk.packets.listeners;

import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeeServerFromNamePacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.UUID;

public class GetBungeeServerFromNameListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetBungeeServerFromNamePacket) {
            final GetBungeeServerFromNamePacket getBungeeServerFromNamePacket = (GetBungeeServerFromNamePacket) packet;
            final String name = getBungeeServerFromNamePacket.getName();
            return VelocityUtils.getServerFromName(name) == null ? new EmptyFutureResponse() : VelocityUtils.getServerFromName(name);
        }
        return null;
    }

}