package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.SendBungeePlayerToServerPacket;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class SendBungeePlayerToServerListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendBungeePlayerToServerPacket) {
            final SendBungeePlayerToServerPacket sendBungeePlayerToServerPacket = (SendBungeePlayerToServerPacket) packet;
            final ProxiedPlayer player = BungeeUtils.getPlayer(sendBungeePlayerToServerPacket.getBungeePlayer());
            if (player == null)
                return;

            final ServerInfo serverInfo = BungeeUtils.getServerInfo(sendBungeePlayerToServerPacket.getBungeeServer());
            if (serverInfo == null)
                return;

            player.connect(serverInfo);
        }
    }

}