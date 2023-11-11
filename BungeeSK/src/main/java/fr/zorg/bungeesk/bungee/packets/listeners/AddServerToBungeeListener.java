package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.entities.BungeeServerBuilder;
import fr.zorg.bungeesk.common.packets.AddServerToBungeePacket;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class AddServerToBungeeListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof AddServerToBungeePacket) {
            final AddServerToBungeePacket addServerToBungeePacket = (AddServerToBungeePacket) packet;
            final BungeeServerBuilder builder = addServerToBungeePacket.getBuilder();
            final InetSocketAddress address = new InetSocketAddress(builder.getIp(), builder.getPort());
            final String name = builder.getName();
            final String motd = builder.getMotd();
            final ServerInfo infos = ProxyServer.getInstance().constructServerInfo(name, address, motd, false);
            ProxyServer.getInstance().getServers().put(name, infos);
        }
    }

}