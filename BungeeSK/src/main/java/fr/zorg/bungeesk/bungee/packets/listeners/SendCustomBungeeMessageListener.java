package fr.zorg.bungeesk.bungee.packets.listeners;

import fr.zorg.bungeesk.bungee.api.BungeeSKListener;
import fr.zorg.bungeesk.bungee.packets.SocketServer;
import fr.zorg.bungeesk.bungee.utils.BungeeUtils;
import fr.zorg.bungeesk.common.entities.BungeeServer;
import fr.zorg.bungeesk.common.packets.BungeeSKPacket;
import fr.zorg.bungeesk.common.packets.CustomBungeeMessagePacket;
import fr.zorg.bungeesk.common.packets.SendCustomBungeeMessagePacket;

public class SendCustomBungeeMessageListener extends BungeeSKListener {

    @Override
    public void onReceive(SocketServer socketServer, BungeeSKPacket packet) {
        if (packet instanceof SendCustomBungeeMessagePacket) {
            final SendCustomBungeeMessagePacket sendCustomBungeeMessagePacket = (SendCustomBungeeMessagePacket) packet;
            for (BungeeServer server : sendCustomBungeeMessagePacket.getServers()) {
                final SocketServer socketServer1 = BungeeUtils.getSocketFromBungeeServer(server);
                if (socketServer1 != null)
                    socketServer1.sendPacket(new CustomBungeeMessagePacket(BungeeUtils.getServerFromSocket(socketServer), sendCustomBungeeMessagePacket.getMessage()));
            }
        }
    }

}