package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeePlayerIpPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;
import java.util.UUID;

public class GetBungeePlayerIpListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GetBungeePlayerIpPacket) {
            final GetBungeePlayerIpPacket getBungeePlayerIpPacket = (GetBungeePlayerIpPacket) packet;
            final BungeePlayer bungeePlayer = getBungeePlayerIpPacket.getBungeePlayer();
            final UUID playerUuid = bungeePlayer.getUuid();
            final ProxiedPlayer proxiedPlayer = BungeeSK.getInstance().getProxy().getPlayer(playerUuid);

            if (proxiedPlayer == null)
                return new EmptyFutureResponse();

            return proxiedPlayer.getAddress().getHostName();
        }
        return null;
    }

}