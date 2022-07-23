package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeePlayerConnectionPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;
import java.util.UUID;

public class GetBungeePlayerConnectionListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GetBungeePlayerConnectionPacket) {
            final GetBungeePlayerConnectionPacket getBungeePlayerConnectionPacket = (GetBungeePlayerConnectionPacket) packet;
            final BungeePlayer bungeePlayer = getBungeePlayerConnectionPacket.getBungeePlayer();
            final ProxiedPlayer proxiedPlayer = BungeeUtils.getPlayer(bungeePlayer);
            return proxiedPlayer != null && proxiedPlayer.isConnected();
        }
        return null;
    }

}