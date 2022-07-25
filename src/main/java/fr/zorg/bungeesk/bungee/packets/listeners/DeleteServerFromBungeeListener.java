package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.DeleteServerFromBungeePacket;
import net.md_5.bungee.api.ProxyServer;

public class DeleteServerFromBungeeListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof DeleteServerFromBungeePacket) {
            final DeleteServerFromBungeePacket deleteServerFromBungeePacket = (DeleteServerFromBungeePacket) packet;
            if (ProxyServer.getInstance().getServers().containsKey(deleteServerFromBungeePacket.getServerName()))
                ProxyServer.getInstance().getServers().remove(deleteServerFromBungeePacket.getServerName());
        }
    }

}