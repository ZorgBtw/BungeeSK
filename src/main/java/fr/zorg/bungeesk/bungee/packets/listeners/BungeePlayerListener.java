package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeePlayerNamedPacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerWithUUIDPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;
import java.util.UUID;

public class BungeePlayerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof BungeePlayerNamedPacket) {
            final BungeePlayerNamedPacket bungeePlayerNamedPacket = (BungeePlayerNamedPacket) packet;
            final String name = bungeePlayerNamedPacket.getName();
            final ProxiedPlayer proxiedPlayer = BungeeSK.getInstance().getProxy().getPlayer(name);
            return proxiedPlayer == null ? new EmptyFutureResponse() : new BungeePlayer(proxiedPlayer.getName(), proxiedPlayer.getUniqueId());
        }
        else if (packet instanceof BungeePlayerWithUUIDPacket) {
            final BungeePlayerWithUUIDPacket bungeePlayerWithUUIDPacket = (BungeePlayerWithUUIDPacket) packet;
            final UUID playerUuid = bungeePlayerWithUUIDPacket.getUuid();
            final ProxiedPlayer proxiedPlayer = BungeeSK.getInstance().getProxy().getPlayer(playerUuid);
            return proxiedPlayer == null ? new EmptyFutureResponse() : new BungeePlayer(proxiedPlayer.getName(), playerUuid);
        }
        return null;
    }
}