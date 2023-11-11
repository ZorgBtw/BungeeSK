package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeePlayerConnectionPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.UUID;

public class GetBungeePlayerConnectionListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetBungeePlayerConnectionPacket) {
            final GetBungeePlayerConnectionPacket getBungeePlayerConnectionPacket = (GetBungeePlayerConnectionPacket) packet;
            final BungeePlayer bungeePlayer = getBungeePlayerConnectionPacket.getBungeePlayer();
            final Player player = VelocityUtils.getPlayer(bungeePlayer);
            return player != null && player.isActive();
        }
        return null;
    }

}