package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.BungeeSK;
import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetAllBungeePlayersPacket;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

public class GetAllBungeePlayersListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GetAllBungeePlayersPacket) {
            final ArrayList<BungeePlayer> bungeePlayers = new ArrayList<>();
            BungeeSK.getInstance().getProxy().getPlayers().forEach(proxiedPlayer -> {
                bungeePlayers.add(new BungeePlayer(proxiedPlayer.getName(), proxiedPlayer.getUniqueId()));
            });
            return bungeePlayers;
        }
        return null;
    }

}