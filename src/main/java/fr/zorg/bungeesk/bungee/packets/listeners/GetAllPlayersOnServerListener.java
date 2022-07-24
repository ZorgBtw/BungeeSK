package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetAllPlayersOnServerPacket;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

public class GetAllPlayersOnServerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, InetAddress address, BungeeSKPacket packet) {
        if (packet instanceof GetAllPlayersOnServerPacket) {
            final GetAllPlayersOnServerPacket getAllPlayersOnServerPacket = (GetAllPlayersOnServerPacket) packet;
            final BungeeServer server = getAllPlayersOnServerPacket.getBungeeServer();
            final ServerInfo serverInfo = BungeeUtils.getServerInfo(server);

            if (serverInfo == null)
                return new EmptyFutureResponse();

            final ArrayList<BungeePlayer> players = new ArrayList<>();
            serverInfo.getPlayers().forEach(player -> players.add(new BungeePlayer(player.getName(), player.getUniqueId())));
            return players;
        }
        return null;
    }

}