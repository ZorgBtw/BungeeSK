package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.Player;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetAllBungeePlayersPacket;
import fr.zorg.velocitysk.BungeeSK;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;

import java.util.ArrayList;
import java.util.UUID;

public class GetAllBungeePlayersListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetAllBungeePlayersPacket) {
            final ArrayList<BungeePlayer> bungeePlayers = new ArrayList<>();
            BungeeSK.getServer().getAllPlayers().stream().filter(Player::isActive).forEach(proxiedPlayer -> {
                bungeePlayers.add(new BungeePlayer(proxiedPlayer.getUsername(), proxiedPlayer.getUniqueId()));
            });
            return bungeePlayers;
        }
        return null;
    }

}