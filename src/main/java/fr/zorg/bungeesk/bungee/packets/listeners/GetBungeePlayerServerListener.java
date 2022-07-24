package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.EmptyFutureResponse;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.GetBungeePlayerServerPacket;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class GetBungeePlayerServerListener extends BungeeSKListener {

    @Override
    public Object onFutureRequest(UUID uuid, SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof GetBungeePlayerServerPacket) {
            final GetBungeePlayerServerPacket getBungeePlayerServerPacket = (GetBungeePlayerServerPacket) packet;
            final ProxiedPlayer player = BungeeUtils.getPlayer(getBungeePlayerServerPacket.getBungeePlayer());
            if (player == null)
                return new EmptyFutureResponse();

            final ServerInfo serverInfo = player.getServer().getInfo();

            if (serverInfo == null)
                return new EmptyFutureResponse();

            return BungeeUtils.getServerFromInfo(serverInfo);
        }
        return null;
    }

}