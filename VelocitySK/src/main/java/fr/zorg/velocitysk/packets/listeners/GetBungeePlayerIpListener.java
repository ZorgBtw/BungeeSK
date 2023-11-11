package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeePlayerIpPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.UUID;

public class GetBungeePlayerIpListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetBungeePlayerIpPacket) {
            final GetBungeePlayerIpPacket getBungeePlayerIpPacket = (GetBungeePlayerIpPacket) packet;
            final BungeePlayer bungeePlayer = getBungeePlayerIpPacket.getBungeePlayer();
            final UUID playerUuid = bungeePlayer.getUuid();
            final Player player = VelocityUtils.getPlayer(playerUuid);

            if (player == null)
                return new EmptyFutureResponse();

            return player.getRemoteAddress().getHostName();
        }
        return null;
    }

}