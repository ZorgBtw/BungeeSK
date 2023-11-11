package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeePlayerNamedPacket;
import fr.zorg.bungeesk.common.packets.BungeePlayerWithUUIDPacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.UUID;

public class BungeePlayerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof BungeePlayerNamedPacket) {
            final BungeePlayerNamedPacket bungeePlayerNamedPacket = (BungeePlayerNamedPacket) packet;
            final String name = bungeePlayerNamedPacket.getName();
            final Player player = VelocityUtils.getPlayer(name);
            return player == null ? new EmptyFutureResponse() : new BungeePlayer(player.getUsername(), player.getUniqueId());
        } else if (packet instanceof BungeePlayerWithUUIDPacket) {
            final BungeePlayerWithUUIDPacket bungeePlayerWithUUIDPacket = (BungeePlayerWithUUIDPacket) packet;
            final UUID playerUuid = bungeePlayerWithUUIDPacket.getUuid();
            final Player player = VelocityUtils.getPlayer(playerUuid);
            return player == null ? new EmptyFutureResponse() : new BungeePlayer(player.getUsername(), playerUuid);
        }
        return null;
    }
}