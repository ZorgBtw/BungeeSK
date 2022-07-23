package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeePlayerPermissionPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetAddress;
import java.util.UUID;

public class BungeePlayerPermissionListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof BungeePlayerPermissionPacket) {
            final BungeePlayerPermissionPacket bungeePlayerPermissionPacket = (BungeePlayerPermissionPacket) packet;
            final BungeePlayer bungeePlayer = bungeePlayerPermissionPacket.getBungeePlayer();
            final ProxiedPlayer proxiedPlayer = BungeeUtils.getPlayer(bungeePlayer);
            if (proxiedPlayer == null)
                return false;

            return proxiedPlayer.hasPermission(bungeePlayerPermissionPacket.getPermission());
        }
        return null;
    }

}