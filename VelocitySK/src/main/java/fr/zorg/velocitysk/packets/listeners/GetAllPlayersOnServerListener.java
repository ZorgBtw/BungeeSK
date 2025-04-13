package fr.zorg.velocitysk.packets.listeners;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import fr.zorg.bungeesk.common.entities.BungeePlayer;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetAllPlayersOnServerPacket;
import fr.zorg.velocitysk.api.BungeeSKListener;
import fr.zorg.velocitysk.packets.SocketServer;
import fr.zorg.velocitysk.utils.VelocityUtils;

import java.util.ArrayList;
import java.util.UUID;

public class GetAllPlayersOnServerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetAllPlayersOnServerPacket) {
            final GetAllPlayersOnServerPacket getAllPlayersOnServerPacket = (GetAllPlayersOnServerPacket) packet;
            final BungeeServer server = getAllPlayersOnServerPacket.getBungeeServer();
            final RegisteredServer registeredServer = VelocityUtils.getRegisteredServer(server);

            if (registeredServer == null)
                return new EmptyFutureResponse();

            final ArrayList<BungeePlayer> players = new ArrayList<>();
            registeredServer.getPlayersConnected().forEach(player -> players.add(new BungeePlayer(player.getUsername(), player.getUniqueId())));
            return players;
        }
        return null;
    }

}